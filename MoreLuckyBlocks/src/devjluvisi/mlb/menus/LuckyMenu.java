package devjluvisi.mlb.menus;

import java.util.ArrayList;
import java.util.LinkedList;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.Menu;
import devjluvisi.mlb.api.gui.pages.Page;
import devjluvisi.mlb.menus.pages.ChangeRarity;
import devjluvisi.mlb.menus.pages.Confirm;
import devjluvisi.mlb.menus.pages.Confirm.Action;
import devjluvisi.mlb.menus.pages.DropsPage;
import devjluvisi.mlb.menus.pages.EditDrop;
import devjluvisi.mlb.menus.pages.ListPage;
import devjluvisi.mlb.menus.pages.LootPage;

/**
 * The main GUI for the application in which all various pages are run on.
 * There is only one of these GUIs in the application, different visual GUIs are different pages.
 * 
 * @author jacob
 *
 */
public class LuckyMenu extends Menu {

	private MoreLuckyBlocks plugin;
	
	private int blockIndex;
	private int dropIndex;
	private Action confirmAction;
	
	private LinkedList<BasePage> pages;

	/*
	 * Various instance variables are here for this menu in order to track pages
	 * such as what lucky block the user is on and what they are trying to edit.
	 * 
	 * Not all instance variables will be used by every page but they can be set
	 * whenever to maintain data.
	 */

	public enum View {
		LIST_LUCKYBLOCKS, LIST_DROPS, LIST_LOOT, EDIT_DROP, CHANGE_RARITY, CONFIRM_ACTION;
	}

	public LuckyMenu(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		pages = new LinkedList<BasePage>();
		// Note: order does not matter
		pages.add(new ListPage(this));
		pages.add(new DropsPage(this));
		pages.add(new LootPage(this));
		pages.add(new EditDrop(this));
		pages.add(new Confirm(this));
		pages.add(new ChangeRarity(this));
		
	}
	
	public void refresh() {
		pages.clear();
		pages.add(new ListPage(this));
		pages.add(new DropsPage(this));
		pages.add(new LootPage(this));
		pages.add(new EditDrop(this));
		pages.add(new Confirm(this));
		pages.add(new ChangeRarity(this));
		
	}

	@Override
	public String getName() {
		return "UNUSED MAIN MENU";
	}

	@Override
	public int getPageCount() {
		return pages.size();
	}

	protected MoreLuckyBlocks getPlugin() {
		return plugin;
	}

	public int getBlockIndex() {
		return blockIndex;
	}

	public void setBlockIndex(int blockIndex) {
		this.blockIndex = blockIndex;
	}

	public int getDropIndex() {
		return dropIndex;
	}

	public void setDropIndex(int dropIndex) {
		this.dropIndex = dropIndex;
	}

	public Action getConfirmAction() {
		return confirmAction;
	}

	public void setConfirmAction(Action confirmAction) {
		this.confirmAction = confirmAction;
	}

	@Override
	public Page[] getPages() {
		Page[] pages = new Page[getPageCount()];
		for (int i = 0; i < getPageCount(); i++) {
			pages[i] = (Page) this.pages.get(i);
		}
		return pages;
	}

}
