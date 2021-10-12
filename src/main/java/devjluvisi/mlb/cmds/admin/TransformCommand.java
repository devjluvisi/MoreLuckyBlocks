package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.luckyblocks.MapLocation3D;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

/**
 * Transform all blocks of a certain type into a luckyblock within a radius
 */
public class TransformCommand implements SubCommand {

    private MoreLuckyBlocks plugin;

    public TransformCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "transform";
    }

    @Override
    public String getDescription() {
        return "Converts all blocks of a specified lucky block material to a lucky block within a specific radius.";
    }

    @Override
    public String getSyntax() {
        return "/mlb transform <lb-name> <radius>\n/mlb transform <lb-name> <world> <x> <y> <z> <radius>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.transform";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(2, 7);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        LuckyBlock lb = null;
        Location orgin = new Location(null, 0, 0, 0);
        int radius = 0;

        if (args.length == 7) {
            try {
                lb = plugin.getLuckyBlocks().get(args[1]);
                orgin.setWorld(plugin.getServer().getWorld(args[2]));
                orgin.setX(Util.toNumber(args[3]));
                orgin.setY(Util.toNumber(args[4]));
                orgin.setZ(Util.toNumber(args[5]));
                radius = Integer.parseInt(args[6]);
                Validate.notNull(lb);
                Validate.notNull(orgin.getWorld());
                Validate.notNull(orgin);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Failed." + ChatColor.RED + " There are or more errors in your command. Make sure all numbers are valid and the specified world exists.");
                return new CommandResult(ResultType.BAD_ARGUMENT_TYPE, args[1]);
            }
        } else if (args.length == 3) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You cannot execute this form of the command as a non-player. You must use the /mlb transform <world> <x> <y> <z> <radius> version.");
                return new CommandResult(ResultType.PASSED);
            }

            if (!Util.isNumber(args[2])) {
                return new CommandResult(ResultType.BAD_ARGUMENT_TYPE, args[2]);
            }
            if (!plugin.getLuckyBlocks().contains(args[1])) {
                return new CommandResult(ResultType.INVALID_LUCKY_BLOCK, args[1]);
            }
            radius = Integer.parseInt(args[2]);
            lb = plugin.getLuckyBlocks().get(args[1]);
            orgin = ((Player) sender).getLocation();
        } else {
            return new CommandResult(ResultType.BAD_USAGE);
        }

        sender.sendMessage(ChatColor.BLUE + "Replacing regular blocks with lucky block: " + ChatColor.GOLD + lb.getInternalName());

        int fromX = orgin.getBlockX() - radius;
        int fromY = orgin.getBlockY() - radius;
        int fromZ = orgin.getBlockZ() - radius;
        int toX = orgin.getBlockX() + radius;
        int toY = orgin.getBlockY() + radius;
        int toZ = orgin.getBlockZ() + radius;
        fromY = fromY < 0 ? 0 : Math.min(fromY, 255);
        toY = toY < 0 ? 0 : Math.min(toY, 255);
        sender.sendMessage(ChatColor.GRAY + "X -> " + fromX + " to " + toX);
        sender.sendMessage(ChatColor.GRAY + "Y -> " + fromY + " to " + toY);
        sender.sendMessage(ChatColor.GRAY + "Z -> " + fromZ + " to " + toZ);

        final long searchBlocks = (long) Math.pow((radius * 2), 3);
        sender.sendMessage(ChatColor.DARK_GRAY + "Request to search " + searchBlocks + " blocks.");
        if (searchBlocks > 50000) {
            sender.sendMessage(ChatColor.RED + "[WARNING] Blocks requested to search exceeds 50,000. It may take time to replace the blocks requested.");
        }

        final long time = System.currentTimeMillis();

        int finalFromY = fromY;
        int finalToY = toY;
        Location finalOrgin = orgin;
        LuckyBlock finalLb = lb;
        final int[] blocksReplaced = { 0 };

        new BukkitRunnable() {
            final long thresholdMessage = searchBlocks / 16;
            long bCounter = 0;
            boolean hasShown = false;

            @Override
            public void run() {
                for (int x = fromX; x < toX; x++) {
                    for (int y = finalFromY; y < finalToY; y++) {
                        for (int z = fromZ; z < toZ; z++) {
                            bCounter++;
                            if (bCounter - 5000D > 0 && !hasShown) {
                                // Calculate estimated time based on the time it took to place "X" blocks.
                                int seconds = (int) (((double) (System.currentTimeMillis() - time) / 1000D) * ((double) searchBlocks / 5000D));
                                sender.sendMessage(ChatColor.BLUE + "Transform: " + ChatColor.AQUA + "Estimated to take " + seconds + " seconds.");
                                hasShown = true;
                            }
                            final MapLocation3D l = new MapLocation3D();
                            l.setWorld(Objects.requireNonNull(finalOrgin.getWorld()).getUID());
                            l.setX(x);
                            l.setY(y);
                            l.setZ(z);
                            if (l.getBlock().getType() == finalLb.getBlockMaterial()) {
                                plugin.getAudit().put(l, finalLb);
                                blocksReplaced[0]++;
                            }
                            if (hasShown && bCounter % thresholdMessage == 0 && bCounter != 0) {
                                sender.sendMessage(ChatColor.GREEN + "Searched over " + ChatColor.GOLD + bCounter + ChatColor.GREEN + " blocks. (" + ChatColor.GOLD + blocksReplaced[0] + ChatColor.GREEN + " replaced)");
                            }
                        }
                    }
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.UNDERLINE + ChatColor.BOLD + "Completed.");
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Task completed in: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.GREEN + "Edited " + ChatColor.YELLOW + blocksReplaced[0] + ChatColor.GREEN + " blocks and turned them into lucky blocks!");
                this.cancel();
            }
        }.runTaskAsynchronously(plugin);

        return new CommandResult(ResultType.PASSED);
    }
}