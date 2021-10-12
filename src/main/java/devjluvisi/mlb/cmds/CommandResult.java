package devjluvisi.mlb.cmds;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;

public final class CommandResult {

    private final ResultType commandResultType;
    private final String badArg;

    public CommandResult(ResultType result) {
        this.commandResultType = result;
        this.badArg = StringUtils.EMPTY;
    }

    public CommandResult(ResultType result, String badArg) {
        this.commandResultType = result;
        this.badArg = badArg;
    }

    public boolean hasDetail() {
        return StringUtils.isNotEmpty(badArg);
    }

    public ResultType getResult() {
        return this.commandResultType;
    }

    public String getBadArg() {
        return this.badArg;
    }

    @Override
    public String toString() {
        return MessageFormat.format("commandResult [type={0}, arg={1}]", commandResultType.name(), badArg);
    }
}
