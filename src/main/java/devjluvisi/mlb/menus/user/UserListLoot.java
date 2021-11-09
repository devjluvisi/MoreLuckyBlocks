package devjluvisi.mlb.menus.user;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.blocks.drops.LootProperty;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import devjluvisi.mlb.util.config.files.messages.Message;
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
        setMenuName(Message.VIEWING_LOOT_GUI_TITLE.format(manager.getMenuData().getLuckyBlock().indexOf(manager.getMenuData().getDrop())));
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
                .with(Message.DROP_TITLE.get() + manager.getMenuData().getLuckyBlock().indexOf(manager.getMenuData().getDrop()))
                .addLine(Message.M10.format(manager.getMenuData().getLuckyBlock().indexOf(manager.getMenuData().getDrop())))
                .addLine(Message.LABEL_RARITY.format(lbDrop.getRarity()))
                .addLine(Message.LABEL_TOTAL_LOOT.format(lbDrop.getLoot().size()))
                .addLine(Message.LABEL_ITEMS.format(lbDrop.getItems().size()))
                .addLine(Message.LABEL_COMMANDS.format(lbDrop.getCommands().size()))
                .addLine(Message.LABEL_POTIONS.format(lbDrop.getPotionEffects().size()))
                .addLine(Message.LABEL_STRUCTURE.format(lbDrop.hasStructure() ? "Yes" : "No"))
                .asItem();
        dropInfo.addUnsafeEnchantment(Enchantment.THORNS, 1);
        ItemMeta meta = dropInfo.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        dropInfo.setItemMeta(meta);

        content[3][3] = dropInfo;
        content[3][4] = new MenuItem().of(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        content[3][5] = new MenuItem().with(Material.PAPER).with(Message.STRUCTURES_TITLE.get())
                .addAllLine(Util.descriptionToLore(Message.M11.get()).toArray(String[]::new)).asItem();

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
