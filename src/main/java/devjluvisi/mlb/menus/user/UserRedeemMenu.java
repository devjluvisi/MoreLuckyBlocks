package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class UserRedeemMenu extends MenuBuilder {

    private LuckyBlock lb;
    private int lbRequestAmount;


    public UserRedeemMenu(MenuManager manager) {
        super(manager, "Redeem Menu", PageType.DOUBLE_CHEST);
        this.lbRequestAmount = 1;
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        lb = manager.getMenuData().getLuckyBlock();
        setMenuName("Redeem for: " + lb.getInternalName());
        for (int i = 0; i < 6; i++) {
            Arrays.fill(content[i], MenuItem.blackPlaceholder().asItem());
        }
        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 1; j < 4; j++) {
                if (index == manager.getPlugin().getExchangesManager().getItems(lb.getInternalName()).size()) {
                    content[i][j] = new ItemStack(Material.AIR);
                    continue;
                }
                content[i][j] = manager.getPlugin().getExchangesManager().getItems(lb.getInternalName()).get(index);
                index++;
            }
        }

        content[0][7] = new MenuItem().with(Material.RED_DYE).with("&c-1 Quantity")
                .addLine("&7Decrease the amount of")
                .addLine("&7requested lucky blocks to: &6" + (lbRequestAmount-1) + "&7.").asItem();
        content[0][6] = new MenuItem(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[0][5] = new MenuItem().with(Material.LIME_DYE).with("&a+1 Quantity")
                .addLine("&7Increase the amount of")
                .addLine("&7requested lucky blocks to: &6" + (lbRequestAmount+1) + "&7.").asItem();
        content[1][5] = new MenuItem(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[1][6] = lb.asItem(manager.getPlugin(), lbRequestAmount);
        content[1][7] = new MenuItem(Material.WHITE_STAINED_GLASS_PANE).asItem();

        content[4][7] = new MenuItem(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[4][6] = new MenuItem(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[4][5] = new MenuItem(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[5][5] = new MenuItem().with(Material.GREEN_TERRACOTTA)
                .with("&2&lConfirm")
                .addLine("&7Confirm your transaction.")
                .addLine("&7to buy &fx" + lbRequestAmount + " &7of")
                .addLine(lb.getName()).asItem();
        content[5][6] = new MenuItem(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[5][7] = new MenuItem().with(Material.RED_TERRACOTTA)
                .with("&c&lDecline")
                .addLine("&7Decline the transaction.").asItem();

        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.USER_REDEEM;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        // Slot 5 = Increment Lucky Block Count
        // Slot 15 = Lucky Block
        // Slot 7 = Decrement Lucky Block Count
        // Slot 50 = Confirm Exchange.
        // Slot 52 = Cancel Exchange.

        switch (slot) {
            case 5 -> {
                if (lbRequestAmount == 64) return;
                this.lbRequestAmount++;
                view.reopen();
            }
            case 7 -> {
                if (lbRequestAmount == 1) return;
                this.lbRequestAmount--;
                view.reopen();
            }
            //TODO: Make the inventory contains work regardless of item stack size.
            //TODO: Improve messages.
            //TODO: Make full item info appear in hover text.
            case 50 -> {
                List<ItemStack> requiredItems = new ArrayList<>(manager.getPlugin().getExchangesManager().getItems(lb.getInternalName()));
                final Inventory playerInventory = manager.getPlayer().getInventory();

                for (ItemStack i : requiredItems) {
                    int amt = (i.getAmount()*requiredItems.stream().filter(e -> e.equals(i)).toList().size() * lbRequestAmount);
                    if(!playerInventory.containsAtLeast(i, amt)) {
                        manager.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Could not complete transaction.");
                        TextComponent textComponent = new TextComponent("You are missing an item: \"" + ChatColor.RESET + ChatColor.WHITE + "x" + amt + " " + (i.hasItemMeta() && Objects.requireNonNull(i.getItemMeta()).hasDisplayName() ? i.getItemMeta().getDisplayName() : i.getType().name()) + ChatColor.GRAY + "\".");
                        textComponent.addExtra("\nHover over item to view information.");
                        textComponent.setColor(net.md_5.bungee.api.ChatColor.GRAY);
                        ItemStack clone = i.clone();
                        clone.setAmount(amt);
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getItemInfo(clone))));
                        manager.getPlayer().spigot().sendMessage(textComponent);
                        view.close();
                        return;
                    }
                }

                if (Arrays.stream(playerInventory.getStorageContents()).noneMatch(Objects::isNull)) {
                    manager.getPlayer().sendMessage(ChatColor.RED + "Please have at least one open slot in your inventory to exchange!");
                    return;
                }
                //TODO: Play Chime, Particles maybe?
                for(int i = 0; i < lbRequestAmount; i++) {
                    requiredItems.forEach(playerInventory::removeItem);
                }
                playerInventory.addItem(lb.asItem(manager.getPlugin(), lbRequestAmount));
                manager.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Success!");
                manager.getPlayer().sendMessage("You purchased " + lbRequestAmount + "x " + lb.getName() + ChatColor.RESET + " lucky blocks!");
            }
            case 52 -> {
                manager.getPlayer().sendMessage(ChatColor.RED + "Cancelled transaction.");
                view.close();
            }

        }
    }

    private static String getItemInfo(ItemStack item) {
        StringBuilder itemInfo = new StringBuilder();
        ItemMeta meta = item.getItemMeta();

        // GOLDEN_SWORD -> Golden Sword
        itemInfo.append(WordUtils.capitalize(StringUtils.lowerCase(item.getType().name().replace("_", " ")))).append(" x").append(item.getAmount());

        if (item.hasItemMeta()) {
            assert meta != null;
            if (meta.hasDisplayName()) {
                itemInfo.append("\n");
                itemInfo.append(meta.getDisplayName()).append(ChatColor.RESET);
            }
            if (meta.hasLore()) {
                itemInfo.append("\n");
                for (String s : Objects.requireNonNull(meta.getLore())) {
                    itemInfo.append(s).append("\n");
                }
                itemInfo.deleteCharAt(itemInfo.length() - 1);
            }
            if (meta.hasEnchants()) {
                itemInfo.append(ChatColor.RESET).append("\n");
                for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                    itemInfo.append(StringUtils.capitalize(entry.getKey().getKey().getKey())).append(" ").append(entry.getValue()).append("\n");
                }
                itemInfo.deleteCharAt(itemInfo.length() - 1);
            }
        }
        return itemInfo.toString();
    }


}
