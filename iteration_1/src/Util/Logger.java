package Util;

public class Logger {

    static private final String PATH = DataIOHandler.currentPath + "src/log.txt";
    static private boolean write = false;

    static public void setup() {
        DataIOHandler.writeFile(PATH, "---\n", true);
        write = true;
    }

    static public void addNewLog(String action, String log) {
        if (!write) return;
        
        String formattedLog = formatLog(action, log);

        System.out.println(formattedLog);
        DataIOHandler.writeFile(PATH, formattedLog + "\n", true);
    }

    static private String formatLog(String action, String log) {
        String[] actions = action.split("-");

        for (int i = 0; i < actions.length; i++) actions[i] = "[" + actions[i] + "]";

        return String.format("%-12s : %-19s : %-11s : \"%s\"", actions[0], actions[1], actions[2], log);
    }

    static public void end() {
        DataIOHandler.writeFile(PATH, "---\n", true);
    }
}