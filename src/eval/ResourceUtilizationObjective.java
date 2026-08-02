package eval;

import planner.model.Mapping;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import java.util.List;

public class ResourceUtilizationObjective {

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
double execTime =
    c.getLength() /
    (vm.getMips() * vm.getNumberOfPes());
          //  double execTime = c.getLength() / vm.getMips();
            vmBusy[vmIdx] += execTime;
        }

        double totalBusy = 0.0;
        for (double t : vmBusy) totalBusy += t;

        return totalBusy / (vms.size() * makespan);
    }
}
