package com.reactiveplayz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MultilineElements {
    public static LinkedHashMap<Integer, ArrayList<String>> getMultiLineComments() {
        File rmlfile = Main.getFile();
        ArrayList<String> commentLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rmlfile))) {
            commentLines = new ArrayList<>(reader.readAllLines());
        } catch (Exception e) {
            System.out.println("Error: \n");
            e.printStackTrace();
        }
        int commentNo = 0;
        LinkedHashMap<Integer, ArrayList<String>> comments = new LinkedHashMap<>();
        for (int i = 0; i < (commentLines.size() - 1); i++) {

            boolean currentLineComment = Identifier.isComment(commentLines.get(i));
            boolean nextLineComment = false;
            if (i + 1 < commentLines.size() - 1) {
                nextLineComment = Identifier.isComment(commentLines.get(i + 1));
            }
            boolean prevLineComment = false;
            if (i - 1 >= 0) {
                prevLineComment = Identifier.isComment(commentLines.get(i - 1));
            }

            if (i - 2 >= 0) {
                if (!prevLineComment && !Identifier.isComment(commentLines.get(i - 2))
                        && currentLineComment && nextLineComment) {
                    commentNo++;
                }
            }
            if (comments.size() != commentNo + 1) {
                comments.put(commentNo, new ArrayList<String>());
            }
            if (currentLineComment && nextLineComment) {
                comments.get(commentNo).add(Identifier.commentText(commentLines.get(i)));
            } else if (currentLineComment && prevLineComment) {
                comments.get(commentNo).add(Identifier.commentText(commentLines.get(i)));
            }
        }
        return comments;
    }
}
