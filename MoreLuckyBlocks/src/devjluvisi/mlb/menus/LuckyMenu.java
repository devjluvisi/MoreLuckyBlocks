package devjluvisi.mlb.menus;

import java.util.ArrayList;
import java.util.List;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.Menu;
import devjluvisi.mlb.api.gui.pages.Page;
import devjluvisi.mlb.menus.pages.Confirm;
import devjluvisi.mlb.menus.pages.Confirm.Action;
import devjluvisi.mlb.menus.pages.DropsPage;
import devjluvisi.mlb.menus.pages.EditDrop;
import devjluvisi.mlb.menus.pages.ListPage;
import devjluvisi.mlb.menus.pages.LootPage;

public class LuckyMenu extends Menu {
	
	private MoreLuckyBlocks plugin;
	private ArrayList<BasePage> pages;
	
	public LuckyMenu(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		pages = new ArrayList<BasePage>();
		
		pages.add(new ListPage(plugin));
	}
	
	public enum View {
		LIST_LUCKYBLOCKS(0), LIST_DROPS(1), LIST_LOOT(2), EDIT_DROP(3), CONFIRM_ACTION(4);
		
		int i;
		View(int i) {
		this.i = i;	
		}
		
		public int toInt() {
			return this.i;
		}
		
	}
	
	public LuckyMenu(MoreLuckyBlocks plugin, int blockIndex) {
		this.plugin = plugin;
		pages = new ArrayList<BasePage>();
		
		pages.add(new ListPage(plugin));
		//pages.add(new Confirm(plugin));
		pages.add(new DropsPage(plugin, blockIndex));
	}
	
	public LuckyMenu(MoreLuckyBlocks plugin, int blockIndex, int lootIndex) {
		this.plugin = plugin;
		pages = new ArrayList<BasePage>();
		
		pages.add(new ListPage(plugin));
		//pages.add(new Confirm(plugin));
		pages.add(new DropsPage(plugin, blockIndex));
		pages.add(new LootPage(plugin, blockIndex, lootIndex));
		pages.add(new EditDrop(plugin, blockIndex, lootIndex));
	}
	
	public LuckyMenu(MoreLuckyBlocks plugin, int blockIndex, int lootIndex, Action confirmAction) {
		this.plugin = plugin;
		pages = new ArrayList<BasePage>();
		
		pages.add(new ListPage(plugin));
		//pages.add(new Confirm(plugin));
		pages.add(new DropsPage(plugin, blockIndex));
		pages.add(new LootPage(plugin, blockIndex, lootIndex));
		pages.add(new EditDrop(plugin, blockIndex, lootIndex));
		pages.add(new Confirm(plugin, blockIndex, lootIndex, confirmAction));
	}
	
	@Override
	public String getName() {
		return "UNUSED MAIN MENU";
	}

	@Override
	public int getPageCount() {
		return pages.size();
	}

	@Override
	public Page[] getPages() {
		Page[] pages = new Page[getPageCount()];
		for(int i = 0; i < getPageCount(); i++) {
			pages[i] = (Page)this.pages.get(i);
		}
		return pages;
	}
	
	
}
