package com.zpedroo.headholograms.data;

import com.zpedroo.headholograms.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import java.sql.*;
import java.util.*;

public class SQLiteConnector {

    Connection connection;
    private Logger logger;

    private String TABLE;

    public SQLiteConnector(String TABLE) {
        this.TABLE = TABLE;
        connection = null;
        (logger = Main.get().getLogger()).info("Connecting to the SQLite database...");
        long ms = System.currentTimeMillis();
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + loadFile("/players.db").getAbsolutePath();
            connection = DriverManager.getConnection(url);
            logger.info("Connection to SQLite has been established in " + (System.currentTimeMillis() - ms) + "ms.");
            checkTable();
        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("Failed to connect to the SQLite database:");
            ex.printStackTrace();
        }
    }

    private void checkTable() throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='" + TABLE + "';")) {
            Long l = null;
            if (!resultSet.next()) {
                logger.info("Database table does not exist, creating...");
                l = System.currentTimeMillis();
            }
            statement.execute("CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                    "`location` VARCHAR(255) NOT NULL," +
                    "`uuid` VARCHAR(255) NOT NULL," +
                    "`lines` VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (`location`));");
            if (l != null) {
                logger.info("Created table in " + (System.currentTimeMillis() - l) + "ms.");
            }
        }
    }

    public void loadHolograms() {
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet result;
        final String query = "SELECT * FROM `" + TABLE + "`;";
        try {
            connection = this.connection;
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                String location = result.getString("location");
                String uuid = result.getString("uuid");
                String lines = result.getString("lines");
                new HologramData(deserializeLocation(location), UUID.fromString(uuid), lines, null).load();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHologram(HologramData hologramData) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT OR REPLACE INTO '" + TABLE + "' VALUES ('" +
                    serializeLocation(hologramData.getLocation()) + "', '" +
                    hologramData.getUUID().toString() + "', '" +
                    hologramData.getLines()  + "');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHologram(HologramData hologramData) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM `" + TABLE + "` WHERE `uuid`='" + hologramData.getUUID() + "';");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    private String serializeLocation(Location location) {
        if (location == null) return null;

        StringBuilder str = new StringBuilder(4);
        str.append(location.getWorld().getName()); // Add our world name.
        str.append("#" + location.getX()); // Add the 'x' point.
        str.append("#" + location.getY()); // Add the 'y' point.
        str.append("#" + location.getZ()); // Add the 'z' point.

        return str.toString();
    }

    public Location deserializeLocation(String location) {
        if (location == null) return null;

        String[] locationSplit = location.split("#");
        double x = Double.parseDouble(locationSplit[1]);
        double y = Double.parseDouble(locationSplit[2]);
        double z = Double.parseDouble(locationSplit[3]);

        return new Location(Bukkit.getWorld(String.valueOf(locationSplit[0])), x, y, z);
    }

    private File loadFile(String filen) {
        File file = new File(Main.get().getDataFolder(), filen);
        if (!file.exists()) {
            try {
                if (file.getParent() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
