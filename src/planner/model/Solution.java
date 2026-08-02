package planner.model;

import java.util.Arrays;
import java.util.Random;
import eval.Objectives;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import java.util.List;
import planner.ga.GAPlanner;


/**
 * Represents a candidate solution (task → VM assignment).
 */
public class Solution {
    private int[] assignment;   // assignment[i] = VM index for task i
    private double score;       // evaluation score (e.g., makespan)

    /** Create empty solution (all -1). */
    public Solution(int taskCount) {
        this.assignment = new int[taskCount];
        Arrays.fill(this.assignment, -1);
        this.score = Double.MAX_VALUE;
    }

    /** Create from an existing assignment array. */
    public Solution(int[] assignment) {
        this.assignment = Arrays.copyOf(assignment, assignment.length);
        this.score = Double.MAX_VALUE;
    }

    /** Copy constructor. */
    public Solution(Solution other) {
        this.assignment = Arrays.copyOf(other.assignment, other.assignment.length);
        this.score = other.score;
    }

    
        /**
     * Construct a Solution from an existing Mapping (assignment only).
     * Does NOT evaluate score.
/**
 * Construct a Solution from an existing Mapping (assignment only).
 */
public Solution(Mapping map) {
    this.assignment = (map != null) ? map.getAssignmentArray() : new int[0];
    this.score = Double.MAX_VALUE;
}

/**
 * Construct a Solution from an existing Mapping and immediately evaluate
 * its objective score.
 */
public Solution(Mapping map, Objectives obj, List<Cloudlet> tasks, List<Vm> vms) {
    this.assignment = (map != null) ? map.getAssignmentArray() : new int[0];
    this.score = (map != null && obj != null) ? obj.objective(map, tasks, vms)
                                              : Double.MAX_VALUE;
}



    /** Factory: random solution. */
    public static Solution random(int taskCount, int vmCount, Random rnd) {
        Solution s = new Solution(taskCount);
        for (int i = 0; i < taskCount; i++) {
            s.assignment[i] = rnd.nextInt(vmCount);
        }
        return s;
    }

// --- Mutation operator ---
// Returns a NEW Solution (does not modify this one).
public Solution mutate(Random rnd, int vmCount) {
    // Copy the current assignment
    int[] newAssign = Arrays.copyOf(this.assignment, this.assignment.length);

    // Randomly pick one task to remap
    int idx = rnd.nextInt(newAssign.length);
    newAssign[idx] = rnd.nextInt(vmCount);

    // Return a fresh Solution with mutated assignment
    return new Solution(newAssign);
}



    /** One-point crossover. */
    public Solution crossover(Solution other, Random rnd) {
        int point = rnd.nextInt(assignment.length);
        int[] childAssign = new int[assignment.length];
        for (int i = 0; i < assignment.length; i++) {
            childAssign[i] = (i < point ? this.assignment[i] : other.assignment[i]);
        }
        return new Solution(childAssign);
    }
        /**
     * Generate a neighbor solution (used in ABC).
     * Standard ABC neighbor: pick a random task index i, pick a second random
     * solution index k != i, then slightly perturb the assignment of i
     * using a random phi in [-1,1] and modulo vmCount.
     */
    public Solution neighbor(Random rnd, int vmCount) {
        // Copy current assignment
        int[] newAssign = Arrays.copyOf(this.assignment, this.assignment.length);

        if (newAssign.length == 0 || vmCount <= 0) {
            return new Solution(newAssign); // nothing to change
        }

        // Pick a random position i
        int i = rnd.nextInt(newAssign.length);

        // Pick a different random position k != i
        int k;
        do { k = rnd.nextInt(newAssign.length); } while (k == i && newAssign.length > 1);

        // ABC standard: new = old[i] + phi*(old[i] - old[k])
        double phi = rnd.nextDouble() * 2 - 1; // [-1,1]
        int val = newAssign[i] + (int)Math.round(phi * (newAssign[i] - newAssign[k]));

        // Ensure within [0, vmCount-1]
        if (val < 0) val = 0;
        if (val >= vmCount) val = vmCount - 1;

        newAssign[i] = val;
        return new Solution(newAssign);
    }


    /** Deep copy. */
    public Solution copy() {
        return new Solution(this);
    }

    /** Copy from another solution. */
    public void copyFrom(Solution other) {
        this.assignment = Arrays.copyOf(other.assignment, other.assignment.length);
        this.score = other.score;
    }

    // --- Getters/Setters ---
    public int[] getAssignment() { return assignment; }

    public void setAssignment(int[] assignment) {
        this.assignment = Arrays.copyOf(assignment, assignment.length);
    }

    public double getScore() { return score; }

    public void setScore(double score) { this.score = score; }

    /** Debug-friendly representation. */
    @Override
    public String toString() {
        return "Solution{" +
                "assignment=" + Arrays.toString(assignment) +
                ", score=" + score +
                '}';
    }
}