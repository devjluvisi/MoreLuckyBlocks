package devjluvisi.mlb.util.config.files;

public enum Placeholder {
    PLAYER_NAME("%player%"),
    TARGET_NAME("%target%"),
    LUCK_VALUE("%luck%"),
    LUCKY_BLOCK_NAME("%luckyblock%");

    private final String text;

    Placeholder(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
