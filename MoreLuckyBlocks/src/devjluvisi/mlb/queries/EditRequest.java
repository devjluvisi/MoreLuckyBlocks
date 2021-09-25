package devjluvisi.mlb.queries;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.drops.DropProperty;
import devjluvisi.mlb.menus.EditDropMenu;

public class EditRequest extends Query {
	EditDropMenu menu;
	DropProperty item;
	InventoryType type;
	
	public EditRequest(UUID u, MoreLuckyBlocks plugin, EditDropMenu menu) {
		super(u, plugin);
		this.menu = new EditDropMenu(menu);
		this.item = null;
		this.type = null;
	}
	
	public void setItem(DropProperty item) {
		this.item = item;
	}
	
	public DropProperty getItem() {
		return item;
	}
	
	public void setType(InventoryType type) {
		this.type = type;
	}
	
	public InventoryType getType() {
		return type;
	}
	
	public EditDropMenu getPreviousMenu() {
		return menu;
	}

	@Override
	protected void runProcess() {
		
	}
	
	

}
