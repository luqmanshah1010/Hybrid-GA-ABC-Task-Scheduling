package planner.heuristic;

import eval.Objectives;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.Planner;
import planner.model.Mapping;

import java.util.*;

/**
 * Round-Robin heuristic planner.
 */
public class RRPlanner implements Planner {

    private final Objectives obj;
    private final List<Double> convergence = new ArrayList<>();

    public RRPlanner(Objectives obj) {
        this.obj = obj;
    }

    @Override
    public Mapping plan(List<Cloudlet> tasks, List<Vm> vms) {
        Mapping mapping = new Mapping(tasks, vms);

        int vmIndex = 0;
        for (Cloudlet t : tasks) {
            Vm v = vms.get(vmIndex);
            mapping.assign(t, v);

            double val = obj.objective(mapping, tasks, vms);
            convergence.add(val); // record after each assignment

            vmIndex = (vmIndex + 1) % vms.size();
        }

        return mapping;
    }

    @Override
    public String name() {
        return "Round-Robin";
    }

    public List<Double> getConvergence() {
        return convergence;
    }
}