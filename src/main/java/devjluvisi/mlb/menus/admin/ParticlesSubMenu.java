package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ParticlesSubMenu extends MenuBuilder {

    public ParticlesSubMenu(MenuManager manager) {
        super(manager, "Edit Particles", PageType.DOUBLE_CHEST);
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        for(int i = 0; i < getPageType().getRow(); i++) {
            Arrays.fill(content[i], new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }
        content[4][1]=content[4][2] = content[4][3] = content[4][4] = content[4][5] = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        content[1][6] = content[2][6] = content[3][6] = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        content[4][6] = new MenuItem(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        content[4][7] = new MenuItem().with(Material.EMERALD).with("&aSave").addLine("&7Click to save your changes.").asItem();
        content[1][7] = new MenuItem().with(Material.GOLD_INGOT).with("&6Max Particles").addLine("&7Click to select a high amount").addLine("&7of particles to spawn.").asItem();
        content[2][7] = new MenuItem().with(Material.IRON_INGOT).with("&6Some Particles").addLine("&7Click to select a regular amount").addLine("&7of particles to spawn.").asItem();
        content[3][7] = new MenuItem().with(Material.BRICK).with("&6Little Particles").addLine("&7Click to select a low amount").addLine("&7of particles to spawn.").asItem();
        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 6; j++) {
                content[i][j] = new ItemStack(Material.AIR);
            }
        }
        return content;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {

    }

    @Override
    public MenuType type() {
        return MenuType.EDIT_PARTICLES_SUB;
    }
}
