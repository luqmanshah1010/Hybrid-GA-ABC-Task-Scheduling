package eval;

import planner.model.Mapping;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import java.util.List;

public class EnergyConsumptionObjective {

    private static final double P_IDLE = 100.0;
    private static final double P_MAX = 200.0;

    public static double compute(
            Mapping mapping,
            List<Cloudlet> tasks,
            List<Vm> vms,
            double makespan
    ) {
        double[] vmBusy = new double[vms.size()];

        for (int i = 0; i < tasks.size(); i++) {
            int vmIdx = mapping.getVmIndexForTask(i);
            Cloudlet c = tasks.get(i);
            Vm vm = vms.get(vmIdx);

            //double execTime = c.getLength() / vm.getMips();
            double execTime = c.getLength() / vm.getMips()*vm.getNumberOfPes();
            vmBusy[vmIdx] += execTime;
        }

        double energy = 0.0;
        for (int i = 0; i < vms.size(); i++) {
            double util = vmBusy[i] / makespan;
            double power = P_IDLE + (P_MAX - P_IDLE) * util;
            energy += power * makespan;
        }

        return energy;
    }
}
