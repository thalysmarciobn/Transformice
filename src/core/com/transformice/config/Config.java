package com.transformice.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {

    public static boolean transformice() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config/transformice.conf"));
            for (String key : config.getProperty("tfm.packetKeys").replace(" ", "").split(",")) {
                transformice.packetKeys.add(Integer.valueOf(key));
            }
            for (String key : config.getProperty("tfm.loginKeys").replace(" ", "").split(",")) {
                transformice.loginKeys.add(Integer.valueOf(key));
            }
            transformice.debug = Boolean.parseBoolean(config.getProperty("tfm.debug"));
            transformice.console = Boolean.parseBoolean(config.getProperty("tfm.console"));
        } catch (IOException error) {
            return false;
        }
        return true;
    }

    public static boolean gameserver() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config/network/gameserver.conf"));
            gameserver.host = config.getProperty("server.host");
            gameserver.port = Integer.valueOf(config.getProperty("server.port"));
            for (String port : config.getProperty("server.ports").replace(" ", "").split(",")) {
                gameserver.ports.add(Integer.valueOf(port));
            }
        } catch (IOException error) {
            return false;
        }
        return true;
    }

    public static boolean bulleserver() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config/network/bulleserver.conf"));
            gameserver.host = config.getProperty("server.host");
            for (String port : config.getProperty("server.ports").replace(" ", "").split(",")) {
                gameserver.ports.add(Integer.valueOf(port));
            }
        } catch (IOException error) {
            return false;
        }
        return true;
    }

    public static boolean database() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config/database.conf"));
            database.host = config.getProperty("mysql.host");
            database.user = config.getProperty("mysql.user");
            database.pass = config.getProperty("mysql.pass");
            database.data = config.getProperty("mysql.data");
            database.port = Integer.valueOf(config.getProperty("mysql.port"));
            database.pool = Integer.valueOf(config.getProperty("mysql.pool"));
        } catch (IOException error) {
            return false;
        }
        return true;
    }

    public static boolean netty() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config/network/netty.conf"));
            netty.corePoolSize = Integer.valueOf(config.getProperty("netty.corePoolSize"));
            netty.maxChannelMemorySize = Integer.valueOf(config.getProperty("netty.maxChannelMemorySize"));
            netty.maxTotalMemorySize = Integer.valueOf(config.getProperty("netty.maxTotalMemorySize"));
            netty.keepAliveTime = Integer.valueOf(config.getProperty("netty.keepAliveTime"));
        } catch (IOException error) {
            return false;
        }
        return true;
    }

    public static class transformice {
        public static List<Integer> packetKeys = new ArrayList();
        public static List<Integer> loginKeys = new ArrayList();
        public static boolean debug = false;
        public static boolean console = false;
    }

    public static class netty {
        public static int corePoolSize = 0;
        public static int maxChannelMemorySize = 0;
        public static int maxTotalMemorySize = 0;
        public static int keepAliveTime = 0;
    }

    public static class gameserver {
        public static String host;
        public static List<Integer> ports = new ArrayList();
        public static int port = 0;
    }

    public static class bulleserver {
        public static String host = "";
        public static String rmi = "";
        public static List<Integer> ports = new ArrayList();
    }

    public static class database {
        public static String host = "";
        public static String user = "";
        public static String pass = "";
        public static String data = "";
        public static int port = 0;
        public static int pool = 0;
    }
}
