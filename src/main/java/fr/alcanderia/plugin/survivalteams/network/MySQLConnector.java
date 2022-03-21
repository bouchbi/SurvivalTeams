package fr.alcanderia.plugin.survivalteams.network;

import fr.alcanderia.plugin.survivalteams.Survivalteams;
import fr.alcanderia.plugin.survivalteams.utils.TeamInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class MySQLConnector {

    private static final Logger logger = Survivalteams.getInstance().getLogger();
    static final String DB_url = "jdbc:mysql://" + Survivalteams.getConfiguration().getString("sqlCredentials.host") + ":" + Survivalteams.getConfiguration().getString("sqlCredentials.port") + "/" + Survivalteams.getConfiguration().getString("sqlCredentials.dbName") + "?serverTimezone=UTC";
    static final String DB_user = Survivalteams.getConfiguration().getString("sqlCredentials.user");
    static final String DB_password = Survivalteams.getConfiguration().getString("sqlCredentials.password");
    static String teamsTabName = Survivalteams.getConfiguration().getString("sqlCredentials.dbTablesPrefix") + "_" + "teams";
    private static Connection con;

    public static String getInfo(String teamName, TeamInfo info) {
        reopenIfClosed();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT " + TeamInfo.NAME.name + ", " + info.name + " FROM " + teamsTabName);

            try {
                ResultSet rs = ps.executeQuery();

                try {
                    String resInfo = null;

                    String resTeamName;
                    while (rs.next()) {
                        resTeamName = rs.getString(TeamInfo.NAME.name);
                        if (!Objects.equals(resTeamName, teamName))
                            continue;

                        resInfo = rs.getString(info.name);
                    }

                    return resInfo;
                } catch (SQLException e) {
                    logger.warning("Cannot get teamInfo for team " + teamName);
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                logger.warning("cannot execute query");
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        logger.warning("cannot close statement");
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            logger.warning("Unable to prepare statement");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void updateInfo(String teamName, TeamInfo info, String update) {
        reopenIfClosed();

        try {
            PreparedStatement ps = con.prepareStatement("UPDATE " + teamsTabName + " SET " + info.name + " = ? WHERE " + TeamInfo.NAME.name + " = ?");

            try {
                ps.setString(1, update);
                ps.setString(2, teamName);
                ps.executeUpdate();
            } catch (SQLException e) {
                logger.warning("cannot execute update");
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        logger.warning("cannot close statement");
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            logger.warning("Unable to prepare statement");
            e.printStackTrace();
        }
    }

    public static String getPlayerTeam(String player) {
        reopenIfClosed();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT " + TeamInfo.NAME.name + ", " + TeamInfo.PLAYERS.name + " FROM " + teamsTabName);

            try {
                ResultSet rs = ps.executeQuery();

                try {
                    while (rs.next()) {
                        String resPlayers = rs.getString(TeamInfo.PLAYERS.name);
                        if (resPlayers == null)
                            continue;

                        if (resPlayers.contains(player))
                            return rs.getString(TeamInfo.NAME.name);
                    }

                    return null;
                } catch (SQLException e) {
                    logger.warning("Cannot get teamInfo from database");
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                logger.warning("cannot execute query");
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        logger.warning("cannot close statement");
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            logger.warning("Unable to prepare statement");
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getAllTeams() {
        reopenIfClosed();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT " + TeamInfo.NAME.name + " FROM " + teamsTabName);

            try {
                ResultSet rs = ps.executeQuery();

                try {
                    List<String> resteams = new ArrayList<>();

                    while (rs.next())
                        resteams.add(rs.getString(TeamInfo.NAME.name));

                    return resteams;

                } catch (SQLException e) {
                    logger.warning("Cannot get team names from database");
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                logger.warning("cannot execute query");
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        logger.warning("cannot close statement");
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            logger.warning("Unable to prepare statement");
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getTeamTop() {
        reopenIfClosed();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT " + TeamInfo.NAME.name + ", " + TeamInfo.ECONOMY.name + " FROM " + teamsTabName);

            try {
                ResultSet rs = ps.executeQuery();

                try {
                    HashMap<Integer, String> teams = new HashMap<>();

                    while (rs.next())
                        teams.put(rs.getInt(TeamInfo.ECONOMY.name), rs.getString(TeamInfo.NAME.name));

                    List<Integer> teamEco = new ArrayList<>(teams.keySet());
                    Collections.sort(teamEco);

                    List<String> teamSorted = new ArrayList<>();
                    teamEco.forEach(tEco -> teamSorted.add(teams.get(tEco)));

                    return teamSorted;
                } catch (SQLException e) {
                    logger.warning("Cannot get team names and eco from database");
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                logger.warning("cannot execute query");
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        logger.warning("cannot close statement");
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            logger.warning("Unable to prepare statement");
            e.printStackTrace();
        }
        return null;
    }

    public static void createTeam(String teamName, Player player) {
        reopenIfClosed();

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO " + teamsTabName + "(" + TeamInfo.NAME.name + "," + TeamInfo.PLAYERS.name + "," + TeamInfo.LEADER.name + "," + TeamInfo.ECONOMY.name + "," + TeamInfo.WARP.name + "," + TeamInfo.COLOR.name + "," + TeamInfo.WARP_VISIBILITY.name + ")VALUES(?, ?, ?, ?, ?, ?, ?)");

            try {
                ps.setString(1, teamName);
                ps.setString(2, player.getName());
                ps.setString(3, player.getName());
                ps.setInt(4, 0);
                ps.setString(5, null);
                ps.setString(6, ChatColor.GRAY.toString());
                ps.setString(7, "0");
                ps.executeUpdate();
            } catch (SQLException e) {
                logger.warning("cannot execute update");
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        logger.warning("cannot close statement");
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            logger.warning("Unable to prepare statement");
            e.printStackTrace();
        }
    }

    public static void removeTeam(String teamName) {
        reopenIfClosed();

        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM " + teamsTabName + " WHERE " + TeamInfo.NAME.name + " = ?");

            try {
                ps.setString(1, teamName);
                ps.executeUpdate();

            } catch (SQLException e) {
                logger.warning("cannot execute update");
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        logger.warning("cannot close statement");
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            logger.warning("Unable to prepare statement");
            e.printStackTrace();
        }
    }

    public static void createTable(String tabName) {
        reopenIfClosed();

        if (tabName != null) {
            try {
                Statement stmt = con.createStatement();

                try {
                    String sql = "CREATE TABLE " + tabName + "(" + TeamInfo.NAME.name + " VARCHAR(24) not NULL, " + TeamInfo.PLAYERS.name + " VARCHAR(1000) DEFAULT NULL, " + TeamInfo.LEADER.name + " VARCHAR(24) DEFAULT NULL, " + TeamInfo.ECONOMY.name + " INTEGER DEFAULT NULL, " + TeamInfo.WARP.name + " VARCHAR(1000) DEFAULT NULL, " + TeamInfo.COLOR.name + " VARCHAR(4) DEFAULT NULL, " + TeamInfo.WARP_VISIBILITY.name + " BOOLEAN NOT NULL DEFAULT FALSE" + ")";
                    stmt.executeUpdate(sql);
                    logger.info("Successfully created " + tabName + " table in given database");
                } catch (SQLException e) {
                    logger.warning("error creating table " + tabName + " in database");
                    e.printStackTrace();
                } finally {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException e) {
                            logger.warning("cannot close statement");
                            e.printStackTrace();
                        }
                    }

                }
            } catch (SQLException e) {
                logger.warning("Error creating table in given database");
                e.printStackTrace();
            }
        } else {
            logger.warning("Cannot create table in given database, check for existing fields 'dbInvTableName' and 'sbECTableName' in config");
        }
    }

    public static void reopenIfClosed() {
        try {
            con.getNetworkTimeout();
        } catch (SQLException e) {
            logger.warning("Connexion timed out, plugin will attempt to close and reopen it");
            closeConnexion();
            openConnexion();
        }
    }

    public static void initConnexion() {
        openConnexion();

        try {
            if (!checkTableExistence(teamsTabName)) {
                logger.info("Table " + teamsTabName + " not found in database, will attempt to create one");
                createTable(teamsTabName);
            }
        } catch (SQLException e) {
            logger.warning("Unable to check table existence in database");
            e.printStackTrace();
        }
    }

    public static void openConnexion() {
        try {
            con = DriverManager.getConnection(DB_url, DB_user, DB_password);
            logger.info("Successfully connected to the given database");
        } catch (SQLException e) {
            logger.warning("Error connecting to database, check the given information in the config");
            e.printStackTrace();
        }
    }

    public static void closeConnexion() {
        try {
            if (con != null) {
                con.close();
                logger.info("Successfully disconnected from database");
            }
        } catch (SQLException e) {
            logger.warning("Unable to close connexion with database");
            e.printStackTrace();
        }

    }

    private static boolean checkTableExistence(String tableName) throws SQLException {
        DatabaseMetaData dbmt = con.getMetaData();
        ResultSet res = dbmt.getTables((String) null, (String) null, tableName, new String[]{"TABLE"});
        return res.next();
    }
}
