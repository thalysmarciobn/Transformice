package com.transformice.server;

import com.transformice.config.Config;
import com.transformice.helpers.StatsUtils;
import com.transformice.logging.Logging;
import com.transformice.server.users.GameManage;
import org.jboss.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Console {

    private static Thread console;

    public static void init(GameManage gameManage) {
        console = new Thread(() -> {
            System.out.print("> ");
            while (true) {
                try {
                    String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    if (input != null) {
                        String[] command = input.split(" ");
                        switch (command[0]) {
                            case "threadstats":
                                System.out.println("=================================================");
                                System.out.println(StatsUtils.getThreadStats());
                                break;
                            case "gcstats":
                                System.out.println("=================================================");
                                System.out.println(StatsUtils.getGCStats());
                                break;
                            case "memusage":
                                System.out.println("=================================================");
                                System.out.println(StatsUtils.getMemUsage());
                                break;
                            case "stats":
                                System.out.println("=================================================");
                                System.out.println("  Connections: " + gameManage.connections.size());
                                System.out.println("  Players: " + gameManage.playersLogged.size());
                                System.out.println("  Rooms: " + gameManage.rooms.size());
                                System.out.println("  Bulles: " + gameManage.server.bulles.size());
                                System.out.println("  Connections JDBC: " + gameManage.server.database.getActiveConnections() + "/" + Config.database.pool);
                                System.out.println("  Uptime: " + StatsUtils.millisecondsToDate(System.currentTimeMillis() - gameManage.server.start));
                                break;
                            case "gc":
                                System.out.println("=================================================");
                                System.gc();
                                System.out.println(StatsUtils.getGCStats());
                                break;
                            case "freedbpool":
                                System.out.println("=================================================");
                                gameManage.server.database.freeIdleConnections();
                                System.out.println("  Connections JDBC: " + gameManage.server.database.getActiveConnections() + "/" + Config.database.pool);
                                break;
                            case "stopconsole":
                                Console.stop();
                                break;
                            case "commands":
                                System.out.println("=================================================");
                                System.out.println("- threadstats");
                                System.out.println("- gcstats");
                                System.out.println("- memusage");
                                System.out.println("- stats");
                                System.out.println("- gc");
                                System.out.println("- freedbpool");
                                System.out.println("- stopconsole");
                                break;
                            default:
                                Logging.print("Command not found. commands", "info");
                        }
                        System.out.print("> ");
                    }
                } catch (Exception error) {
                    Logging.error(error);
                }
            }
        });
        console.setDaemon(true);
        console.setName("console");
        console.start();
    }

    @SuppressWarnings("deprecation")
    public static void stop() {
        console.stop();
    }
}
