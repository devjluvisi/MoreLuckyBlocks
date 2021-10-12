package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.cmds.CommandResult;
import devjluvisi.mlb.cmds.ResultType;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.util.Range;
import org.bukkit.command.CommandSender;

public record TestCommand(MoreLuckyBlocks plugin) implements SubCommand {

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Test Command";
    }

    @Override
    public String getSyntax() {
        return "/mlb test";
    }

    @Override
    public String getPermission() {
        return "mlb.admin";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(1, 1);
    }

    @Override
    public CommandResult perform(CommandSender sender, String[] args) {
        sender.sendMessage("Running Test. Check logger");
        this.plugin.getServerDropStructure().save();
        this.plugin.getAudit().dumpLogger();
        this.plugin.getLuckyBlocks().dumpLogger();
        return new CommandResult(ResultType.PASSED);
    }

}
