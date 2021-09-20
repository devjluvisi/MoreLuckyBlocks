package devjluvisi.mlb.menus;

import fr.dwightstudio.dsmapi.MenuView;
import fr.dwightstudio.dsmapi.SimpleMenu;
import fr.dwightstudio.dsmapi.pages.PageType;
import fr.dwightstudio.dsmapi.utils.ItemCreator;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ExampleMenu extends SimpleMenu {


    @Override
    public String getName() {
        return ChatColor.DARK_PURPLE + "Menu Example";
    }


    @Override
    public ItemStack[] getContent() {
        // Methode to generate a 2D array of the shape of the inventory
        ItemStack[][] content = getPageType().getBlank2DArray();

        // Add the items
        content[1][4] = new ItemCreator(Material.APPLE).setName("Give Apple").getItem();
        content[2][8] = new ItemCreator(Material.ARROW).setName("Next Page").getItem();

        // Flatten the 2D Array (the methods return a 1D Array)
        return getPageType().flatten(content);
    }

    @Override
    public PageType getPageType() {
        return PageType.CHEST;
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