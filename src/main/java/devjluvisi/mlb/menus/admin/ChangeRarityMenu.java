package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Random;

public class ChangeRarityMenu extends MenuBuilder  {

    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;
    private static final float RARITY_STEP_INTERVAL = 5.0F;
    float rarity;

    public ChangeRarityMenu(MenuManager manager) {
        super(manager, "Change Rarity");
    }

    private void init() {
        lb = manager.getMenuData().getLuckyBlock();
        lbDrop = manager.getMenuData().getDrop();
        this.setMenuName("Change Rarity of Drop #" + lb.indexOf(lbDrop));
        if (lb.getDroppableItems().size() <= lb.indexOf(lbDrop)) {
            this.rarity = 50.0F; // default
            return;
        }
        this.rarity = lbDrop.getRarity();
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        init();
        Arrays.fill(content[0], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[1], MenuItem.blackPlaceholder().asItem());
        Arrays.fill(content[2], MenuItem.blackPlaceholder().asItem());

        content[1][1] = new MenuItem().of(MenuItem.SpecialItem.INCREASE_RARITY).asItem();
        content[1][7] = new MenuItem().of(MenuItem.SpecialItem.DECREASE_RARITY).asItem();

        content[1][4] = new MenuItem().with(Material.EXPERIENCE_BOTTLE).with("&eSave Rarity")
                .addLine("&7Update the rarity of this")
                .addLine("&7drop to &6" + rarity + "&7.").asItem();

        return content;
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if(itemStack == null) return;
        if(itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.INCREASE_RARITY).asItem())) {
            if (this.rarity <= RARITY_STEP_INTERVAL) {
                this.rarity = 0.1F;
            } else {
                this.rarity -= RARITY_STEP_INTERVAL;
            }

            view.reopen();
            return;
        }
        if(itemStack.equals(new MenuItem().of(MenuItem.SpecialItem.DECREASE_RARITY).asItem())) {
            if (this.rarity >= 95.0F) {
                this.rarity = 100.0F;
            } else {
                this.rarity += RARITY_STEP_INTERVAL;
            }

            view.reopen();
            return;
        }
        if(itemStack.getType().equals(Material.EXPERIENCE_BOTTLE)) {
            lbDrop.setRarity(rarity);
            manager.open(manager.getPlayer(), MenuType.LIST_LOOT);
        }

    }

    @Override
    public MenuType type() {
        return MenuType.CHANGE_RARITY;
    }
}
