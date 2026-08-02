/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author luqman
 */
package eval;

import app.Config;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.model.Mapping;

import java.util.List;

/**
 * Implements your Makespan formula from the DOCX.
 * Right now: single objective (makespan).
 * Later: you can extend it to multi-objective (e.g., makespan + energy).
 */
public class MakespanObjective implements Objectives {

    @Override
    public double objective(Mapping mapping, List<Cloudlet> tasks, List<Vm> vms) {
        double maxCompletion = 0.0;

        for (Vm vm : vms) {
            double finishTime = 0.0;

            // Each Cloudlet assigned to this VM
            for (Cloudlet cl : mapping.getTasksAssignedTo(vm)) {
                // Execution time (CPU) = MI / (MIPS * cores)
                double exec = (double) cl.getLength() / (vm.getMips() * vm.getNumberOfPes());

                // Transfer time (network) = size / bandwidth
                double transfer = (double) cl.getFileSize() / vmBandwidthMBps(vm);

                // Migration overhead = α * size + T_mig
                double migration = cl.getFileSize() * Config.ALPHA_SWAP + Config.T_MIG_SEC;

                // Accumulate task times on this VM (sequential scheduling assumption)
                finishTime += exec + transfer + migration;
            }

            // Makespan = maximum VM finish time
            maxCompletion = Math.max(maxCompletion, finishTime);
        }
        return maxCompletion;
    }

 
    @Override
    public String name() {
        return "Makespan (s)";
    }

    /** Helper: convert VM bandwidth from MBps (as set in Config). */
/** Helper: convert VM bandwidth to MB/s safely. */
private static double vmBandwidthMBps(Vm vm) {

    double bw = vm.getBw().getCapacity();

    // 🔴 Guard against zero or invalid bandwidth
    if (bw <= 0) {
        return 1.0; // prevent division explosion
    }

    // 🧠 CloudSim Plus stores bw in bits/sec.
    // Convert bits/sec → MB/sec
    double mbps = bw / (8.0 * 1_000_000.0);

    // 🔴 Safety floor to avoid extreme transfer times
    if (mbps < 0.01) {
        mbps = 0.01;
    }

    return mbps;
}


}