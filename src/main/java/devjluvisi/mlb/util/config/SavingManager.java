package devjluvisi.mlb.util.config;

import devjluvisi.mlb.MoreLuckyBlocks;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class SavingManager {

    private static final long INITIAL_DELAY = 40L;
    private static final long MINECRAFT_TICK_SPEED = 20L;

    private final AtomicInteger iterationsRun;
    private final AtomicInteger previousWorldDataHash;
    private final AtomicInteger previousBlockDataHash;
    private MoreLuckyBlocks plugin;
    private int taskId;

    public SavingManager(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
        this.previousWorldDataHash = new AtomicInteger(-1);
        this.previousBlockDataHash = new AtomicInteger(-1);
        this.taskId = -1;
        this.iterationsRun = new AtomicInteger(-1);
        if (plugin.getSettingsManager().isAutoSaveEnabled()) {
            previousWorldDataHash.set(plugin.getAudit().hashCode());
            previousBlockDataHash.set(plugin.getLuckyBlocks().getDetailedHash());
            plugin.getServer().getLogger().info("Auto Save is enabled. Changes to lucky blocks (edits) will immediatly save. Saving world data will happen every " + plugin.getSettingsManager().getBlockDataSaveInterval() + " seconds.");
        }
        beginSaveTimer();
    }

    private void beginSaveTimer() {
        iterationsRun.set(0);
        long saveSpeed = plugin.getSettingsManager().getBlockDataSaveInterval();

        if (saveSpeed == 0) {
            return;
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                boolean hasSaved = false;

                // Cancel task if auto save is disabled.
                if (!plugin.getSettingsManager().isAutoSaveEnabled()) {
                    return;
                }

                // Save the blocks.yml file.
                if (!(previousBlockDataHash.get() == -1 || previousBlockDataHash.get() == plugin.getLuckyBlocks().getDetailedHash())) {
                    hasSaved = true;
                    previousBlockDataHash.set(plugin.getLuckyBlocks().getDetailedHash());
                }

                // Save the world-data.yml file.
                if (!(previousWorldDataHash.get() == -1 || previousWorldDataHash.get() == plugin.getAudit().hashCode())) {
                    hasSaved = true;
                    previousWorldDataHash.set(plugin.getAudit().hashCode());
                }
                if (!hasSaved) {
                    return;
                }
                // Files have been saved.
                // Run the following code.
                if (plugin.getSettingsManager().isSavingMessagesEnabled()) {
                    plugin.getServer().getOnlinePlayers().forEach((Player p) -> {
                        if (p.hasPermission("mlb.admin.alert")) {
                            p.sendMessage(ChatColor.GRAY + "[MoreLuckyBlocks: " + ChatColor.ITALIC + "Changes Saved" + ChatColor.RESET + ChatColor.GRAY + "]");
                        }
                    });
                }
                saveResources();
                iterationsRun.getAndIncrement();
            }
        }.runTaskTimerAsynchronously(plugin, INITIAL_DELAY, (saveSpeed * MINECRAFT_TICK_SPEED)).getTaskId();

    }

    public void saveResources() {
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getLuckyBlocks().save();
                plugin.getAudit().writeAll();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void dumpLogger() {
        plugin.getServer().getConsoleSender().sendMessage("SavingManager - Dump");
        plugin.getServer().getConsoleSender().sendMessage("------------------------");
        if (plugin.getSettingsManager().isAutoSaveEnabled()) {
            plugin.getServer().getConsoleSender().sendMessage("Editing Auto save is enabled.");
        }
        if (!isIntervalRunning()) {
            plugin.getServer().getConsoleSender().sendMessage("WORLD DATA SAVING IS DISABLED.");
            return;
        }
        plugin.getServer().getConsoleSender().sendMessage("Task Id: " + taskId);
        plugin.getServer().getConsoleSender().sendMessage("Block Data Hash: " + previousBlockDataHash.get());
        plugin.getServer().getConsoleSender().sendMessage("Audit Hash: " + previousWorldDataHash.get());
        plugin.getServer().getConsoleSender().sendMessage("Iterations: " + iterationsRun.get());
    }

    public boolean isIntervalRunning() {
        return taskId != -1 && plugin.getServer().getScheduler().isCurrentlyRunning(taskId);
    }

}
