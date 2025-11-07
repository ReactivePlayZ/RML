/*
 * 
 * My Text Watchlist to JSON format
 * by @ReactivePlayZ
 * Started at 30th Oct. 2025 @ 2:40pm
 * Read README.md for more information
 * 
 */
package com.reactiveplayz;

import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileChecker {
    private static File filePath = Paths.get(System.getProperty("user.dir")).toFile();

    public static File getFile() {
        return filePath;
    }

    public static void initiateFilePath() {

        System.out.print("Enter file path: ");
        try (Scanner scanner = new Scanner(System.in)) {
            filePath = Paths.get(scanner.nextLine()).toFile();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        if (!filePath.exists() || !filePath.isFile()) {
            System.err.println("Invalid file path");
            System.exit(1);
        }
        // '\\s+' is regex for all whitespaces
        System.out.println("Using file path: " + filePath.getAbsolutePath());

    }
}