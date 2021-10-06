package devjluvisi.mlb.menus;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.Menu;
import devjluvisi.mlb.api.gui.pages.Page;
import devjluvisi.mlb.menus.pages.*;
import devjluvisi.mlb.menus.pages.Confirm.Action;

import java.util.LinkedList;

/**
 * <h2>LuckyMenu</h2>
 * The main GUI for the application in which all various pages are run on. There
 * is only one of these GUIs in the application, different visual GUIs are
 * different pages.
 *
 * @author jacob
 */
public class LuckyMenu extends Menu {

    private final MoreLuckyBlocks plugin;
    /**
     * List of all different pages in the menu.
     */
    private final LinkedList<BasePage> pages;
    private int blockIndex;
    private int dropIndex;
    private Action confirmAction;

    /*
     * Various instance variables are here for this menu in order to track pages
     * such as what lucky block the user is on and what they are trying to edit.
     *
     * Not all instance variables will be used by every page but they can be set
     * whenever to maintain data.
     */

    public LuckyMenu(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        this.pages = new LinkedList<>();
        // Note: order does not matter
        this.addPages();
    }

    private void addPages() {
        this.pages.add(new ListPage(this));
        this.pages.add(new DropsPage(this));
        this.pages.add(new LootPage(this));
        this.pages.add(new EditDrop(this));
        this.pages.add(new Confirm(this));
        this.pages.add(new ChangeRarity(this));
    }

    public void refresh() {
        this.pages.clear();
        this.addPages();
    }

    @Override
    public String getName() {
        return "UNUSED MAIN MENU";
    }

    @Override
    public int getPageCount() {
        return this.pages.size();
    }

    protected MoreLuckyBlocks getPlugin() {
        return this.plugin;
    }

    public int getBlockIndex() {
        return this.blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public int getDropIndex() {
        return this.dropIndex;
    }

    public void setDropIndex(int dropIndex) {
        this.dropIndex = dropIndex;
    }

    public Action getConfirmAction() {
        return this.confirmAction;
    }

    public void setConfirmAction(Action confirmAction) {
        this.confirmAction = confirmAction;
    }

    @Override
    public Page[] getPages() {
        final Page[] pages = new Page[this.getPageCount()];
        for (int i = 0; i < this.getPageCount(); i++) {
            pages[i] = this.pages.get(i);
        }
        return pages;
    }

    public enum View {
        LIST_LUCKYBLOCKS, LIST_DROPS, LIST_LOOT, EDIT_DROP, CHANGE_RARITY, CONFIRM_ACTION
    }

}
