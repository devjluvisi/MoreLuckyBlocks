package devjluvisi.mlb.events.luckyblocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.PluginConstants;
import devjluvisi.mlb.api.items.CustomItemMeta;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.util.config.files.messages.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
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
            if (lb.getInternalName().equals(luckId) && lb.getBlockMaterial() == item.getType()) {
                blockPlaced = lb;
            }
        }
        if (Objects.isNull(blockPlaced)) {
            return;
        }

        if (blockPlaced.getDroppableItems().isEmpty()) {
            e.getPlayer().sendMessage(Message.CANT_PLACE.get());
            e.setCancelled(true);
            return;
        }
        blockPlaced.setBlockLuck(specialMeta.getFloat(PluginConstants.BlockLuckIdentifier));
        this.plugin.getAudit().put(e.getBlock().getLocation(), blockPlaced);
        Location loc = e.getBlockPlaced().getLocation();
        e.getPlayer().sendMessage(Message.PLACED_BLOCK.format(blockPlaced.getInternalName(), Objects.requireNonNull(loc.getWorld()).getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), blockPlaced.getBlockLuck()));
    }

}
