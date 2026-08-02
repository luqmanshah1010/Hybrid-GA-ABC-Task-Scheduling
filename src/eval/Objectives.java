/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author luqman
 */
package eval;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.model.Mapping;

import java.util.List;

/**
 * Defines the interface for evaluating solutions.
 * For now: single-objective (Makespan).
 * Later: you can replace with multi-objective scalarization (Energy + Makespan)
 * without changing GA/ABC/Hybrid code.
 */
public interface Objectives {
    /**
     * Evaluate the given mapping and return a LOWER-IS-BETTER score.
     * Example: makespan in seconds.
     */
    double objective(Mapping mapping, List<Cloudlet> tasks, List<Vm> vms);

    /** Label for convergence chart Y-axis */
    default String name() {
        return "Makespan (s)";
    }
}
