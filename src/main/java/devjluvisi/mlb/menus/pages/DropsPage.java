package devjluvisi.mlb.menus.pages;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.utils.ItemCreator;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;
import devjluvisi.mlb.menus.pages.Confirm.Action;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class DropsPage extends BasePage {

    private static final byte CHEST_ROW_SIZE = 18;

    private int startingIndex;

    public DropsPage(LuckyMenu menu) {
        super(menu);
        this.setMenuName(ChatColor.DARK_PURPLE + "Viewing: "
                + this.plugin.getLuckyBlocks().get(menu.getBlockIndex()).getInternalName());
        this.startingIndex = 0;
    }

    @Override
    public ItemStack[] getContent() {
        final ItemStack[][] content = this.getPageType().getBlank2DArray();
        int rowCount = 0;
        int colCount = 0;
        for (int i = this.startingIndex; i < this.plugin.getLuckyBlocks().get(super.getBlockIndex()).getDroppableItems()
                .size(); i++) {
            if (colCount == 9) {
                colCount = 0;
                rowCount++;
            }
            final ItemStack dropSlot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            final ItemMeta meta = dropSlot.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.GREEN + "Drop: " + (i));
            final int size = this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().get(i)
                    .getItems().size()
                    + this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().get(i).getCommands()
                    .size()
                    + this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().get(i)
                    .getPotionEffects().size();
            meta.setLore(Arrays.asList(ChatColor.AQUA + "You have a drop set for this item.",
                    ChatColor.GRAY.toString() + ChatColor.BOLD + "Total Drops: " + ChatColor.GREEN + size,
                    ChatColor.GRAY.toString() + ChatColor.BOLD + "Rarity: " + ChatColor.LIGHT_PURPLE
                            + this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().get(i)
                            .getRarity(),
                    ChatColor.GRAY.toString() + ChatColor.ITALIC + "Click to configure/delete drop."));
            dropSlot.setItemMeta(meta);
            if (rowCount == 3) {
                continue;
            }
            content[rowCount][colCount] = dropSlot;
            colCount++;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                if (content[i][j] == null) {
                    content[i][j] = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE)
                            .setName(ChatColor.GRAY + "You have no drop set for this slot.").getItem();
                }
            }
        }
        content[2][1] = super.getSpecialItem(SpecialItem.EXIT_BUTTON);
        content[2][0] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][6] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][2] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][3] = super.getSpecialItem(SpecialItem.ADD_NEW_DROP);
        content[2][4] = super.getSpecialItem(SpecialItem.REMOVE_ALL_DROPS);
        content[2][5] = super.getSpecialItem(SpecialItem.DELETE_LUCKY_BLOCK);
        final int maxPages = (this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().size()
                / CHEST_ROW_SIZE) + 1;
        final int currPage = (this.startingIndex / CHEST_ROW_SIZE) + 1;
        content[2][7] = super.getPlaceholderItem(Material.FEATHER,
                ChatColor.YELLOW + "Previous Page " + ChatColor.GRAY + "(" + currPage + "/" + maxPages + ")",
                List.of(ChatColor.GRAY + "Go back to the previous page."));
        content[2][8] = super.getPlaceholderItem(Material.ARROW,
                ChatColor.YELLOW + "Next Page " + ChatColor.GRAY + "(" + currPage + "/" + maxPages + ")",
                List.of(ChatColor.GRAY + "Go to the next page."));
        return this.getPageType().flatten(content);
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        if (itemStack.getItemMeta().getDisplayName().contains("Drop:")) {

            final int dropIndex = Integer
                    .parseInt(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).split(": ")[1]);

            this.setDropIndex(dropIndex);
            this.traverse(view, View.LIST_LOOT);
            return;
        }
        if (itemStack.equals(this.getSpecialItem(SpecialItem.ADD_NEW_DROP))) {
            this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().add(new LuckyBlockDrop());
            this.setDropIndex(this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().size() - 1);
            this.traverse(view, View.EDIT_DROP);
            return;
        }
        if (itemStack.equals(this.getSpecialItem(SpecialItem.REMOVE_ALL_DROPS))) {
            if (this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().size() == 1) {
                view.getPlayer().sendMessage(ChatColor.RED + "You must have more then one drop in this lucky block.");
                return;
            }
            this.setConfirmAction(Action.REMOVE_ALL_DROPS);
            this.traverse(view, View.CONFIRM_ACTION);
            return;
        }
        if (itemStack.equals(this.getSpecialItem(SpecialItem.DELETE_LUCKY_BLOCK))) {
            if (this.plugin.getLuckyBlocks().size() == 1) {
                view.getPlayer().sendMessage(ChatColor.RED + "You must have at least one lucky block.");
                return;
            }
            this.setConfirmAction(Action.REMOVE_LUCKY_BLOCK);
            this.traverse(view, View.CONFIRM_ACTION);
            return;

        }
        if (itemStack.getType().equals(Material.FEATHER)) {
            if ((this.startingIndex - CHEST_ROW_SIZE) <= 0) {
                this.startingIndex = 0;
            } else {
                this.startingIndex -= CHEST_ROW_SIZE;
            }

            view.reopen();
            return;
        }
        if (itemStack.getType().equals(Material.ARROW)) {
            final int maxPages = (this.plugin.getLuckyBlocks().get(this.getBlockIndex()).getDroppableItems().size()
                    / CHEST_ROW_SIZE) + 1;
            final int currPage = (this.startingIndex / CHEST_ROW_SIZE) + 1;
            if (currPage == maxPages) {
                return;
            }
            this.startingIndex += CHEST_ROW_SIZE;
            view.reopen();
            return;
        }
        if (itemStack.equals(this.getSpecialItem(SpecialItem.EXIT_BUTTON))) {
            this.traverse(view, View.LIST_LUCKYBLOCKS);
            return;
        }
    }

    @Override
    public View identity() {
        return View.LIST_DROPS;
    }

}
