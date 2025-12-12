package com.reactiveplayz;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static File rmlFile;

    public static File getFile() {
        return rmlFile;
    }

    public static void main(String args[]) {
        /*
         * FileChecker.initiateFilePath();
         * LinkedHashMap<Integer, ArrayList<String>> comments =
         * MultilineElements.getMultiLineComments();
         * for (int i = 0; i < comments.size(); i++) {
         * System.out.println(i + "# | " + comments.get(i));
         * }
         */
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("Enter a (RML) file path: ");
            rmlFile = new File(scanner.nextLine());
        } while (!rmlFile.exists() || !rmlFile.isFile());
        scanner.close();
        Parser.Parse(rmlFile);
        Converter.write(rmlFile);
    }
}
