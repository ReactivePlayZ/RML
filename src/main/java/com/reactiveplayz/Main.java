package com.reactiveplayz;

import java.io.File;

public class Main {
    private static File rmlFile;

    public static File getFile() {
        return rmlFile;
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            helpMsg();
            System.exit(0);
        }
        if (args.length == 1) {
            if (args[0].toLowerCase().equals("help") || args[0].toLowerCase().equals("--help")
                    || args[0].equals("/?")) {
                helpMsg();
                System.exit(0);
            }
            if (args[0].toLowerCase().equals("--createjson")) {
                helpMsg("--createjson");
                System.exit(1);
            }
            if (!args[0].startsWith("--")) {
                System.out.println("Commands and flags must start with --");
                System.exit(1);
            }
            helpMsg();
        }
        if (args.length >= 2) {
            if (args[0].toLowerCase().equals("help") || args[0].toLowerCase().equals("--help")) {
                helpMsg(args[1]);
                System.exit(0);
            }
            if (args[0].toLowerCase().equals("--createjson")) {
                rmlFile = new File(args[1]);
                if (!rmlFile.exists() || !rmlFile.isFile()) {
                    System.out.println("The given path does not exist or is not a file: " + rmlFile.getAbsolutePath());
                    System.exit(1);
                }
                Parser.Parse(rmlFile);
                Converter.write(rmlFile);
                System.out.println("Created file '" + rmlFile.getName() + " rml.json' in current directory.");
            }
        }
    }

    public static void helpMsg() {
        System.out.println("RML Interpreter:");
        helpMsg("--help");
        helpMsg("--createjson");

    }

    public static void helpMsg(String command) {
        command = command.toLowerCase();
        switch (command) {
            case "--help", "help":
                System.out.println("--help or help:");
                System.out.println("    Without arguments prints all command usages");
                System.out.println("    With a command argument prints that command's help message:");
                System.out.println("    --help (--command)");
                System.out.println();
                break;
            case "--createjson", "createjson":
                System.out.println("--createJson:");
                System.out.println("    Converts the RML file to JSON");
                System.out.println("    --createJson <path to file>");
                System.out.println();
                break;
            case "--output", "output":
                System.out.println("--output:");
                System.out.println("    Not implemented yet but will allow to specify output directory");
                break;
            default:
                System.out.println("no command found");
        }
    }
}
