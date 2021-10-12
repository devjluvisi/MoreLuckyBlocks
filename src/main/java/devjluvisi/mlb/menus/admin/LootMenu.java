package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class LootMenu extends MenuBuilder {

    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;

    public LootMenu(MenuManager manager) {
        super(manager, "Viewing Loot ");
    }


    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        setMenuName("Viewing Loot: #" + manager.getMenuData().getLuckyBlock().indexOf(manager.getMenuData().getDrop()));
        this.lb = manager.getMenuData().getLuckyBlock();
        this.lbDrop = manager.getMenuData().getDrop();

        content[0][0] = new MenuItem().with(Material.OAK_SIGN).with("&7Viewing Drop &8-> &3#" + lb.indexOf(lbDrop))
                .addLine("&7You are currently viewing all of the loot")
                .addLine("&7that can drop from this drop.")
                .addLine("&8- &7Rarity: &9" + lbDrop.getRarity())
                .addLine("&8- &7# of Items: &9" + lbDrop.getItems().size())
                .addLine("&8- &7# of Potions: &9" + lbDrop.getPotionEffects().size())
                .addLine("&8- &7# of Commands: &9" + lbDrop.getCommands().size())
                .addLine("\n")
                .addLine("&6Click on the Book & Quill to edit the drop.")
                .asItem();
        content[0][1] = MenuItem.blackPlaceholder().asItem();
        content[1][0] = MenuItem.blackPlaceholder().asItem();
        content[1][1] = MenuItem.blackPlaceholder().asItem();

        final ArrayList<LootProperty> dropList = lbDrop.getLoot();

        int arrIndex = 0;

        // Replace empty slots with loot if it exists.
        for (int i = 0; i < 2; i++) {
            for (int j = 2; j < 9; j++) {
                if (arrIndex == dropList.size()) {
                    break;
                }
                content[i][j] = dropList.get(arrIndex).asItem();
                arrIndex++;
            }
        }

        Arrays.fill(content[2], MenuItem.redPlaceholder().asItem());
        content[2][4] = new MenuItem().of(MenuItem.SpecialItem.EDIT_DROP).asItem();
        content[2][5] = new MenuItem().of(MenuItem.SpecialItem.CHANGE_RARITY).asItem();
        content[2][6] = new MenuItem().of(MenuItem.SpecialItem.COPY_DROP).asItem();
        content[2][7] = new MenuItem().of(MenuItem.SpecialItem.DELETE_DROP).asItem();
        content[2][8] = new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.LIST_LOOT;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack == null) return;
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.EDIT_DROP).asItem())) {
            manager.open(manager.getPlayer(), MenuType.EDIT_LOOT);
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.COPY_DROP).asItem())) {
            lb.getDroppableItems().add(lbDrop.ofUniqueCopy());
            manager.open(manager.getPlayer(), MenuType.LIST_DROPS);
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.DELETE_DROP).asItem())) {
            //manager.open(manager.getPlayer(), MenuType.CONFIRM); // Delete drop
            manager.open(manager.getPlayer(), new ConfirmMenu(manager).returnTo(type()).request(ConfirmMenu.ConfirmAction.DELETE_DROP));
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.CHANGE_RARITY).asItem())) {
            manager.open(manager.getPlayer(), MenuType.CHANGE_RARITY);
            return;
        }
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem())) {
            manager.open(manager.getPlayer(), MenuType.LIST_DROPS);
            return;
        }

    }
}
