package devjluvisi.mlb.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.mojang.datafixers.View;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.DropProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockCommand;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.blocks.drops.LuckyBlockPotionEffect;
import devjluvisi.mlb.menus.BaseMenu.SpecialItem;
import devjluvisi.mlb.queries.EditRequest;
import devjluvisi.mlb.queries.Query;
import fr.dwightstudio.dsmapi.MenuView;
import fr.dwightstudio.dsmapi.pages.PageType;
import net.md_5.bungee.api.ChatColor;

public class EditDropMenu extends BaseMenu {

	private ViewDropLootMenu prevMenu;
	private LuckyBlockDrop drop;
	private String dropIdentifier;
	private UUID playerUUID;
	
	public EditDropMenu(MoreLuckyBlocks plugin, ViewDropLootMenu prevMenu,  LuckyBlockDrop drop, String dropIdentifier, UUID playerUUID) {
		super(plugin, "Editing Drop: " + dropIdentifier, PageType.CHEST);
		this.drop = drop;
		this.dropIdentifier = dropIdentifier;
		this.playerUUID = playerUUID;
		this.prevMenu = prevMenu;
	}
	
	public EditDropMenu(EditDropMenu previous) {
		super(previous.plugin, previous.getName(), previous.getPageType());
		this.drop = previous.drop;
		this.dropIdentifier = previous.dropIdentifier;
		this.playerUUID = previous.playerUUID;
		this.prevMenu = previous.prevMenu;
	}
	

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();
		ItemStack editDrop = new ItemStack(Material.OAK_SIGN);
		ItemMeta meta = editDrop.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Editing Drop " + ChatColor.DARK_GRAY + "-> " + ChatColor.DARK_AQUA + dropIdentifier);
		meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Drag items from your inventory", ChatColor.GRAY + "to add them to the block.", ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Add potion effects and commands", ChatColor.GRAY + "using the items below.", ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Remember to " + ChatColor.DARK_GREEN + "Save " + ChatColor.GRAY + "before exiting."));
		editDrop.setItemMeta(meta);
		content[0][0] = editDrop;
		content[0][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		content[1][0] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		content[1][1] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		for(int i = 0; i < 5; i++) content[2][i] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		content[2][5] = super.getSpecialItem(SpecialItem.SAVE_BUTTON);
		content[2][6] = super.getSpecialItem(SpecialItem.ADD_COMMAND);
		content[2][7] = super.getSpecialItem(SpecialItem.ADD_POTION_EFFECT);
		content[2][8] = super.getSpecialItem(SpecialItem.EXIT_BUTTON);
		
		Query q = Query.queryContains(plugin, playerUUID);
        if(q != null && q instanceof EditRequest) {
            EditRequest req = (EditRequest)q;
            if(req.getType() == InventoryType.PLAYER) {
            	//TODO UPDATE
            	if(req.getItem() instanceof LuckyBlockItem) {
            		drop.getItems().add(new LuckyBlockItem(req.getItem().asItem()));
            	}else if(req.getItem() instanceof LuckyBlockPotionEffect) {
            		drop.getItems().add(new LuckyBlockItem(req.getItem().asItem()));
            	}else if(req.getItem() instanceof LuckyBlockCommand) {
            		drop.getItems().add(new LuckyBlockItem(req.getItem().asItem()));
            	}

            }else if(req.getType() == InventoryType.CHEST) {
            	if(req.getItem() instanceof LuckyBlockItem) {
            		drop.getItems().remove(new LuckyBlockItem(req.getItem().asItem()));
            	}else if(req.getItem() instanceof LuckyBlockPotionEffect) {
            		drop.getPotionEffects().remove((LuckyBlockPotionEffect)req.getItem());
            	}else if(req.getItem() instanceof LuckyBlockCommand) {
            		drop.getCommands().remove((LuckyBlockCommand)req.getItem());
            	}
            	
            }
            q.execute();
        }
		ArrayList<DropProperty> dropList = drop.getLoot();
		
        
        int arrIndex = 0;
        
        for(int i = 0; i < 2; i++) {
        	for(int j = 2; j < 9; j++) {
        		if(arrIndex == dropList.size()) break;
        		content[i][j] = dropList.get(arrIndex).asItem();
        		arrIndex++;
        	}
        }
        new EditRequest(playerUUID, plugin, this).add();
		return getPageType().flatten(content);
	} 
	
	public static Event getEvent(MoreLuckyBlocks plugin) {
		return new Event(plugin);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack item) {
		if(item == null) return;
		if(item.equals(super.getSpecialItem(SpecialItem.EXIT_BUTTON))) {
			new ViewDropLootMenu(prevMenu).open(view.getPlayer());
			return;
		}
	}
	
	public LuckyBlockDrop getDrop() {
		return this.drop;
	}
	
	
}
class Event implements Listener {
	MoreLuckyBlocks plugin;
	public Event(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void inventoryClick(InventoryClickEvent e) {
		// We only want left clicks
		if(e.getClick() != ClickType.SHIFT_LEFT || e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) {
			return;
		}
		
		//2-8 and 11-17 are the slots in the inventory where items can appear.
		if(e.getClickedInventory().getType() != InventoryType.PLAYER) {
			if(!(e.getSlot() >= 2 && e.getSlot() <= 8 || e.getSlot() >= 11 && e.getSlot() <= 17)) {
				return;
			}
		}
		
				
		Query q = Query.queryContains(plugin, e.getWhoClicked().getUniqueId());
		if(q == null ) {
			return;
		}
		if(!(q instanceof EditRequest)) {
			return;
		}
		e.setCancelled(true);
		// Does Nothing, removes from main array list
		q.execute();
		
		// User wants to add an item.
		if(e.getClickedInventory().getType() == InventoryType.PLAYER) {
			EditRequest addRequest = new EditRequest(e.getWhoClicked().getUniqueId(), plugin, ((EditRequest)q).getPreviousMenu());
			
			//int index = ((EditRequest)q).getPreviousMenu().getDrop().indexOf(((EditRequest)q).getItem());
			//addRequest.setItem((DropProperty)((EditRequest)q).getPreviousMenu().getDrop().getLoot().get(index));
			addRequest.setItem((DropProperty) new LuckyBlockItem(e.getCurrentItem()));
			
			addRequest.setType(InventoryType.PLAYER);
			addRequest.add();
		}
		// User wants to remove an item.
		if(e.getClickedInventory().getType() == InventoryType.CHEST) {
			EditRequest removeRequest = new EditRequest(e.getWhoClicked().getUniqueId(), plugin, ((EditRequest)q).getPreviousMenu());
			
			int index = ((EditRequest)q).getPreviousMenu().getDrop().indexOf(e.getCurrentItem());
			if(index == -1) {
				new EditDropMenu(((EditRequest)q).getPreviousMenu()).open((Player)e.getWhoClicked());
				return;
			}
			removeRequest.setItem((DropProperty)((EditRequest)q).getPreviousMenu().getDrop().getLoot().get(index));
			
			removeRequest.setType(InventoryType.CHEST);
			removeRequest.add();
			
		}
		new EditDropMenu(((EditRequest)q).getPreviousMenu()).open((Player)e.getWhoClicked());
		
	}
}