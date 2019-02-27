package schedulingApp.model;

import java.io.*;
import java.time.LocalDateTime;

public class Output {

    private static final String FILENAME = "LoginRecord.txt";

    //From: https://www.mkyong.com/java/how-to-append-content-to-file-in-java/
    public static void recordLogin() {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            String data = "User: " + DataSource.getUserLoggedIn() +"\t Login Time:  " + LocalDateTime.now();
            File file = new File(FILENAME);

            if(!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.newLine(); // bw.newLine(); adds carriage return. From: https://stackoverflow.com/questions/2832756/carriage-return-line-feed-in-java
            bw.write(data);
        } catch (IOException e) {
            System.out.println("Output.recordLogin() IOException error: " + e.getMessage());
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                System.out.println("Output.recordLogin() IOException error: " + e.getMessage());

            }
        }
    }
}
