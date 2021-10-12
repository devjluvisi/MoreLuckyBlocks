package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class UserListLoot extends MenuBuilder {

    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;

    public UserListLoot(MenuManager manager) {
        super(manager, "Viewing Loot ", PageType.CHEST_PLUS);
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        setMenuName("Viewing Loot: #" + manager.getMenuData().getLuckyBlock().indexOf(manager.getMenuData().getDrop()));
        this.lb = manager.getMenuData().getLuckyBlock();
        this.lbDrop = manager.getMenuData().getDrop();
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[3], MenuItem.blackPlaceholder().asItem());
        content[1][0] = content[2][0] = content[1][8] = content[2][8] = MenuItem.blackPlaceholder().asItem();
        final ArrayList<LootProperty> dropList = lbDrop.getLoot();

        int arrIndex = 0;

        // Replace empty slots with loot if it exists.
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 8; j++) {
                if (arrIndex == dropList.size()) {
                    break;
                }
                content[i][j] = dropList.get(arrIndex).asItem();
                arrIndex++;
            }
        }

        ItemStack dropInfo = new MenuItem()
                .with(Material.BOOK)
                .with("&7Drop: " + manager.getMenuData().getLuckyBlock().indexOf(manager.getMenuData().getDrop()))
                .addLine("&7You are currently viewing drop &8#&e" + manager.getMenuData().getLuckyBlock().indexOf(manager.getMenuData().getDrop()))
                .addLine("&8- &7Rarity: &d" + lbDrop.getRarity())
                .addLine("&8- &7Total Drops: &d" + lbDrop.getLoot().size())
                .addLine("&8- &7Items: &d" + lbDrop.getItems().size())
                .addLine("&8- &7Commands: &d" + lbDrop.getCommands().size())
                .addLine("&8- &7Potions: &d" + lbDrop.getPotionEffects().size())
                .addLine("&8- &7Has Structure: &d" + lbDrop.hasStructure())
                .asItem();
        dropInfo.addUnsafeEnchantment(Enchantment.THORNS, 1);
        ItemMeta meta = dropInfo.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        dropInfo.setItemMeta(meta);

        content[3][3] = dropInfo;
        content[3][4] = new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        content[3][5] = new MenuItem().with(Material.PAPER).with("&e&lStructures")
                .addLine("&7Structures are blocks/mobs which are")
                .addLine("&7created when you break a lucky block.")
                .addLine("&7Structures are &cnot&7 shown here.").asItem();

        return content;
    }

    @Override
    public MenuType type() {
        return MenuType.USER_LIST_LOOT;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if (itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem())) {
            manager.open(manager.getPlayer(), MenuType.USER_LIST_DROPS);
        }
    }
}
