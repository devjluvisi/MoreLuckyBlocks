package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.shared.ItemViewMenu;
import devjluvisi.mlb.menus.util.MenuItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EditLuckyBlockMenu extends MenuBuilder implements Listener {

    private static final byte EDIT_NAME_SLOT = 9;
    private static final byte EDIT_LORE_SLOT = 11;
    private static final byte EDIT_MATERIAL_SLOT = 13;
    private static final byte EDIT_BREAK_PERMISSION_SLOT = 15;
    private static final byte EDIT_REQUIRED_TOOL_SLOT = 17;
    private static final byte EDIT_PARTICLES_SLOT = 27;
    private static final byte EDIT_BREAK_SOUND_SLOT = 29;
    private static final byte EDIT_PLACE_COOLDOWN_SLOT = 31;
    private static final byte EDIT_BREAK_COOLDOWN_SLOT = 33;
    private static final byte EDIT_ENCHANTED_SLOT = 35;
    private static final byte EXIT_BUTTON_SLOT = 49;
    private static HashMap<UUID, Map.Entry<EditingAttribute, String>> playersEditing;

    // For registering events.
    public EditLuckyBlockMenu(MoreLuckyBlocks plugin) {
        super(new MenuManager(plugin));
        playersEditing = new HashMap<>();
    }

    public EditLuckyBlockMenu(MenuManager manager) {
        super(manager, "Editing " + manager.getMenuData().getLuckyBlock().getInternalName());
        if (manager.isIndirectMenu()) {
            setPageType(PageType.DOUBLE_CHEST);
        } else {
            setPageType(PageType.CHEST_PLUS_PLUS);
        }
    }

    @EventHandler
    public void getInteraction(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!playersEditing.containsKey(p.getUniqueId())) {
            return;
        }


        LuckyBlock luckyBlock = manager.getPlugin().getLuckyBlocks().get(playersEditing.get(p.getUniqueId()).getValue());
        if (playersEditing.get(p.getUniqueId()).getKey().equals(EditingAttribute.ITEM_MATERIAL)) {
            e.setCancelled(true);
            if (!p.getInventory().getItemInMainHand().getType().isBlock() || p.getInventory().getItemInMainHand().getType().isAir()) {
                p.sendMessage(ChatColor.RED + "You must use a block to set this lucky block material!");
                return;
            }
            p.sendMessage(ChatColor.DARK_GREEN + "Updated the material of " + ChatColor.GOLD + luckyBlock.getInternalName());
            p.sendMessage(ChatColor.GRAY + "New Block Type " + ChatColor.WHITE + "→ " + ChatColor.GREEN + p.getInventory().getItemInMainHand().getType());
            luckyBlock.setBlockMaterial(p.getInventory().getItemInMainHand().getType());
            playersEditing.remove(p.getUniqueId());
            return;
        }
        if (playersEditing.get(p.getUniqueId()).getKey().equals(EditingAttribute.REQUIRED_TOOL)) {
            e.setCancelled(true);
            if (p.getInventory().getItemInMainHand().getType().isAir()) {
                p.sendMessage(ChatColor.DARK_GREEN + "Updated the required tool of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Required Tool " + ChatColor.WHITE + "→ " + ChatColor.GREEN + "None");
                luckyBlock.setRequiredTool(null);
            } else {
                p.sendMessage(ChatColor.DARK_GREEN + "Updated the required tool of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Required Tool " + ChatColor.WHITE + "→ " + ChatColor.GREEN + p.getInventory().getItemInMainHand().getType());
                if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                    p.sendMessage(ChatColor.DARK_GRAY + "The tool you set has custom attributes (name,lore,enchants...etc) so in order to break this lucky block players will need the exact tool you used (or a direct copy).");
                }

                luckyBlock.setRequiredTool(p.getInventory().getItemInMainHand());
            }

            playersEditing.remove(p.getUniqueId());
            return;
        }
    }

    @EventHandler
    public void getChatMessage(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (!playersEditing.containsKey(p.getUniqueId())) {
            return;
        }
        e.setCancelled(true);

        EditingAttribute attribute = playersEditing.get(p.getUniqueId()).getKey();
        LuckyBlock luckyBlock = manager.getPlugin().getLuckyBlocks().get(playersEditing.get(p.getUniqueId()).getValue());

        // If the user wishes not to edit the attribute.
        if (e.getMessage().equalsIgnoreCase("cancel")) {
            playersEditing.remove(p.getUniqueId());
            p.sendMessage(ChatColor.GREEN + "You cancelled your editing action.");
            return;
        }

        // Get the attribute the user clicked on to edit.
        switch (attribute) {
            case NAME -> {
                if (!PluginConstants.INTERNAL_NAME_RANGE.isInRange(Util.makeInternal(e.getMessage()).length())) {
                    p.sendMessage(ChatColor.RED + "Specified name is not in range (" + PluginConstants.INTERNAL_NAME_RANGE.getMin() + "-" + PluginConstants.INTERNAL_NAME_RANGE.getMax() + ")");
                    return;
                }
                p.sendMessage(ChatColor.DARK_GREEN + "Updated the name of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Item Name " + ChatColor.WHITE + "→ " + ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                p.sendMessage(ChatColor.GRAY + "New Lucky Block Name " + ChatColor.WHITE + "→ " + ChatColor.GREEN + Util.makeInternal(e.getMessage()));
                luckyBlock.setInternalName(Util.makeInternal(e.getMessage()));
                luckyBlock.setName(e.getMessage());
            }
            case PLACE_COOLDOWN -> {
                int cooldown = NumberUtils.toInt(e.getMessage(), -1);
                if (cooldown == -1) {
                    p.sendMessage(ChatColor.RED + "You did not input a valid number. Valid numbers are between 0 and " + Integer.MAX_VALUE + ".");
                    return;
                }
                p.sendMessage(ChatColor.DARK_GREEN + "Updated the place cooldown of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Place Cooldown " + ChatColor.WHITE + "→ " + ChatColor.GREEN + cooldown);
                luckyBlock.setPlaceCooldown(cooldown);
            }
            case BREAK_COOLDOWN -> {
                int cooldown = NumberUtils.toInt(e.getMessage(), -1);
                if (cooldown == -1) {
                    p.sendMessage(ChatColor.RED + "You did not input a valid number. Valid numbers are between 0 and " + Integer.MAX_VALUE + ".");
                    return;
                }
                p.sendMessage(ChatColor.DARK_GREEN + "Updated the break cooldown of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Break Cooldown " + ChatColor.WHITE + "→ " + ChatColor.GREEN + cooldown);
                luckyBlock.setBreakCooldown(cooldown);
            }
            case BREAK_PERMISSION -> {
                p.sendMessage(ChatColor.DARK_GREEN + "Updated the break permission of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Break Permission " + ChatColor.WHITE + "→ " + ChatColor.GREEN + e.getMessage().toLowerCase());
                luckyBlock.setBreakPermission(e.getMessage().toLowerCase());
            }
            case BREAK_SOUND -> {
                if (Arrays.stream(Sound.values()).noneMatch(s -> s.name().equals(e.getMessage().toUpperCase()))) {
                    p.sendMessage(ChatColor.RED + "Unknown sound specified: " + e.getMessage().toUpperCase());
                    return;
                }
                p.sendMessage(ChatColor.DARK_GREEN + "Updated the break sound of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Break Sound " + ChatColor.WHITE + "→ " + ChatColor.GREEN + e.getMessage().toUpperCase());
                luckyBlock.setBreakSound(Sound.valueOf(e.getMessage().toUpperCase()));
            }
            case ITEM_LORE -> {
                if (e.getMessage().equalsIgnoreCase("none")) {
                    luckyBlock.setLore(Collections.emptyList());
                } else {
                    luckyBlock.setLore(Util.asNormalColoredString(Arrays.stream(e.getMessage().split(",")).toList()));
                }

                p.sendMessage(ChatColor.DARK_GREEN + "Updated the lore of " + ChatColor.GOLD + luckyBlock.getInternalName());
                p.sendMessage(ChatColor.GRAY + "New Lore " + ChatColor.WHITE + "→ " + ChatColor.GREEN + e.getMessage());
            }
            default -> {
                return;
            }
        }
        playersEditing.remove(p.getUniqueId());
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[1], MenuItem.whitePlaceholder().asItem());
        Arrays.fill(content[2], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[3], MenuItem.whitePlaceholder().asItem());
        Arrays.fill(content[4], MenuItem.blackPlaceholder().asItem());
        if (manager.isIndirectMenu()) {
            Arrays.fill(content[5], MenuItem.whitePlaceholder().asItem());
        }
        LuckyBlock lb = manager.getMenuData().getLuckyBlock();
        content[1][0] = new MenuItem(MenuItem.SpecialItem.EDIT_NAME).addLine("\n").addLine(ChatColor.AQUA + lb.getInternalName()).asItem();
        content[1][2] = new MenuItem(MenuItem.SpecialItem.EDIT_LORE).addLine("\n").addAllLine((lb.getLore().isEmpty() ? new String[] { ChatColor.AQUA + "None" } : lb.getLore().toString().substring(1, lb.getLore().toString().length() - 1).split(", "))).asItem();

        content[1][4] = new MenuItem(MenuItem.SpecialItem.EDIT_MATERIAL).with(lb.getBlockMaterial()).addLine("\n").addLine(ChatColor.AQUA + lb.getBlockMaterial().name()).asItem();
        content[1][6] = new MenuItem(MenuItem.SpecialItem.EDIT_BREAK_PERMISSION).addLine("\n").addLine(ChatColor.AQUA + (lb.getBreakPermission().length() == 0 ? "None" : lb.getBreakPermission())).asItem();
        content[1][8] = new MenuItem(MenuItem.SpecialItem.EDIT_TOOL).with(lb.getRequiredTool() != null ? lb.getRequiredTool().getType() : Material.DIAMOND_PICKAXE).addLine("\n").addLine(ChatColor.AQUA + (lb.getRequiredTool() == null ? "None" : (lb.getRequiredTool().hasItemMeta() && lb.getRequiredTool().getItemMeta().hasDisplayName() ? lb.getRequiredTool().getItemMeta().getDisplayName() : lb.getRequiredTool().getType().name()))).asItem();
        MenuItem particles = new MenuItem(MenuItem.SpecialItem.EDIT_PARTICLES).addLine("\n");
        if (lb.getParticleMap().isEmpty()) {
            particles.addLine(ChatColor.AQUA + "None");
        } else {
            lb.getParticleMap().entrySet().forEach(k -> particles.addLine(ChatColor.AQUA + ParticlesSubMenu.transform(k.getKey().name()) + " " + ChatColor.GRAY + ParticlesSubMenu.stars(k.getValue())));
        }

        content[3][0] = particles.asItem();

        content[3][2] = new MenuItem(MenuItem.SpecialItem.EDIT_SOUND).addLine("\n").addLine(ChatColor.AQUA + (lb.getBreakSound() == null ? "None" : lb.getBreakSound().name())).asItem();
        content[3][4] = new MenuItem(MenuItem.SpecialItem.EDIT_PLACE_COOLDOWN).addLine("\n").addLine(ChatColor.AQUA + String.valueOf((lb.getPlaceCooldown() == 0 ? "None" : lb.getPlaceCooldown()))).asItem();
        content[3][6] = new MenuItem(MenuItem.SpecialItem.EDIT_BREAK_COOLDOWN).addLine("\n").addLine(ChatColor.AQUA + String.valueOf((lb.getBreakCooldown() == 0 ? "None" : lb.getBreakCooldown()))).asItem();
        content[3][8] = new MenuItem(MenuItem.SpecialItem.EDIT_ENCHANTED).addLine("\n").addLine(ChatColor.AQUA + String.valueOf(lb.isItemEnchanted())).asItem();
        if (manager.isIndirectMenu()) {
            content[5][4] = new MenuItem(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        }
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.EDIT_LUCKY_BLOCK_ATTRIBUTES;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        Player p = manager.getPlayer();
        switch (slot) {
            case EDIT_NAME_SLOT -> {
                playersEditing.put(p.getUniqueId(), Map.entry(EditingAttribute.NAME, manager.getMenuData().getLuckyBlock().getInternalName()));
                p.sendMessage(ChatColor.AQUA + "Type in chat the new name for this lucky block.");
                p.sendMessage(ChatColor.AQUA + "This will effect both the item name and the \"internal\" name which is used in the config.");
                p.sendMessage(ChatColor.GRAY + "Color codes are allowed for item name.");
            }
            case EDIT_LORE_SLOT -> {
                playersEditing.put(manager.getPlayer().getUniqueId(), Map.entry(EditingAttribute.ITEM_LORE, manager.getMenuData().getLuckyBlock().getInternalName()));
                p.sendMessage(ChatColor.AQUA + "Type in chat the new lore for this lucky block.");
                p.sendMessage(ChatColor.AQUA + "Separate lore lines using a comma (\",\"). Chat colors are allowed.");
                p.sendMessage(ChatColor.GRAY + "Enter \"none\" to remove all lore.");
            }
            case EDIT_MATERIAL_SLOT -> {
                playersEditing.put(manager.getPlayer().getUniqueId(), Map.entry(EditingAttribute.ITEM_MATERIAL, manager.getMenuData().getLuckyBlock().getInternalName()));
                p.sendMessage(ChatColor.AQUA + "Right-Click on an item to set it as the new block for this Lucky Block.");
            }
            case EDIT_BREAK_PERMISSION_SLOT -> {
                playersEditing.put(manager.getPlayer().getUniqueId(), Map.entry(EditingAttribute.BREAK_PERMISSION, manager.getMenuData().getLuckyBlock().getInternalName()));
                p.sendMessage(ChatColor.AQUA + "Type in chat the new break permission for this lucky block.");
            }
            case EDIT_REQUIRED_TOOL_SLOT -> {
                if (clickType == ClickType.RIGHT && manager.getMenuData().getLuckyBlock().hasRequiredTool()) {
                    manager.open(manager.getPlayer(), new ItemViewMenu(manager, manager.getMenuData().getLuckyBlock().getRequiredTool()));
                    return;
                }
                playersEditing.put(manager.getPlayer().getUniqueId(), Map.entry(EditingAttribute.REQUIRED_TOOL, manager.getMenuData().getLuckyBlock().getInternalName()));
                p.sendMessage(ChatColor.AQUA + "Right-Click on a tool/item in your main hand to set it as the required tool to break this lucky block.");
                p.sendMessage(ChatColor.GRAY + "Right-Click on AIR to disable a required tool.");
            }
            case EDIT_PARTICLES_SLOT -> {
                manager.open(view.getPlayer(), MenuType.EDIT_PARTICLES_SUB);
                return;
            }
            case EDIT_BREAK_SOUND_SLOT -> {
                playersEditing.put(manager.getPlayer().getUniqueId(), Map.entry(EditingAttribute.BREAK_SOUND, manager.getMenuData().getLuckyBlock().getInternalName()));
                p.sendMessage(ChatColor.AQUA + "Enter the new sound you want this lucky block to play when broken.");
                TextComponent cmp = new TextComponent("Click here to view possible sounds.");
                cmp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to go to spigot sound repository.")));
                cmp.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"));
                cmp.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
                p.spigot().sendMessage(cmp);
                p.sendMessage(ChatColor.GRAY + "Test a sound using /mlb playsound <sound>");
                p.sendMessage(ChatColor.GRAY + "Enter \"none\" to remove sound.");
            }
            case EDIT_PLACE_COOLDOWN_SLOT -> {
                p.sendMessage(ChatColor.AQUA + "Enter in chat the new cooldown you want this lucky block to have between placements (seconds).");
                p.sendMessage(ChatColor.AQUA + "This cooldown can be overridden with a permission mlb.override.<lb-name>");
                playersEditing.put(manager.getPlayer().getUniqueId(), Map.entry(EditingAttribute.PLACE_COOLDOWN, manager.getMenuData().getLuckyBlock().getInternalName()));
            }
            case EDIT_BREAK_COOLDOWN_SLOT -> {
                p.sendMessage(ChatColor.AQUA + "Enter in chat the new break cooldown you want this lucky block to have between breaks (seconds).");
                p.sendMessage(ChatColor.AQUA + "This cooldown can be overridden with a permission mlb.override.<lb-name>");
                playersEditing.put(manager.getPlayer().getUniqueId(), Map.entry(EditingAttribute.BREAK_COOLDOWN, manager.getMenuData().getLuckyBlock().getInternalName()));
            }
            case EDIT_ENCHANTED_SLOT -> {
                // Not added to hash map.
                if (manager.getMenuData().getLuckyBlock().isItemEnchanted()) {
                    p.sendMessage(ChatColor.GREEN + "You disenchanted " + ChatColor.DARK_GREEN + manager.getMenuData().getLuckyBlock().getInternalName());
                    manager.getMenuData().getLuckyBlock().setItemEnchanted(false);
                } else {
                    p.sendMessage(ChatColor.GREEN + "You enchanted " + ChatColor.DARK_GREEN + manager.getMenuData().getLuckyBlock().getInternalName());
                    manager.getMenuData().getLuckyBlock().setItemEnchanted(true);
                }
                view.close();
                return;
            }
            case EXIT_BUTTON_SLOT -> {
                manager.regress();
                return;
            }
            default -> {
                return;
            }
        }
        p.sendMessage(ChatColor.GRAY + "Type \"" + ChatColor.RED + "cancel" + ChatColor.GRAY + "\" to cancel this action.");
        p.sendMessage("");
        view.close();
    }


    /**
     * The current part of the lucky block the player is editing.
     */
    private enum EditingAttribute {
        NAME, ITEM_LORE, ITEM_MATERIAL, BREAK_PERMISSION, REQUIRED_TOOL, PARTICLES, BREAK_SOUND, PLACE_COOLDOWN,
        BREAK_COOLDOWN, TOGGLE_ENCHANTED, NONE
    }
}
