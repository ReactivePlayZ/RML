package com.reactiveplayz;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String args[]) {
        FileChecker.initiateFilePath();
        LinkedHashMap<Integer, ArrayList<String>> comments = MultilineElements.getMultiLineComments();
        for (int i = 0; i < comments.size(); i++) {
            System.out.println(i + "# | " + comments.get(i));
        }
    }
}
