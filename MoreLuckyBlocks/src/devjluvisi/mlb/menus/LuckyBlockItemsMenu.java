package devjluvisi.mlb.menus;

import fr.dwightstudio.dsmapi.MenuView;
import fr.dwightstudio.dsmapi.SimpleMenu;
import fr.dwightstudio.dsmapi.pages.PageType;
import fr.dwightstudio.dsmapi.utils.ItemCreator;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;

public class LuckyBlockItemsMenu extends BaseMenu {
	
	private LuckyBlock block;
	private MoreLuckyBlocks plugin;
	
	public LuckyBlockItemsMenu(MoreLuckyBlocks plugin, LuckyBlock block) {
		super(plugin, ChatColor.DARK_PURPLE + "Viewing: " + block.getInternalName(), PageType.CHEST);
		this.plugin = plugin;
		this.block = block;
	}

    @Override
    public ItemStack[] getContent() {
        ItemStack[][] content = getPageType().getBlank2DArray();
        int rowCount = 0;
        int colCount = 0;
        for(int i = 0; i < block.getDroppableItems().size(); i++) {
        	if(colCount == 9) {
        		colCount = 0;
        		rowCount++;
        	}
        	ItemStack dropSlot = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        	ItemMeta meta = dropSlot.getItemMeta();
        	meta.setDisplayName(ChatColor.GREEN + "Drop: " + (i+1));
        	int size = block.getDroppableItems().get(i).getItems().size() + block.getDroppableItems().get(i).getCommands().size() + block.getDroppableItems().get(i).getPotionEffects().size();
        	meta.setLore(Arrays.asList(ChatColor.AQUA + "You have a drop set for this item.", ChatColor.GRAY.toString()+ChatColor.BOLD + "Total Drops: " + ChatColor.GREEN + size, ChatColor.GRAY.toString() + ChatColor.ITALIC + "Click to configure/delete drop."));
        	dropSlot.setItemMeta(meta);
        	content[rowCount][colCount] = dropSlot;
        	colCount++;
        }
        for(int i = 0; i < 3; i++) {
        	for(int j = 0; j < 9; j++) {
        		if(content[i][j] == null) {
        			content[i][j] = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "You have no drop set for this slot.").getItem();
        		}
        	}
        }
        content[2][0] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][1] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][2] = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        content[2][3] = super.getSpecialItem(SpecialItem.EXIT_BUTTON);
        content[2][4] = super.getPlaceholderItem(Material.PAPER, ChatColor.RESET + "Edit Lucky Block Attributes", Arrays.asList(ChatColor.GRAY + "Use various commands to edit additional", ChatColor.GRAY + "attributes about this lucky block.", ChatColor.YELLOW + "/mlb edit <name>"));
        content[2][5] = super.getSpecialItem(SpecialItem.ADD_NEW_DROP);
        content[2][6] = super.getSpecialItem(SpecialItem.REMOVE_ALL_DROPS);
        content[2][7] = super.getSpecialItem(SpecialItem.PREVIOUS_PAGE);
        content[2][8] = super.getSpecialItem(SpecialItem.NEXT_PAGE);
        return getPageType().flatten(content);
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if(itemStack == null) return;
        
        switch(itemStack.getType()) {
            case APPLE:
                view.close();
                view.getPlayer().sendMessage("You clicked on an Apple!");
                break;
            case ARROW:
                view.close();
                view.getPlayer().sendMessage("You clicked on to go to the next page!");
                break;
            default:
                break;
        }
    }
}