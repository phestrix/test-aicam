package ru.phestrix.commandLineParser;

import java.util.Objects;

public class CommandLineParser {
    public static String parseArgs(String[] args) {
        if (args.length != 3) {
            return "Bad number of arguments";
        }
        String nameOfOperation = args[0];
        String pathToInputFile = args[1];
        String pathToOutputFile = args[2];
        if (!Objects.equals(nameOfOperation, "search") ||
                !Objects.equals(nameOfOperation, "stat")) {
            return "unsupported type of operation";
        }
        if (!pathToInputFile.contains(".json") || !pathToOutputFile.contains(".json")) {
            return "unsupported file format";
        }
        return nameOfOperation;
    }
}
