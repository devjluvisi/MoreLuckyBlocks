package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class ListMenu extends MenuBuilder  {

    private final Random rand;

    public ListMenu(MenuManager manager) {
        super(manager, "Lucky Block List", PageType.DOUBLE_CHEST);
        rand = new Random();
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[5], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[2], new MenuItem().with(Material.HOPPER).with("&c&lEMPTY").addLine("&7You can add a lucky block here!").addLine("&7Up to &e14&7 custom lucky blocks can be added!").addLine("&e/mlb create").asItem());
        Arrays.fill(content[3], new MenuItem().with(Material.HOPPER).with("&c&lEMPTY").addLine("&7You can add a lucky block here!").addLine("&7Up to &e14&7 custom lucky blocks can be added!").addLine("&e/mlb create").asItem());
        Arrays.fill(content[1], randomPane());
        Arrays.fill(content[4], randomPane());
        content[2][0] = content[3][0] = content[2][8] = content[3][8] = randomPane();

        int xCoord = 1;
        int yCoord = 2;

        MenuItem i;
        for(LuckyBlock lb : manager.getPlugin().getLuckyBlocks()) {
            if(yCoord==-1) break;
            i = new MenuItem()
                    .with(lb.getBlockMaterial())
                    .with("&r" + lb.getInternalName())
                    .addLine("&3Item Name&7: " + lb.getName())
                    .addLine("&3Break Permission&7: " + lb.getBreakPermission())
                    .addLine("&3Material&7: " + lb.getBlockMaterial().name())
                    .addLine("&3Default Luck&7: " + lb.getDefaultBlockLuck())
                    .addLine("&3# of Droppable Items&7: " + lb.getDroppableItems().size())
                    .addLine("&3Lore Length&7: " + lb.getLore().size())
                    .addLine("\n")
                    .addLine("&e&lCLICK TO CONFIGURE");
            content[yCoord][xCoord] = i.asItem();
            // Move on to the next index
            xCoord = xCoord + 1 < 9 ? (xCoord + 1) : 1;
            yCoord = xCoord == 1 ? yCoord != 4 ? (yCoord + 1) : -1 : yCoord;
        }
        return content;
    }

    public ItemStack randomPane() {
        return new ItemStack(new Material[] {
                Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE,
                Material.CYAN_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE
        }[rand.nextInt(8)]);
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if(itemStack == null) return;
        manager.setMenuData(new MenuResource().with(manager.getPlugin().getLuckyBlocks().get(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName())));
        manager.open(view.getPlayer(), MenuType.LIST_DROPS);
    }

    @Override
    public MenuType type() {
        return MenuType.LIST_LUCKY_BLOCKS;
    }
}
