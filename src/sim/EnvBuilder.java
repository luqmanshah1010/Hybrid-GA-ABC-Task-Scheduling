package sim;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import app.Config;

import java.util.*;

/**
 * Builds heterogeneous environments (tasks + VMs) using Config.
 */
public class EnvBuilder {
    private final Random rnd;
    private final List<Cloudlet> tasks = new ArrayList<>();
    private final List<Vm> vms = new ArrayList<>();

    public EnvBuilder(long seed) {
        this.rnd = new Random(seed);
        buildVMs();
        buildTasks();
    }

    private void buildVMs() {
        for (int i = 0; i < Config.NUM_VMs; i++) {
            // Assign heterogeneous performance: some fast, some slow
            int baseMips = Config.VM_MIPS_PER_CORE;

            // 30% fast, 40% medium, 30% slow
            int mips;
            double p = rnd.nextDouble();
            if (p < 0.3) {
                mips = baseMips * 5;   // very fast VM
            } else if (p < 0.7) {
                mips = baseMips * 2;   // medium VM
            } else {
                mips = baseMips;       // slow VM
            }

          Vm vm = new VmSimple(i, mips, Config.VM_CORES)
        .setRam(Config.VM_RAM_MB)
        .setBw(Config.VM_BW_MBPS * 8L * 1_000_000L) // ⭐ FIX
        .setSize(10000);

            vms.add(vm);
        }
    }

    private void buildTasks() {
        for (int i = 0; i < Config.NUM_TASKS; i++) {
            long length = Config.TASK_MI_MIN +
                    (long) (rnd.nextDouble() * (Config.TASK_MI_MAX - Config.TASK_MI_MIN));
            long fileSize = Config.TASK_SIZE_MB_MIN +
                    (long) (rnd.nextDouble() * (Config.TASK_SIZE_MB_MAX - Config.TASK_SIZE_MB_MIN));

            Cloudlet cl = new CloudletSimple(i, length, 1)
                    .setFileSize(fileSize)
                    .setOutputSize(fileSize);
            tasks.add(cl);
        }
    }

    public List<Cloudlet> getTasks() {
        return tasks;
    }

    public List<Vm> getVms() {
        return vms;
    }
}