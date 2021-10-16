package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.api.items.CustomItemMeta;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.admin.TrackCommand;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import devjluvisi.mlb.util.luckyblocks.LuckyValues;
import devjluvisi.mlb.util.luckyblocks.MapLocation3D;
import jdk.jfr.Enabled;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.xml.validation.Validator;
import java.util.*;

public class TrackMenu extends MenuBuilder {

    private int currentPage;
    private final int maxPage;

    public TrackMenu(MenuManager manager) {
        super(manager, "Placed Lucky Blocks (" + "0" + "/" + ((manager.getPlugin().getAudit().getMap().size() / TrackCommand.BLOCKS_PER_PAGE) + (manager.getPlugin().getAudit().getMap().size()%TrackCommand.BLOCKS_PER_PAGE != 0 ? 1 : 0)) + ")", PageType.DOUBLE_CHEST);
        this.currentPage = 1;
        this.maxPage = (manager.getPlugin().getAudit().getMap().size() / TrackCommand.BLOCKS_PER_PAGE) + (manager.getPlugin().getAudit().getMap().size()%TrackCommand.BLOCKS_PER_PAGE != 0 ? 1 : 0);
    }


    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        setMenuName("Placed Lucky Blocks (" + currentPage + "/" + maxPage + ")");
        Arrays.stream(content).forEach(e -> Arrays.fill(e, MenuItem.blackPlaceholder().asItem()));

        int minIndex = ((currentPage-1) * TrackCommand.BLOCKS_PER_PAGE);
        final int maxIndex = ((currentPage-1) * TrackCommand.BLOCKS_PER_PAGE) + TrackCommand.BLOCKS_PER_PAGE;

        Iterator<MapLocation3D> it = manager.getPlugin().getAudit().getMap().keySet().stream().iterator();
        // Cycle to the next page.
        int a = 0;
        while(a != minIndex) {
            it.next();
            a++;
        }
        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 8; j++) {
                content[i][j] = MenuItem.whitePlaceholder().asItem();
                if(minIndex >= maxIndex || !it.hasNext()) {
                    continue;
                }
                MapLocation3D key = it.next();
                LuckyBlock lb = manager.getPlugin().getAudit().find(key);
                Validate.notNull(lb, "Lucky block was null on TrackMenu.");
                ItemStack lbItem = new MenuItem().with(lb.getBlockMaterial()).with("&7Lucky Block &6#" + minIndex)
                        .addLine("&8Type: &7" + lb.getInternalName())
                        .addLine("&8Block Luck: &7" + lb.getBlockLuck())
                        .addLine("\n")
                        .addLine("&3" + key.getBukkitWorld().getName() + " &7[&9" + key.getX() + "&7,&9 " + key.getY() + "&7, &9" + key.getZ() + "&7]")
                        .addLine("\n")
                        .addLine("&eLeft-Click to Teleport.")
                        .addLine("&cRight-Click to Remove.").asItem();
                content[i][j] = lbItem;
                minIndex++;
            }
        }

        if(currentPage == 1) {
            content[4][6] = new MenuItem().with(Material.FEATHER).with("&ePrevious Page").addLine("&7You are on the first page.").asItem();
        }else{
            content[4][6] = new MenuItem().with(Material.FEATHER).with("&ePrevious Page").addLine("&7Click to go to page " + (currentPage-1) + ".").asItem();
        }
        if(currentPage == maxPage) {
            content[4][7] = new MenuItem().with(Material.ARROW).with("&eNext Page").addLine("&7You are on the last page.").asItem();
        }else{
            content[4][7] = new MenuItem().with(Material.ARROW).with("&eNext Page").addLine("&7Click to go to page " + (currentPage+1) + ".").asItem();
        }
        content[5][4] = new MenuItem(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        return content;
    }

    private static final byte EXIT_BUTTON = 49;
    private static final byte NEXT_PAGE = 43;
    private static final byte PREVIOUS_PAGE = 42;

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        switch(slot) {
            case EXIT_BUTTON -> {
                view.close();
            }
            case NEXT_PAGE -> {
                if(currentPage==maxPage) {
                    return;
                }
                currentPage++;
                view.reopen();
            }
            case PREVIOUS_PAGE -> {
                if(currentPage==1) {
                    return;
                }
                currentPage--;
                view.reopen();
            }
            default -> {
                if(!(itemStack.hasItemMeta() && Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName())) {
                    return;
                }
                String line = itemStack.getItemMeta().getLore().get(3);
                line = ChatColor.stripColor(line);
                line = StringUtils.replace(line, ",", "");
                line = StringUtils.replace(line, "[", "");
                line = StringUtils.replace(line, "]", "");

                World w = manager.getPlugin().getServer().getWorld(line.split(" ")[0]);
                double x = Double.parseDouble(line.split(" ")[1]);
                double y = Double.parseDouble(line.split(" ")[2]);
                double z = Double.parseDouble(line.split(" ")[3]);
                if(clickType.isLeftClick()) {
                    manager.getPlayer().teleport(new Location(w, x, y+2, z));
                    assert w != null;
                    manager.getPlayer().sendMessage(ChatColor.GREEN + "Teleported you to (" + w.getName() + "), " + x + ", " + (y+2) + ", " + z);
                    view.close();
                }else if(clickType.isRightClick()){
                    manager.getPlugin().getAudit().remove(new Location(w, x, y, z));
                    assert w != null;

                    w.getBlockAt((int)x, (int)y, (int)z).setType(Material.AIR);
                    manager.getPlayer().sendMessage(ChatColor.GREEN + "Removed lucky block at (" + w.getName() + "), " + x + ", " + y + ", " + z);
                    view.reopen();
                }

            }
        }
    }

    @Override
    public MenuType type() {
        return MenuType.TRACK_MENU;
    }
}
