package planner.heuristic;

import eval.Objectives;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.Planner;
import planner.model.Mapping;

import java.util.*;

/**
 * Classic Min-Min heuristic planner.
 */
public class MinMinPlanner implements Planner {

    private final Objectives obj;
    private final List<Double> convergence = new ArrayList<>();

    public MinMinPlanner(Objectives obj) {
        this.obj = obj;
    }

    @Override
    public Mapping plan(List<Cloudlet> tasks, List<Vm> vms) {
        Mapping mapping = new Mapping(tasks, vms);

        while (mapping.hasUnassignedTasks()) {
            double bestValue = Double.MAX_VALUE;
            Cloudlet bestTask = null;
            Vm bestVm = null;

            for (Cloudlet t : tasks) {
                if (mapping.isAssigned(t)) continue;

                for (Vm v : vms) {
                    Mapping trial = mapping.copy();
                    trial.assign(t, v);
                    double val = obj.objective(trial, tasks, vms);
                    if (val < bestValue) {
                        bestValue = val;
                        bestTask = t;
                        bestVm = v;
                    }
                }
            }

            if (bestTask != null && bestVm != null) {
                mapping.assign(bestTask, bestVm);
                convergence.add(bestValue); // record after each assignment
            }
        }

        return mapping;
    }

    @Override
    public String name() {
        return "Min-Min";
    }

    public List<Double> getConvergence() {
        return convergence;
    }
}