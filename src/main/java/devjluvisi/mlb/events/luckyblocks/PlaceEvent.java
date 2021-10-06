package devjluvisi.mlb.events.luckyblocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.items.CustomItemMeta;
import devjluvisi.mlb.blocks.LuckyBlock;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public record PlaceEvent(MoreLuckyBlocks plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeLuckyBlock(BlockPlaceEvent e) {
        final ItemStack item = e.getItemInHand();
        final ItemMeta meta = item.getItemMeta();
        final CustomItemMeta specialMeta = this.plugin.getMetaFactory().createCustomMeta(meta);
        if (!specialMeta.containsKey(PluginConstants.LuckyIdentifier)) {
            return;
        }
        LuckyBlock blockPlaced = null;
        final String luckId = specialMeta.getString(PluginConstants.LuckyIdentifier);

        for (final LuckyBlock lb : this.plugin.getLuckyBlocks()) {
            if (lb.getInternalName().equals(luckId)) {
                blockPlaced = lb;
            }
        }
        if (Objects.isNull(blockPlaced)) {
            return;
        }
        if (blockPlaced.getDroppableItems().size() == 0) {
            e.getPlayer().sendMessage(
                    ChatColor.RED + "This lucky block cannot be placed because there have been no drops set for it.");
            e.setCancelled(true);
            return;
        }
        blockPlaced.setBlockLuck(specialMeta.getFloat(PluginConstants.BlockLuckIdentifier));
        this.plugin.getAudit().put(e.getBlock().getLocation(), blockPlaced);
        e.getPlayer().sendMessage("You placed a lucky block (" + blockPlaced.getInternalName() + ").");
    }

}
