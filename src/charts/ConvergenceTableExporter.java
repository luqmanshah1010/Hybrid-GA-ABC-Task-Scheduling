
package charts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class to export convergence tables for algorithms
 * without modifying the existing project logic.
 */
public class ConvergenceTableExporter {

    public static void export(String fileName, List<Double> values) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Iteration,Value\n");
            for (int i = 0; i < values.size(); i++) {
                writer.write((i + 1) + "," + values.get(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportCombined(
            String fileName,
            List<Double> ga,
            List<Double> abc,
            List<Double> hybrid) {

        int max = Math.max(ga.size(), Math.max(abc.size(), hybrid.size()));

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Iteration,GA,ABC,Hybrid\n");

            for (int i = 0; i < max; i++) {
                Double g = i < ga.size() ? ga.get(i) : null;
                Double a = i < abc.size() ? abc.get(i) : null;
                Double h = i < hybrid.size() ? hybrid.get(i) : null;

                writer.write((i + 1) + "," +
                        (g != null ? g : "") + "," +
                        (a != null ? a : "") + "," +
                        (h != null ? h : "") + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
