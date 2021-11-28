package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.events.custom.DataChangedEvent;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class EditDropMenu extends MenuBuilder {

    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;


    private int addPotionEffectStage;

    public EditDropMenu(MenuManager manager) {
        super(manager, "Editing Drop", PageType.CHEST);
        this.addPotionEffectStage = 0;
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        this.lb = manager.getMenuData().getLuckyBlock();
        this.lbDrop = manager.getMenuData().getDrop();

        setMenuName("Editing Drop #" + lb.indexOf(lbDrop));
        content[0][0] = new MenuItem().with(Material.OAK_SIGN).with("&7Editing Drop &8-> &3#" + lb.indexOf(lbDrop))
                .addLine("&8- &7SHIFT+RIGHT Click items to add.")
                .addLine("&8- &7SHIFT+RIGHT Click items in GUI to remove.")
                .addLine("&8- &7Add potion effects and commands using items below.")
                .addLine("&8- &7Add a structure by doing &e/mlb struct&7.")
                .addLine("&oRemember to save before exiting!")
                .asItem();

        content[0][1] = MenuItem.blackPlaceholder().asItem();
        content[1][0] = MenuItem.blackPlaceholder().asItem();
        content[1][1] = MenuItem.blackPlaceholder().asItem();

        final ArrayList<LootProperty> dropList = lbDrop.getLoot();

        int arrIndex = 0;

        // Replace empty slots with loot if it exists.
        for (int i = 0; i < 2; i++) {
            for (int j = 2; j < 9; j++) {
                if (arrIndex == dropList.size()) {
                    break;
                }
                content[i][j] = dropList.get(arrIndex).asItem();
                arrIndex++;
            }
        }

        Arrays.fill(content[2], MenuItem.redPlaceholder().asItem());
        if (manager.getPlugin().getSettingsManager().isAutoSaveEnabled()) {
            content[2][4] = new MenuItem().of(MenuItem.SpecialItem.AUTO_SAVING).asItem();
        } else {
            content[2][4] = new MenuItem().of(MenuItem.SpecialItem.SAVE_BUTTON).asItem();
        }

        content[2][5] = new MenuItem().of(MenuItem.SpecialItem.ADD_STRUCTURE).asItem();
        content[2][6] = new MenuItem().of(MenuItem.SpecialItem.ADD_COMMAND).asItem();
        content[2][7] = new MenuItem().of(MenuItem.SpecialItem.ADD_POTION_EFFECT).asItem();
        content[2][8] = new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.EDIT_LOOT;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) return;
        if (clickType == ClickType.SHIFT_LEFT) {
            if (isPlayerSlot(slot)) {
                if (canAddItems()) {
                    manager.getPlayer().sendMessage(ChatColor.RED + "You are only allowed to add up to "
                            + PluginConstants.MAX_LOOT_AMOUNT + " items per drop.");
                    return;
                }
                lbDrop.getItems().add(new LuckyBlockItem(itemStack));
            } else {
                if (!(((slot >= 2) && (slot <= 8)) || ((slot >= 11) && (slot <= 17)))) {
                    return;
                }
                lbDrop.removeLoot(itemStack);
            }
            view.reopen();
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.SAVE_BUTTON).asItem())) {
            manager.getPlugin().getServer().getPluginManager().callEvent(new DataChangedEvent());
            manager.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your edits have been saved to config.");
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.ADD_COMMAND).asItem())) {
            if (this.canAddItems()) {
                manager.getPlayer().sendMessage(ChatColor.RED + "You are only allowed to add up to "
                        + PluginConstants.MAX_LOOT_AMOUNT + " items per drop.");
                return;
            }

            manager.getPlayer().sendMessage("");
            manager.getPlayer().sendMessage(ChatColor.GRAY + "You are attempting to add a command to drop: "
                    + ChatColor.BLUE + this.getDropIndex());
            manager.getPlayer().sendMessage(ChatColor.RED + "Please enter a command in the chat to add...");
            manager.getPlayer().sendMessage(ChatColor.GRAY + "To cancel this action, type \"/exit\".");
            manager.getPlayer().sendMessage("");

            manager.getPlugin().getPlayersEditingDrop().put(manager.getPlayer().getUniqueId(), this);
            view.close();
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.ADD_POTION_EFFECT).asItem())) {
            if (this.canAddItems()) {
                manager.getPlayer().sendMessage(ChatColor.RED + "You are only allowed to add up to "
                        + PluginConstants.MAX_LOOT_AMOUNT + " items per drop.");
                return;
            }
            manager.getPlayer().sendMessage("");
            manager.getPlayer().sendMessage(ChatColor.GRAY + "You are attempting to add a potion effect to drop: "
                    + ChatColor.BLUE + this.getDropIndex());
            manager.getPlayer().sendMessage(ChatColor.RED + "Please enter a potion effect in the chat to add...");
            manager.getPlayer().sendMessage(ChatColor.GRAY + "To cancel this action, type \"exit\".");
            manager.getPlayer().sendMessage(
                    ChatColor.GOLD + "(1/3) > " + ChatColor.YELLOW + "Enter the name of the potion effect to add.");
            manager.getPlayer().sendMessage("");

            setAddPotionEffectStage(1);
            manager.getPlugin().getPlayersEditingDrop().put(manager.getPlayer().getUniqueId(), this);
            view.close();

        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.ADD_STRUCTURE).asItem())) {
            manager.open(manager.getPlayer(), MenuType.EDIT_STRUCTURE);
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem())) {
            manager.regress(view);
        }
    }

    private boolean canAddItems() {
        return lbDrop.getLoot().size() >= PluginConstants.MAX_LOOT_AMOUNT;
    }

    public int getDropIndex() {
        return lb.indexOf(lbDrop);
    }

    public MenuResource getResource() {
        return manager.getMenuData();
    }

    public int getBlockIndex() {
        return manager.getPlugin().getLuckyBlocks().indexOf(lb);
    }

    public int getAddPotionEffectStage() {
        return this.addPotionEffectStage;
    }

    public void setAddPotionEffectStage(int addPotionEffectStage) {
        this.addPotionEffectStage = addPotionEffectStage;
    }

}
