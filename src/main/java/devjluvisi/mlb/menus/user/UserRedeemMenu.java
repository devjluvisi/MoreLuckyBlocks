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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

        content[0][7] = new MenuItem(Material.RED_DYE).asItem();
        content[0][6] = new MenuItem().with(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[0][5] = new MenuItem(Material.LIME_DYE).asItem();
        content[1][5] = new MenuItem().with(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[1][6] = lb.asItem(manager.getPlugin(), lbRequestAmount);
        content[1][7] = new MenuItem().with(Material.WHITE_STAINED_GLASS_PANE).asItem();

        content[4][7] = new MenuItem().with(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[4][6] = new MenuItem().with(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[4][5] = new MenuItem().with(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[5][5] = new MenuItem().with(Material.GREEN_TERRACOTTA).asItem();
        content[5][6] = new MenuItem().with(Material.WHITE_STAINED_GLASS_PANE).asItem();
        content[5][7] = new MenuItem().with(Material.RED_TERRACOTTA).asItem();

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
                ItemStack[] items = manager.getPlayer().getInventory().getContents();
                List<ItemStack> requiredItems = manager.getPlugin().getExchangesManager().getItems(lb.getInternalName());
                for (ItemStack i : requiredItems) {
                    if (!Arrays.asList(items).contains(i)) {
                        manager.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Could not complete transaction.");
                        TextComponent textComponent = new TextComponent("You are missing an item: \"" + ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "x" + i.getAmount() + " " +  (i.hasItemMeta() && Objects.requireNonNull(i.getItemMeta()).hasDisplayName() ? i.getItemMeta().getDisplayName() : i.getType().name()) + ChatColor.GRAY + "\".");
                        textComponent.addExtra("\nHover over item to view information.");
                        textComponent.setColor(net.md_5.bungee.api.ChatColor.GRAY);
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getItemInfo())));
                        manager.getPlayer().spigot().sendMessage(textComponent);
                        return;
                    }
                }
                manager.getPlayer().sendMessage("Completed.");


            }
            case 52 -> {
                manager.getPlayer().sendMessage(ChatColor.RED + "Cancelled transaction.");
                view.close();
            }

        }
    }

    /**
     * Tells if the current players inventory has the item required to
     * do the exchange.
     * @param item The item to check for.
     * @return If the players inventory has the required items.
     */
    private boolean hasItem(ItemStack item) {
        return false;
    }

    private String getItemInfo() {
        StringBuilder itemInfo = new StringBuilder();
        return itemInfo.toString();
    }


}
