package devjluvisi.mlb;

/**
 * Non-User editable constants in the plugin. Values which will not change no
 * matter what.
 *
 * @author jacob
 */
public interface PluginConstants {

    float LUCK_MIN_VALUE = -100.0F;
    float LUCK_MAX_VALUE = 100.0F;

    float DEFAULT_PLAYER_LUCK = 0.0F;
    float DEFAULT_BLOCK_LUCK = 0.0F;

    byte MAX_LUCKY_BLOCK_AMOUNT = 14;
    byte MAX_LOOT_AMOUNT = 14;

    String LuckyIdentifier = "lb-identity";
    String BlockLuckIdentifier = "lb-block-luck";

}
