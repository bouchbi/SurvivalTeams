package fr.alcanderia.plugin.survivalteams.network;

import fr.alcanderia.plugin.survivalteams.ConfigHandler;
import fr.alcanderia.plugin.survivalteams.Survivalteams;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MySQLConnector {

    private static final Logger logger = Survivalteams.getInstance().getLogger();
    static final String DB_url = "jdbc:mysql://" + Survivalteams.getConfiguration().getString("sqlCredentials.host") + ":" + Survivalteams.getConfiguration().getString("sqlCredentials.port") + "/" + Survivalteams.getConfiguration().getString("sqlCredentials.dbName") + "?serverTimezone=UTC";
    static final String DB_user = Survivalteams.getConfiguration().getString("sqlCredentials.user");
    static final String DB_password = Survivalteams.getConfiguration().getString("sqlCredentials.password");
    static String tabName = Survivalteams.getConfiguration().getString("sqlCredentials.dbTablesPrefix") + "_" + "teams";
    private static Connection con;

    public static void createTable(String tabName) {
        if (tabName != null) {
            try {
                Statement stmt = con.createStatement();

                try {
                    String sql = "CREATE TABLE " + tabName + "(name VARCHAR(24) not NULL, players VARCHAR(1000) DEFAULT NULL, eco INTEGER DEFAULT NULL, warp VARCHAR(1000) DEFAULT NULL, rank INTEGER DEFAULT NULL)";
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
            if (con.isClosed()) {
                logger.info("connexion closed, trying to reopen to execute task");
                openConnexion();
            }
        } catch (SQLException e) {
            logger.warning("cannot check connexion status");
            e.printStackTrace();
        }
    }

    public static void initConnexion() {
        openConnexion();

        try {
            if (!checkTableExistence(tabName)) {
                logger.info("Table " + tabName + " not found in database, will attempt to create one");
                createTable(tabName);
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
