
package charts;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.CategoryStyler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.internal.chartpart.Chart;

public class ChartExporter {

    // =========================================================
    // IEEE-safe, colorblind-friendly palette
    // =========================================================
private static Color[] journalColors() {
    return new Color[]{
            new Color(0, 114, 178),   // GA
            new Color(213, 94, 0),    // ABC
            new Color(0, 158, 115),   // Hybrid
            new Color(86, 180, 233)   // RR
    };
}


    // =========================================================
    // IEEE professional styling
    // =========================================================
    private static void applyJournalStyle(CategoryChart chart) {

        CategoryStyler styler = chart.getStyler();

     //   styler.setSeriesColors(journalColors());

        // thinner bars but tightly packed
        styler.setAvailableSpaceFill(0.7);
        styler.setOverlapped(true);

        // remove legend (requested)
        styler.setLegendVisible(true);

        // clean white background
        styler.setChartBackgroundColor(Color.WHITE);
        styler.setPlotBackgroundColor(Color.WHITE);

        // light grid for print
        styler.setPlotGridLinesVisible(true);
        styler.setPlotGridLinesColor(new Color(220, 220, 220));

        // IEEE-like fonts
        Font titleFont = new Font("Times New Roman", Font.BOLD, 18);
        styler.setChartTitleFont(titleFont);

        // remove heavy borders
        styler.setPlotBorderVisible(false);

        // anti-alias for sharp export
        styler.setAntiAlias(true);
    }

    // =========================================================
    // Ensure outputs directory exists
    // =========================================================
    private static void ensureOutputDir() {
        try {
            Files.createDirectories(Paths.get("outputs"));
        } catch (IOException e) {
            System.err.println("Failed to create outputs directory: " + e.getMessage());
        }
    }

    // =========================================================
    // Helper to add colored bars (names below bars)
    // =========================================================
// =========================================================
// Helper to add colored bars (IEEE clean version)
// =========================================================
// =========================================================
// Helper to add colored bars (FIXED per-bar colors)
// =========================================================
// =========================================================
// Helper to add colored bars (PROPER per-bar colors)
// =========================================================
private static void addAlgorithmBars(CategoryChart chart,
                                     double ga, double abc,
                                     double hybrid, double rr) {

    List<String> cats = List.of("GA", "ABC", "Hybrid", "RR");

    // Each algorithm gets its own series positioned at its category
    chart.addSeries("GA", cats, List.of(ga, 0.0, 0.0, 0.0));
    chart.addSeries("ABC", cats, List.of(0.0, abc, 0.0, 0.0));
    chart.addSeries("Hybrid", cats, List.of(0.0, 0.0, hybrid, 0.0));
    chart.addSeries("RR", cats, List.of(0.0, 0.0, 0.0, rr));

    // Professional journal palette
    chart.getStyler().setSeriesColors(new Color[]{
            new Color(0, 114, 178),   // GA
            new Color(213, 94, 0),    // ABC
            new Color(0, 158, 115),   // Hybrid
            new Color(86, 180, 233)   // RR
    });
}



    // =========================================================
    // Makespan
    // =========================================================
    public static void exportMakespanComparison(
            double ga, double abc, double hybrid,double rr) {

        ensureOutputDir();

        CategoryChart chart = new CategoryChartBuilder()
                .width(900).height(600)
                .title("Makespan Comparison")
                .xAxisTitle("Algorithms")
                .yAxisTitle("Makespan (s)")
                .build();

        applyJournalStyle(chart);
        addAlgorithmBars(chart, ga, abc, hybrid, rr);

        save(chart, "outputs/makespan_comparison.png");
    }

    // =========================================================
    // Load balance
    // =========================================================
    public static void exportLoadBalanceComparison(
            double ga, double abc, double hybrid, double rr) {

        ensureOutputDir();

        CategoryChart chart = new CategoryChartBuilder()
                .width(900).height(600)
                .title("Load Imbalance Comparison")
                .xAxisTitle("Algorithms")
                .yAxisTitle("Load Imbalance (s)")
                .build();

        applyJournalStyle(chart);
        addAlgorithmBars(chart, ga, abc, hybrid,rr);

        save(chart, "outputs/load_balance_comparison.png");
    }

    // =========================================================
    // Resource utilization
    // =========================================================
    public static void exportResourceUtilizationComparison(
            double ga, double abc, double hybrid, double rr) {

        ensureOutputDir();

        CategoryChart chart = new CategoryChartBuilder()
                .width(900).height(600)
                .title("Average Resource Utilization Comparison")
                .xAxisTitle("Algorithms")
                .yAxisTitle("Average Resource Utilization Rate (ARUR)")
                .build();

        applyJournalStyle(chart);
        addAlgorithmBars(chart, ga, abc, hybrid,rr);

        save(chart, "outputs/resource_utilization_comparison.png");
    }

    // =========================================================
    // Energy
    // =========================================================
    public static void exportEnergyConsumptionComparison(
            double ga, double abc, double hybrid, double rr) {

        ensureOutputDir();

        CategoryChart chart = new CategoryChartBuilder()
                .width(900).height(600)
                .title("Energy Consumption Comparison")
                .xAxisTitle("Algorithms")
                .yAxisTitle("Energy (joules)")
                .build();

        applyJournalStyle(chart);
        addAlgorithmBars(chart, ga, abc, hybrid, rr);

        save(chart, "outputs/energy_consumption_comparison.png");
    }

    // =========================================================
    // Response time
    // =========================================================
    public static void exportResponseTimeComparison(
            double ga, double abc, double hybrid, double rr) {

        ensureOutputDir();

        CategoryChart chart = new CategoryChartBuilder()
                .width(900).height(600)
                .title("Average Response Time Comparison")
                .xAxisTitle("Algorithms")
                .yAxisTitle("Average Response Time(ART) (s)")
                .build();

        applyJournalStyle(chart);
        addAlgorithmBars(chart, ga, abc, hybrid, rr);

        save(chart, "outputs/response_time_comparison.png");
    }

    // =========================================================
    // Multi-objective
    // =========================================================
    public static void exportMOComparison(
            double ga, double abc, double hybrid, double rr) {

        ensureOutputDir();

        CategoryChart chart = new CategoryChartBuilder()
                .width(900).height(600)
                .title("Multi-Objective Fitness Comparison")
                .xAxisTitle("Algorithms")
                .yAxisTitle("Multi-objective Fitness (F(X))")
                .build();

        applyJournalStyle(chart);
        addAlgorithmBars(chart, ga, abc, hybrid, rr);

        save(chart, "outputs/mo_comparison.png");
    }

    // =========================================================
    // Convergence
    // =========================================================
    public static void exportConvergence(
            List<Double> ga, List<Double> abc, List<Double> hybrid,
            List<Double> rr) {

        ensureOutputDir();

        XYChart chart = new XYChartBuilder()
                .width(900).height(600)
                .title("Convergence Curve-MIXED Profile (Baseline)")
                .xAxisTitle("Iteration")
                .yAxisTitle("Fitness")
                .build();

        chart.getStyler().setSeriesColors(journalColors());
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotGridLinesVisible(true);
        chart.getStyler().setPlotGridLinesColor(new Color(220, 220, 220));
        chart.getStyler().setLegendVisible(true);

        if (ga != null) chart.addSeries("GA", ga);
        if (abc != null) chart.addSeries("ABC", abc);
        if (hybrid != null) chart.addSeries("Hybrid", hybrid);
//        if (minmin != null) chart.addSeries("Min-Min", minmin);
        if (rr != null) chart.addSeries("RR", rr);

        save(chart, "outputs/convergence.png");
    }

    // =========================================================
    // Save helper
    // =========================================================
    private static void save(Chart<?, ?> chart, String path) {
        try {
            BitmapEncoder.saveBitmap(chart, path, BitmapEncoder.BitmapFormat.PNG);
            System.out.println("Saved chart: " + path);
        } catch (IOException e) {
            System.err.println("Chart save FAILED: " + path);
            e.printStackTrace();
        }
    }
}
