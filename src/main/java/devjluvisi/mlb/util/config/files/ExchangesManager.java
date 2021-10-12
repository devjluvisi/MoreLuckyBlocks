package devjluvisi.mlb.util.config.files;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.util.config.ConfigManager;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: needs mlb.lb.exchange.[lb_name] to use exchanges.
 */
public class ExchangesManager extends ConfigManager {

    public ExchangesManager(MoreLuckyBlocks plugin) {
        super(plugin, "exchanges.yml");
    }

    /**
     * Rename a lucky block that is saved in the config.
     * Also removes the previous exchange from the exchanges resource file.
     * @param previousName Previous internal name of the lucky block.
     * @param newName New lucky block to rename to.
     */
    public void rename(String previousName, String newName) {
        if (!hasExchange(previousName)) return;
        addExchange(newName, getItems(previousName));
        removeExchange(previousName);
    }

    /**
     *
     * @param name Internal name of a lucky block.
     * @return If the lucky block has an exchange.
     */
    public boolean hasExchange(String name) {
        return getConfig().get("exchanges.%s" .formatted(name)) != null;
    }

    /**
     * Add a new exchange to the configuration.
     * @param internalName lucky block to add an exchange to.
     * @param items The items to make the exchange.
     */
    public void addExchange(String internalName, List<ItemStack> items) {
        int index = 0;
        for (ItemStack i : items) {
            setValue("exchanges.%s.%d" .formatted(internalName, index), i);
            index++;
        }
    }

    /**
     * Get all of the items required for a lucky block exchange.
     * @param internalName Name of the lucky block.
     * @return List of items.
     */
    public List<ItemStack> getItems(String internalName) {
        List<ItemStack> itemStackList = new ArrayList<>();
        int index = 0;
        while (getConfig().get("exchanges.%s.%d" .formatted(internalName, index)) != null) {
            itemStackList.add(getConfig().getItemStack("exchanges.%s.%d" .formatted(internalName, index)));
            index++;
        }
        return itemStackList;
    }

    /**
     * @param internalName Removes a lucky block exchange from config.
     */
    public void removeExchange(String internalName) {
        if (!hasExchange(internalName)) return;
        setValue("exchanges.%s" .formatted(internalName), null);
    }

}
