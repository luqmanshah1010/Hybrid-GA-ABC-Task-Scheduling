package eval;

import planner.model.Mapping;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import java.util.List;

public class LoadBalanceObjective {

    public static double compute(Mapping map, List<Cloudlet> tasks, List<Vm> vms) {
        int m = vms.size();
        double[] load = new double[m];

        for (int i = 0; i < tasks.size(); i++) {
            int vmIdx = map.getVmIndexForTask(i);
            if (vmIdx >= 0) {
                Cloudlet c = tasks.get(i);
                Vm vm = vms.get(vmIdx);
              load[vmIdx] += c.getLength() /
               (vm.getMips() * vm.getNumberOfPes());

            }
        }

        double max = 0, sum = 0;
        for (double l : load) {
            if (l > max) max = l;
            sum += l;
        }
        return max - (sum / m);
    }
}
