package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EventLog {
    public static void saveStringBuilderToFile(StringBuilder sb) {
        String filePath = "log1.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(sb.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
