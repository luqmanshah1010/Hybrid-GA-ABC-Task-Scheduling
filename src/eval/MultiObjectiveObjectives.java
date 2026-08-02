package eval;

import planner.model.Mapping;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import java.util.List;

public class MultiObjectiveObjectives implements Objectives {

    private final double msMin, msMax, lbMin, lbMax, rtMin, rtMax, uMin, uMax, eMin, eMax;

    private final MakespanObjective msObj = new MakespanObjective();

    public MultiObjectiveObjectives(
            double msMin, double msMax,
            double lbMin, double lbMax,
            double rtMin, double rtMax,
            double uMin, double uMax,
            double eMin, double eMax
    ) {
        this.msMin = msMin; this.msMax = msMax;
        this.lbMin = lbMin; this.lbMax = lbMax;
        this.rtMin = rtMin; this.rtMax = rtMax;
        this.uMin  = uMin;  this.uMax  = uMax;
        this.eMin  = eMin;  this.eMax  = eMax;
    }

    private double clip(double x) {
        return Math.max(0.0, Math.min(1.0, x));
    }

    private double nmin(double v, double min, double max) {
        double n = (v - min) / (max - min + 1e-9);
        return clip(n);
    }

    private double nmax(double v, double min, double max) {
        double n = (max - v) / (max - min + 1e-9);
        return clip(n);
    }

    @Override
    public double objective(Mapping m, List<Cloudlet> t, List<Vm> v) {
        double ms = msObj.objective(m, t, v);
        double lb = LoadBalanceObjective.compute(m, t, v);
        double rt = ResponseTimeObjective.compute(m, t, v);
        double util = ResourceUtilizationObjective.compute(m, t, v, ms);
        double energy = EnergyConsumptionObjective.compute(m, t, v, ms);

        double msN = nmin(ms, msMin, msMax);
        double lbN = nmin(lb, lbMin, lbMax);
        double rtN = nmin(rt, rtMin, rtMax);
        double uN  = nmax(util, uMin, uMax);
        double eN  = nmin(energy, eMin, eMax);

        return 0.30 * msN
             + 0.20 * lbN
             + 0.20 * rtN
             + 0.15 * uN
             + 0.15 * eN;
    }

    @Override
    public String name() {
        return "Multi-Objective Fitness";
    }
}
