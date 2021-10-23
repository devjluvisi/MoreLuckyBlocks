package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuResource;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

// mlb.lb.view.<lucky-internal-name> required to view lucky block in GUI.
public class UserListMenu extends MenuBuilder {

    private final Random rand;

    public UserListMenu(MenuManager manager) {
        super(manager, "Lucky Block List", PageType.DOUBLE_CHEST);
        rand = new Random();
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[5], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[1], randomPane());
        Arrays.fill(content[4], randomPane());
        content[2][0] = content[3][0] = content[2][8] = content[3][8] = randomPane();
        int xCoord = 1;
        int yCoord = 2;

        MenuItem i;
        for (LuckyBlock lb : manager.getPlugin().getLuckyBlocks()) {
            if (yCoord == -1) break;
//            if(!manager.getPlayer().hasPermission("mlb.lb.view." + lb.getInternalName())) {
//                continue;
//            }
            i = new MenuItem()
                    .with(lb.getBlockMaterial())
                    .with(lb.getName())
                    .addLine("&8(&7" + lb.getInternalName() + "&8)")
                    .addLine("&3Possible Drops&7: " + lb.getDroppableItems().size())
                    .addLine("&3/mlb info " + lb.getInternalName())
                    .addLine("&7for more information.")
                    .addLine("\n");
            if (manager.getPlayer().hasPermission(lb.getBreakPermission())) {
                i.addLine("&a✔ You can break this lucky block!");
            } else {
                i.addLine("&c✖ You cannot break this lucky block.");
            }
            i.addLine("\n");
            i.addLine("&7Click to view contents.");
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
    public MenuType type() {
        return MenuType.USER_LIST_LUCKY_BLOCKS;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) return;
        LuckyBlock lb = manager.getPlugin().getLuckyBlocks().stream().filter(e -> e.getName().equals(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName())).findFirst().orElse(null);
        if (lb == null) return;
        manager.setMenuData(new MenuResource().with(lb));
        manager.open(view.getPlayer(), MenuType.USER_LIST_DROPS);
    }
}
