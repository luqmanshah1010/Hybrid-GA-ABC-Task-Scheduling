package eval;

import planner.model.Mapping;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import java.util.List;

public class ResponseTimeObjective {

    public static double compute(
            Mapping mapping,
            List<Cloudlet> tasks,
            List<Vm> vms
    ) {
        double[] vmTime = new double[vms.size()];
        double sumRT = 0.0;

        for (int i = 0; i < tasks.size(); i++) {
            int vmIdx = mapping.getVmIndexForTask(i);
            Cloudlet c = tasks.get(i);
            Vm vm = vms.get(vmIdx);

            double execTime = c.getLength() / (vm.getMips()*vm.getNumberOfPes());
            vmTime[vmIdx] += execTime;
            sumRT += vmTime[vmIdx];
        }
        return sumRT / tasks.size();
    }
}
