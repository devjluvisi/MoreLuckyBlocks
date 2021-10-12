package devjluvisi.mlb.cmds;

/**
 * A result from a command.
 */
public enum ResultType {
    /** The command completed successfully or resulted in an error not present in the execution result enum. */
    PASSED,
    /** Player specified is not found. */
    INVALID_PLAYER,
    /** Player does not have permission to execute the specific command specified. */
    INVALID_PERMISSION,
    /** Argument specified is not of the correct type (ex. str to int). */
    BAD_ARGUMENT_TYPE,
    /** Usage of the command is not valid. */
    BAD_USAGE,
    /** Lucky block specified does not exist. */
    INVALID_LUCKY_BLOCK,
    /** The material specified is not valid. */
    INVALID_MATERIAL,
    /** Request to execute command cancelled for any reason. */
    BAD_REQUEST;
}
