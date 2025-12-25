package com.reactiveplayz.rml;

import java.io.File;

import com.reactiveplayz.rml.serializer.JSONSerializer;

public class Main {
    private static File rmlFile = new File("");
    private static RMLFile parsedRMLFile;
    private static final String version = "1.1";

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
            if (args[0].toLowerCase().equals("info") || args[0].toLowerCase().equals("--info")) {
                infoMsg();
                System.exit(0);
            }
            if (!args[0].startsWith("--")) {
                System.out.println("Commands and flags must start with --");
                System.exit(1);
            }
            helpMsg(args[0]);
            System.exit(1);
        }
        if (args.length >= 2) {
            if (args[0].toLowerCase().equals("help") || args[0].toLowerCase().equals("--help")) {
                helpMsg(args[1]);
                System.exit(0);
            }
            if (args[0].toLowerCase().equals("--createjson")) {
                rmlFile = new File(args[1]);
                if (args.length >= 3 && args[1].toLowerCase().equals("--prettyjson")) {
                    rmlFile = new File(args[2]);
                }
                if (!rmlFile.exists() || !rmlFile.isFile()) {
                    System.out.println("The given path does not exist or is not a file: " + rmlFile.getAbsolutePath());
                    System.exit(1);
                }
                parsedRMLFile = new RMLFile(rmlFile.getName());
                JSONSerializer JsonConverter = new JSONSerializer(parsedRMLFile);
                Parser.Parse(rmlFile, parsedRMLFile);
                if (args.length == 3 && args[1].toLowerCase().equals("--prettyjson")) {
                    JsonConverter.write(true);
                } else {
                    JsonConverter.write();
                }
                System.out.println("Created file '" + rmlFile.getName() + " rml.json' in current directory.");
            }
        }
    }

    private static void infoMsg() {
        System.out.println("rml Version: " + version);
        System.out.println("rml GitHub: https://github.com/ReactivePlayZ/RML");
        System.out.println();
    }

    public static void helpMsg() {
        System.out.println("rml Interpreter:");
        infoMsg();
        helpMsg("--help");
        helpMsg("--createjson");
        helpMsg("--prettyJson");

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
                System.out.println("    Converts the rml file to JSON");
                System.out.println("    --createJson <path to file>");
                System.out.println();
                System.out.println("    --prettyJson:");
                System.out.println("        Enables pretty printing of JSON for conversion");
                System.out.println("        --createJson --prettyJson <path to file>");
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
