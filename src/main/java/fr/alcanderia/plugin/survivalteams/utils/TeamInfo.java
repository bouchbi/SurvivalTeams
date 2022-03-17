package fr.alcanderia.plugin.survivalteams.utils;

public enum TeamInfo {
    NAME("name"),
    PLAYERS("players"),
    LEADER("leader"),
    ECONOMY("eco"),
    WARP("warp"),
    WARP_VISIBILITY("warpVisibility"),
    COLOR("color");

    public final String name;

    TeamInfo(String name) {
        this.name = name;
    }
}
