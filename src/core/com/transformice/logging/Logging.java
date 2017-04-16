package com.transformice.logging;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {
    private static Logger print = Logger.getLogger("logger");

    public static void print(String message, String type) {
        switch (type) {
            case "info":
            default:
                print.info(message);
                break;
        }
    }

    public static void error(Exception error) {
        try {
            if (error != null && (error.getMessage() != null ? !error.getMessage().toLowerCase().contains("address already in use") : true)) {
                System.err.println(new StringBuilder("[").append(new SimpleDateFormat("HH:mm:ss").format(new Date())).append("] ").append(error.getMessage()).toString());
                File file = new File("./logs/errors.log");
                FileWriter fw = new FileWriter(file, true);
                try (PrintWriter pw = new PrintWriter(fw)) {
                    pw.println("============================================================\n- Time: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()));
                    pw.println("- Error: ");
                    error.printStackTrace(pw);
                }
            }
        } catch (IOException Error) {
            Error.printStackTrace(System.err);
        }
    }

    public static void packet(String error) {
        try {
            File file = new File("./logs/packet.log");
            FileWriter fw = new FileWriter(file, true);
            try (PrintWriter pw = new PrintWriter(fw)) {
                pw.println("============================================================\n- Time: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date()));
                pw.println(error + "\n");
            }
        } catch (IOException Error) {
            Error.printStackTrace(System.err);
        }
    }
}