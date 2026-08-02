package app;

import charts.ChartExporter;

import eval.*;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.Planner;
import planner.abc.ABCPlanner;
import planner.ga.GAPlanner;
import planner.hybrid.HybridGAABCPlanner;
import planner.heuristic.MinMinPlanner;
import planner.heuristic.RRPlanner;
import planner.model.Mapping;
import sim.EnvBuilder;
import java.util.ArrayList;
import java.util.List;
import planner.model.Solution;
import charts.ConvergenceTableExporter;

public class Bootstrap {

    public static void main(String[] args) throws Exception {

        Config.applyProfile();
        long seed = Config.SEED;

        Objectives objective = new MakespanObjective();

        // ================================
        // Build environments
        // ================================
        EnvBuilder envGA = new EnvBuilder(seed);
        EnvBuilder envABC = new EnvBuilder(seed);
        EnvBuilder envHybrid = new EnvBuilder(seed);
        EnvBuilder envMinMin = new EnvBuilder(seed);
        EnvBuilder envRR = new EnvBuilder(seed);

        List<Cloudlet> tasksGA = envGA.getTasks();
        List<Vm> vmsGA = envGA.getVms();

        List<Cloudlet> tasksABC = envABC.getTasks();
        List<Vm> vmsABC = envABC.getVms();

        List<Cloudlet> tasksHybrid = envHybrid.getTasks();
        List<Vm> vmsHybrid = envHybrid.getVms();

        List<Cloudlet> tasksMinMin = envMinMin.getTasks();
        List<Vm> vmsMinMin = envMinMin.getVms();

        List<Cloudlet> tasksRR = envRR.getTasks();
        List<Vm> vmsRR = envRR.getVms();

        // ================================
        // Instantiate planners
        // ================================
        Planner ga = new GAPlanner(objective, seed);
        Planner abc = new ABCPlanner(objective, seed);
        Planner hybrid = new HybridGAABCPlanner(objective, seed);
        Planner minmin = new MinMinPlanner(objective);
        Planner rr = new RRPlanner(objective);

        // ================================
        // Run planners
        // ================================
        Mapping gaMap = ga.plan(tasksGA, vmsGA);
        Mapping abcMap = abc.plan(tasksABC, vmsABC);
        Mapping hybridMap = hybrid.plan(tasksHybrid, vmsHybrid);
        Mapping minminMap = minmin.plan(tasksMinMin, vmsMinMin);
        Mapping rrMap = rr.plan(tasksRR, vmsRR);

        // ================================
        // Individual Objectives
        // ================================

        // Makespan
        double gaMS = objective.objective(gaMap, tasksGA, vmsGA);
        double abcMS = objective.objective(abcMap, tasksABC, vmsABC);
        double hybridMS = objective.objective(hybridMap, tasksHybrid, vmsHybrid);
        double mmMS = objective.objective(minminMap, tasksMinMin, vmsMinMin);
        double rrMS = objective.objective(rrMap, tasksRR, vmsRR);

        // Load Balance
        double gaLB = LoadBalanceObjective.compute(gaMap, tasksGA, vmsGA);
        double abcLB = LoadBalanceObjective.compute(abcMap, tasksABC, vmsABC);
        double hybridLB = LoadBalanceObjective.compute(hybridMap, tasksHybrid, vmsHybrid);
        double mmLB = LoadBalanceObjective.compute(minminMap, tasksMinMin, vmsMinMin);
        double rrLB = LoadBalanceObjective.compute(rrMap, tasksRR, vmsRR);

        // Utilization
        double gaU = ResourceUtilizationObjective.compute(gaMap, tasksGA, vmsGA, gaMS);
        double abcU = ResourceUtilizationObjective.compute(abcMap, tasksABC, vmsABC, abcMS);
        double hybridU = ResourceUtilizationObjective.compute(hybridMap, tasksHybrid, vmsHybrid, hybridMS);
        double mmU = ResourceUtilizationObjective.compute(minminMap, tasksMinMin, vmsMinMin, mmMS);
        double rrU = ResourceUtilizationObjective.compute(rrMap, tasksRR, vmsRR, rrMS);

        // Energy
        double gaE = EnergyConsumptionObjective.compute(gaMap, tasksGA, vmsGA, gaMS);
        double abcE = EnergyConsumptionObjective.compute(abcMap, tasksABC, vmsABC, abcMS);
        double hybridE = EnergyConsumptionObjective.compute(hybridMap, tasksHybrid, vmsHybrid, hybridMS);
        double mmE = EnergyConsumptionObjective.compute(minminMap, tasksMinMin, vmsMinMin, mmMS);
        double rrE = EnergyConsumptionObjective.compute(rrMap, tasksRR, vmsRR, rrMS);

        // Response Time
        double gaRT = ResponseTimeObjective.compute(gaMap, tasksGA, vmsGA);
        double abcRT = ResponseTimeObjective.compute(abcMap, tasksABC, vmsABC);
        double hybridRT = ResponseTimeObjective.compute(hybridMap, tasksHybrid, vmsHybrid);
        double mmRT = ResponseTimeObjective.compute(minminMap, tasksMinMin, vmsMinMin);
        double rrRT = ResponseTimeObjective.compute(rrMap, tasksRR, vmsRR);

        // ================================
        // PRINT INDIVIDUAL OBJECTIVES
        // ================================
        System.out.println("===== RESULTS =====");

        System.out.printf("GA → Makespan: %.6f | LoadBalance: %.6f | AvgResponseTime: %.6f | Utilization: %.6f | Energy: %.6f%n",
                gaMS, gaLB, gaRT, gaU, gaE);

        System.out.printf("ABC → Makespan: %.6f | LoadBalance: %.6f | AvgResponseTime: %.6f | Utilization: %.6f | Energy: %.6f%n",
                abcMS, abcLB, abcRT, abcU, abcE);

        System.out.printf("Hybrid → Makespan: %.6f | LoadBalance: %.6f | AvgResponseTime: %.6f | Utilization: %.6f | Energy: %.6f%n",
                hybridMS, hybridLB, hybridRT, hybridU, hybridE);

    //    System.out.printf("Min-Min → Makespan: %.6f | LoadBalance: %.6f | AvgResponseTime: %.6f | Utilization: %.6f | Energy: %.6f%n",
      //          mmMS, mmLB, mmRT, mmU, mmE);

        System.out.printf("Round-Robin → Makespan: %.6f | LoadBalance: %.6f | AvgResponseTime: %.6f | Utilization: %.6f | Energy: %.6f%n",
                rrMS, rrLB, rrRT, rrU, rrE);

        // ================================
        // MULTI-OBJECTIVE NORMALIZATION
        // ================================
        double msMin = Math.min(Math.min(gaMS, abcMS), Math.min(mmMS, rrMS));
        double msMax = Math.max(Math.max(gaMS, abcMS), Math.max(mmMS, rrMS));

        double lbMin = Math.min(Math.min(gaLB, abcLB), Math.min(mmLB, rrLB));
        double lbMax = Math.max(Math.max(gaLB, abcLB), Math.max(mmLB, rrLB));

        double rtMin = Math.min(Math.min(gaRT, abcRT), Math.min(mmRT, rrRT));
        double rtMax = Math.max(Math.max(gaRT, abcRT), Math.max(mmRT, rrRT));

        double uMin = Math.min(Math.min(gaU, abcU), Math.min(mmU, rrU));
        double uMax = Math.max(Math.max(gaU, abcU), Math.max(mmU, rrU));

        double eMin = Math.min(Math.min(gaE, abcE), Math.min(mmE, rrE));
        double eMax = Math.max(Math.max(gaE, abcE), Math.max(mmE, rrE));

        MultiObjectiveObjectives moObj =
                new MultiObjectiveObjectives(
                        msMin, msMax,
                        lbMin, lbMax,
                        rtMin, rtMax,
                        uMin, uMax,
                        eMin, eMax
                );
// ================================
// MULTI OBJECTIVE CONVERGENCE
// ================================

List<Double> gaConv = new ArrayList<>();

for (Solution s : ((GAPlanner) ga).getBestSolutions()) {
    Mapping m = new Mapping(tasksGA, vmsGA, s);
    gaConv.add(moObj.objective(m, tasksGA, vmsGA));
}

List<Double> abcConv = new ArrayList<>();

for (Solution s : ((ABCPlanner) abc).getBestSolutions()) {
    Mapping m = new Mapping(tasksABC, vmsABC, s);
    abcConv.add(moObj.objective(m, tasksABC, vmsABC));
}

List<Double> hybridConv = new ArrayList<>();

for (Solution s : ((HybridGAABCPlanner) hybrid).getBestSolutions()) {
    Mapping m = new Mapping(tasksHybrid, vmsHybrid, s);
    hybridConv.add(moObj.objective(m, tasksHybrid, vmsHybrid));
}

// Export convergence graph
ChartExporter.exportConvergence(
        gaConv,
        abcConv,
        hybridConv,
        null
);
// Export convergence graph
ChartExporter.exportConvergence(
        gaConv,
        abcConv,
        hybridConv,
        null
);

// ================================
// EXPORT CONVERGENCE TABLES
// ================================

ConvergenceTableExporter.export("outputs/ga_convergence.csv", gaConv);

ConvergenceTableExporter.export("outputs/abc_convergence.csv", abcConv);

ConvergenceTableExporter.export("outputs/hybrid_convergence.csv", hybridConv);

ConvergenceTableExporter.exportCombined(
        "outputs/convergence_combined.csv",
        gaConv,
        abcConv,
        hybridConv
);
        // ================================
        // F(X) FOR ALL ALGORITHMS
        // ================================
        double gaFX = moObj.objective(gaMap, tasksGA, vmsGA);
        double abcFX = moObj.objective(abcMap, tasksABC, vmsABC);
        double hybridFX = moObj.objective(hybridMap, tasksHybrid, vmsHybrid);
        double mmFX = moObj.objective(minminMap, tasksMinMin, vmsMinMin);
        double rrFX = moObj.objective(rrMap, tasksRR, vmsRR);

        System.out.println("===== MULTI-OBJECTIVE FITNESS =====");
        System.out.printf("GA -> F(X): %.6f%n", gaFX);
        System.out.printf("ABC -> F(X): %.6f%n", abcFX);
        System.out.printf("Hybrid -> F(X): %.6f%n", hybridFX);
        //System.out.printf("Min-Min -> F(X): %.6f%n", mmFX);
        System.out.printf("Round-Robin -> F(X): %.6f%n", rrFX);

        System.out.println("Saved: outputs/... charts generated.");
 ChartExporter.exportMakespanComparison(gaMS, abcMS, hybridMS,rrMS);
 ChartExporter.exportLoadBalanceComparison(gaLB, abcLB, hybridLB, rrLB);
 ChartExporter.exportResourceUtilizationComparison(gaU, abcU, hybridU,rrU);
 ChartExporter.exportResponseTimeComparison(gaRT, abcRT, hybridRT, rrRT);
 ChartExporter.exportEnergyConsumptionComparison(gaE, abcE, hybridE, rrE);
ChartExporter.exportMOComparison(gaFX, abcFX, hybridFX, rrFX);
    }
    
}
