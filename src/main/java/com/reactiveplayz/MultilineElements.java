package com.reactiveplayz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MultilineElements {
    public static void main(String[] args) {
        /*
         * String prevLine;
         * String data = "";
         * ArrayList<String> multiLineComments = new ArrayList<>();
         * try (Scanner reader = new Scanner(FileChecker.getFilePath())) {
         * while (reader.hasNextLine()) {
         * prevLine = data;
         * data = reader.nextLine();
         * if (Identifier.isComment(prevLine) && Identifier.isComment(data)) {
         * int mLCSize = multiLineComments.size();
         * System.out.println(mLCSize);
         * if (mLCSize == 0 || mLCSize == 1) {
         * multiLineComments.add(Identifier.commentText(data));
         * continue;
         * }
         * if (!multiLineComments.get(mLCSize - 1).equals(prevLine)) {
         * multiLineComments.add(Identifier.commentText(prevLine));
         * }
         * multiLineComments.add(Identifier.commentText(data));
         * }
         * }
         * } catch (Exception e) {
         * e.printStackTrace();
         * }
         * for (String x : multiLineComments) {
         * System.out.println(x);
         * }
         */
        File rmlfile = FileChecker.getFile();
        ArrayList<String> commentLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rmlfile))) {
            commentLines = new ArrayList<>(reader.readAllLines());
        } catch (Exception e) {
            System.out.println("Error: \n");
            e.printStackTrace();
        }
        int commentNo = 0;
        for (int i = 0; i < (commentLines.size() - 1); i++) {
            try {
                if (!Identifier.isComment(commentLines.get(i - 1))
                        && !Identifier.isComment(commentLines.get(i - 2))
                        && Identifier.isComment(commentLines.get(i))) {
                    commentNo++;
                }
            } catch (Exception _) {
            }
            if (Identifier.isComment(commentLines.get(i))
                    && Identifier.isComment(commentLines.get(i + 1))) {
                System.out.println(commentNo + "# " + commentLines.get(i));
            } else if (Identifier.isComment(commentLines.get(i))
                    && Identifier.isComment(commentLines.get(i - 1))) {
                System.out.println(commentNo + "# " + commentLines.get(i));
            }
        }
    }
}
