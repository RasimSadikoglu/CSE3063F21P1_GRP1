package Util;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class Logger {

    static private final String PATH = DataIOHandler.currentPath + "src/log.txt";
    static private boolean write = false;

    static private TreeMap<String, Integer> summary;

    static public void setup() {
        summary = new TreeMap<String, Integer>();

        new File(PATH).delete();

        System.out.println("---------- START ----------");
        DataIOHandler.writeFile(PATH, "---------- START ----------\n", true);
        write = true;
    }

    static public void addNewLog(String action, String log) {
        if (!write) return;
        
        String formattedLog = formatLog(action, log);

        System.out.println(formattedLog);
        DataIOHandler.writeFile(PATH, formattedLog + "\n", true);
    }

    static public void addNewSummary(String cause) {

        if (!write) return;

        Integer numberOfStudents = summary.get(cause);

        if (numberOfStudents == null) summary.put(cause, 1);
        else summary.put(cause, numberOfStudents + 1);

    }

    static private String formatLog(String action, String log) {
        String[] actions = action.split("-");

        for (int i = 0; i < actions.length; i++) actions[i] = "[" + actions[i] + "]";

        return String.format("%-9s : %-8s : %-14s : %-11s : \"%s\"", actions[0], actions[1], actions[2], actions[3], log);
    }

    static public void end() {

        System.out.println("---------- SUMMARY ----------");
        DataIOHandler.writeFile(PATH, "---------- SUMMARY ----------\n", true);

        for (Map.Entry<String, Integer> entry: summary.entrySet()) {
            String[] actions = entry.getKey().split("-");

            String formattedEntry = String.format("%d students couldn't register the course %s due to %s.", entry.getValue(), actions[0], actions[1]);

            System.out.println(formattedEntry);
            DataIOHandler.writeFile(PATH, formattedEntry + "\n", true);
        }

        System.out.println("---------- END ----------");
        DataIOHandler.writeFile(PATH, "---------- END ----------\n", true);
    }
}