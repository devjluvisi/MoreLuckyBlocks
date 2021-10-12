package devjluvisi.mlb.menus;

import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import org.apache.commons.lang.Validate;

public class MenuResource {

    private LuckyBlock lb;
    private LuckyBlockDrop lbDrop;

    public MenuResource() {
    }

    public MenuResource with(LuckyBlock lb) {
        Validate.isTrue(lb != null, "Lucky block passed through MenuResource was null.");
        this.lb = lb;
        return this;
    }

    public MenuResource with(LuckyBlockDrop lbDrop) {
        Validate.isTrue(lbDrop != null, "Lucky block drop passed through MenuResource was null.");
        this.lbDrop = lbDrop;
        return this;
    }

    public LuckyBlock getLuckyBlock() {
        Validate.notNull(lb, "LuckyBlock data for MenuResource is null after request.");
        return lb;
    }

    public LuckyBlockDrop getDrop() {
        Validate.notNull(lbDrop, "Lucky Block Drop data for MenuResource is null upon request.");
        return lbDrop;
    }

}
