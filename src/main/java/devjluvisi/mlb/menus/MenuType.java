package devjluvisi.mlb.menus;

import devjluvisi.mlb.menus.admin.*;
import devjluvisi.mlb.menus.user.*;

public enum MenuType {

    // Admin Menus
    LIST_LUCKY_BLOCKS(ListMenu.class),
    LIST_DROPS(DropsMenu.class),
    LIST_LOOT(LootMenu.class),
    EDIT_LOOT(EditDropMenu.class),
    CHANGE_RARITY(ChangeRarityMenu.class),
    VIEW_EXCHANGE(ExchangesMenu.class),
    CONFIRM(ConfirmMenu.class),
    // Empty menu
    EMPTY(null),
    // For users only
    USER_LIST_LUCKY_BLOCKS(UserListMenu.class),
    USER_LIST_DROPS(UserListDrops.class),
    USER_LIST_LOOT(UserListLoot.class),
    USER_REDEEM(UserRedeemMenu.class),
    USER_REDEEM_LIST(UserRedeemList.class);

    private final Class<? extends MenuBuilder> classType;

    MenuType(Class<? extends MenuBuilder> classType) {
        this.classType = classType;
    }

    public Class<? extends MenuBuilder> getMenuClass() {
        return classType;
    }

}
