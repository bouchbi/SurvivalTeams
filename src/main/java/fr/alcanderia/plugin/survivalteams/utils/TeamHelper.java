package fr.alcanderia.plugin.survivalteams.utils;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.network.MySQLConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TeamHelper {

    public static List<String> getAllTeams() {
        return MySQLConnector.getAllTeams();
    }

    public static List<String> getTeamPlayers(String teamName) {
        String res = MySQLConnector.getInfo(teamName, TeamInfo.PLAYERS);

        if (res != null) {
            return new ArrayList<>(Arrays.asList(res.split(";")));
        } else
            return null;
    }

    public static String getPlayerTeam(String player) {
        return MySQLConnector.getPlayerTeam(player);
    }

    public static void addPlayer(String teamName, String playerName) {
        List<String> playersOld = getTeamPlayers(teamName);
        if (!playersOld.contains(playerName)) {
            playersOld.add(playerName);
            MySQLConnector.updateInfo(teamName, TeamInfo.PLAYERS, String.join(";", playersOld));
            Survivalteams.getInstance().getLogger().info("Successfully added " + playerName + " to team " + teamName);
        }
    }

    public static void removePlayer(String teamName, String playerName) {
        List<String> playersOld = getTeamPlayers(teamName);
        if (playersOld != null && playersOld.contains(playerName)) {
            playersOld.remove(playerName);
            MySQLConnector.updateInfo(teamName, TeamInfo.PLAYERS, String.join(";", playersOld));
            Survivalteams.getInstance().getLogger().info("Successfully removed " + playerName + " from team " + teamName);
        }
    }

    public static int getTeamEconomy(String teamName) {
        String res = MySQLConnector.getInfo(teamName, TeamInfo.ECONOMY);

        if (res != null)
            return Integer.parseInt(res);
        else
            return 0;
    }

    public static String getTeamLeader(String teamName) {
        return MySQLConnector.getInfo(teamName, TeamInfo.LEADER);
    }

    public static int[] getTeamWarpLocation(String teamName) {
        String res = MySQLConnector.getInfo(teamName, TeamInfo.WARP);

        if (res != null) {
            String[] locs = res.split(";");

            return new int[]{Integer.parseInt(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2])};
        } else
            return null;
    }

    public static boolean checkTeamExistence(String teamName) {
        return Objects.requireNonNull(MySQLConnector.getAllTeams()).contains(teamName);
    }
}
