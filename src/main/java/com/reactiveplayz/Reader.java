package com.reactiveplayz;

import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;

public class Reader {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("(READER) Enter a text file path: ");
        File fileToConv = Paths.get(scanner.nextLine()).toFile();
        scanner.close();
        if (!fileToConv.exists() || !fileToConv.isFile()) {
            System.out.println("Path is not a file.");
            System.exit(1);
        }

        try (Scanner reader = new Scanner(fileToConv)) {
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (Identifier.isPlainText(data)) {
                    // System.out.println("Skipped " + data);
                    continue;
                }
                if (Identifier.isKeyValue(data)) {
                    for (String e : Identifier.keyValueGroups(data)) {
                        if (e == null || e.isEmpty()) {
                            continue;
                        }
                        if (e.equals(Identifier.keyValueGroups(data)[3])) {
                            System.out.print(" // " + e);
                            System.out.println();
                            continue;
                        }
                        System.out.print(e);
                    }
                } else {
                    if (Identifier.isComment(data)) {
                        System.out.print("// " + Identifier.getLineValue(data));
                    } else {
                        System.out.print(Identifier.getLineValue(data));
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
        }
    }
}
