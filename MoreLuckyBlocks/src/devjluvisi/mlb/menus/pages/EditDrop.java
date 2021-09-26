package devjluvisi.mlb.menus.pages;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.blocks.drops.LuckyBlockItem;
import devjluvisi.mlb.menus.BasePage;
import devjluvisi.mlb.menus.LuckyMenu;
import devjluvisi.mlb.menus.LuckyMenu.View;

public class EditDrop extends BasePage {
	
	private int blockIndex;
	private int dropIndex;
	private int addPotionEffectStage;

	public EditDrop(MoreLuckyBlocks plugin, int blockIndex, int dropIndex) {
		super(plugin, "Editing Drop #" + String.valueOf(dropIndex));
		this.blockIndex = blockIndex;
		this.dropIndex = dropIndex;
		this.addPotionEffectStage = 0;
	}

	@Override
	public ItemStack[] getContent() {
		ItemStack[][] content = getPageType().getBlank2DArray();
		ItemStack editDrop = new ItemStack(Material.OAK_SIGN);
		ItemMeta meta = editDrop.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Editing Drop " + ChatColor.DARK_GRAY + "-> " + ChatColor.DARK_AQUA + dropIndex);
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
		
		ArrayList<LootProperty> lootList = plugin.getLuckyBlocks().get(blockIndex).getDroppableItems().get(dropIndex).getLoot();

        int arrIndex = 0;
        
        for(int i = 0; i < 2; i++) {
        	for(int j = 2; j < 9; j++) {
        		if(arrIndex == lootList.size()) break;
        		content[i][j] = lootList.get(arrIndex).asItem();
        		arrIndex++;
        	}
        }
        return getPageType().flatten(content);
	}

	@Override
	public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
		plugin.getServer().getConsoleSender().sendMessage("Slot: " + slot);
		if(itemStack==null || itemStack.getType().isAir()) return;
		if(clickType == ClickType.SHIFT_LEFT) {
			if(isPlayerSlot(slot)) {
				
				plugin.getLuckyBlocks().get(blockIndex).getDroppableItems().get(dropIndex).getItems().add(new LuckyBlockItem(itemStack));
					view.setPage(View.EDIT_DROP.toInt());
			}else {
				if(slot >= 2 && slot <= 8 || slot >= 11 && slot <= 17) {
					plugin.getLuckyBlocks().get(blockIndex).getDroppableItems().get(dropIndex).removeLoot(itemStack);
					view.setPage(View.EDIT_DROP.toInt());
				}
			}
		}
		
		if(itemStack.equals(getSpecialItem(SpecialItem.SAVE_BUTTON))) {
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.ADD_COMMAND))) {
			view.close();	
			view.getPlayer().sendMessage("");
			view.getPlayer().sendMessage(ChatColor.GRAY + "You are attempting to add a command to drop: " + ChatColor.BLUE + String.valueOf(dropIndex));
			view.getPlayer().sendMessage(ChatColor.RED + "Please enter a command in the chat to add...");
			view.getPlayer().sendMessage(ChatColor.GRAY + "To cancel this action, type \"/exit\".");
			view.getPlayer().sendMessage("");
			plugin.getPlayersEditingDrop().put(view.getPlayer().getUniqueId(), new LuckyMenu(plugin, blockIndex, dropIndex));
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.ADD_POTION_EFFECT))) {
			view.close();	
			view.getPlayer().sendMessage("");
			view.getPlayer().sendMessage(ChatColor.GRAY + "You are attempting to add a potion effect to drop: " + ChatColor.BLUE + String.valueOf(dropIndex));
			view.getPlayer().sendMessage(ChatColor.RED + "Please enter a potion effect in the chat to add...");
			view.getPlayer().sendMessage(ChatColor.GRAY + "To cancel this action, type \"exit\".");
			view.getPlayer().sendMessage(ChatColor.GOLD + "(1/3) > " + ChatColor.YELLOW + "Enter the name of the potion effect to add.");
			view.getPlayer().sendMessage("");
			
			LuckyMenu menu = new LuckyMenu(plugin, blockIndex, dropIndex);
			((EditDrop)menu.getPage(View.EDIT_DROP.toInt())).setAddPotionEffectStage(1);
			plugin.getPlayersEditingDrop().put(view.getPlayer().getUniqueId(), menu);
			
			return;
		}
		if(itemStack.equals(getSpecialItem(SpecialItem.EXIT_BUTTON))) {
			view.setPage(View.LIST_LOOT.toInt());
		}
	}

	public int getBlockIndex() {
		return blockIndex;
	}

	public int getDropIndex() {
		return dropIndex;
	}

	public int getAddPotionEffectStage() {
		return addPotionEffectStage;
	}

	public void setAddPotionEffectStage(int addPotionEffectStage) {
		this.addPotionEffectStage = addPotionEffectStage;
	}
	
	
	
	
	
	
	
	

}
