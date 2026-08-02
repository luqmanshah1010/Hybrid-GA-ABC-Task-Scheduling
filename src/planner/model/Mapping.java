package planner.model;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.*;

/**
 * Represents a mapping between Cloudlets (tasks) and VMs.
 */
public class Mapping {
    private final List<Cloudlet> tasks;
    private final List<Vm> vms;
    private final int[] assignment;   // assignment[i] = VM index for task i, -1 if unassigned

    public Mapping(List<Cloudlet> tasks, List<Vm> vms) {
        this.tasks = tasks;
        this.vms = vms;
        this.assignment = new int[tasks.size()];
        Arrays.fill(this.assignment, -1);
    }

    /** Copy constructor */
    public Mapping(Mapping other) {
        this.tasks = other.tasks;
        this.vms = other.vms;
        this.assignment = Arrays.copyOf(other.assignment, other.assignment.length);
    }
    public Mapping(List<Cloudlet> tasks, List<Vm> vms, Solution sol) {
    this.tasks = tasks;
    this.vms = vms;
    this.assignment = Arrays.copyOf(sol.getAssignment(), sol.getAssignment().length);
}

    /** Create a deep copy */
    public Mapping copy() {
        return new Mapping(this);
    }

    /** Assign a task to a VM */
    public void assign(Cloudlet task, Vm vm) {
        int taskIndex = tasks.indexOf(task);
        int vmIndex = vms.indexOf(vm);

        if (taskIndex >= 0 && vmIndex >= 0) {
            assignment[taskIndex] = vmIndex;
        }
    }

    /** Check if a task is already assigned */
    public boolean isAssigned(Cloudlet task) {
        int idx = tasks.indexOf(task);
        return idx >= 0 && assignment[idx] != -1;
    }

    /** Check if there are still unassigned tasks */
    public boolean hasUnassignedTasks() {
        for (int vmId : assignment) {
            if (vmId == -1) return true;
        }
        return false;
    }

    /** Get VM assigned to a task */
    public Vm getVmForTask(Cloudlet task) {
        int taskIndex = tasks.indexOf(task);
        if (taskIndex >= 0 && assignment[taskIndex] != -1) {
            return vms.get(assignment[taskIndex]);
        }
        return null;
    }

    /** Get all tasks assigned to a VM */
    public List<Cloudlet> getTasksAssignedTo(Vm vm) {
        List<Cloudlet> result = new ArrayList<>();
        int vmIndex = vms.indexOf(vm);
        if (vmIndex == -1) return result;

        for (int i = 0; i < tasks.size(); i++) {
            if (assignment[i] == vmIndex) {
                result.add(tasks.get(i));
            }
        }
        return result;
    }
        /** Get number of tasks in this mapping. */
    public int getTaskCount() {
        return assignment.length;
    }

    /** Get a copy of the current assignment array (task → VM). */
    public int[] getAssignmentArray() {
        return Arrays.copyOf(assignment, assignment.length);
    }

    /** Get the VM index assigned to a task index. */
    public int getVmIndexForTask(int taskIndex) {
        if (taskIndex >= 0 && taskIndex < assignment.length) {
            return assignment[taskIndex];
        }
        return -1;
    }

}