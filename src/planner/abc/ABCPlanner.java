package planner.abc;

import eval.Objectives;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.Planner;
import planner.model.Mapping;
import planner.model.Solution;

import java.util.*;

/**
 * Improved Artificial Bee Colony (ABC) planner.
 * - Adaptive perturbation factor (phi decreases with cycles)
 * - Early scout reset (limit = 5)
 * - Greedy replacement (<=)
 */
public class ABCPlanner implements Planner {
private final List<Solution> bestSolutions = new ArrayList<>();
    private final Objectives obj;
    private final Random rnd;

    // ABC hyperparams
    private final int foodSources;   // number of food sources
    private final int cycles;      // number of cycles
    private final int limit;       // scout reset limit

    // State
    private final List<Solution> foods = new ArrayList<>();
    private final int[] trial;     // trial counters per food source
    private Solution best = null;
    private final List<Double> convergence = new ArrayList<>();

    public ABCPlanner(Objectives obj, long seed) {
        this(obj, seed, 40, 50, 5); // defaults
    }

    public ABCPlanner(Objectives obj, long seed, int foodSources, int cycles, int limit) {
        this.obj = obj;
        this.rnd = new Random(seed);
        this.foodSources = foodSources;
        this.cycles = cycles;
        this.limit = limit;
        this.trial = new int[foodSources];
    }

    @Override
    public String name() {
        return "ABC";
    }

    /*@Override
    public Mapping plan(List<Cloudlet> tasks, List<Vm> vms) {
        foods.clear();
        Arrays.fill(trial, 0);

        // initialize food sources
        for (int i = 0; i < foodCount; i++) {
            Solution s = Solution.random(tasks.size(), vms.size(), rnd);
            double score = obj.objective(new Mapping(tasks, vms, s), tasks, vms);
            s.setScore(score);
            foods.add(s);
        }

        best = foods.stream().min(Comparator.comparingDouble(Solution::getScore)).get();
        convergence.add(best.getScore());

        // main loop
        for (int cycle = 0; cycle < cycles; cycle++) {
            // employed bees
            for (int i = 0; i < foodCount; i++) {
                Solution candidate = mutate(foods.get(i), cycle, tasks.size(), vms.size());
                double score = obj.objective(new Mapping(tasks, vms, candidate), tasks, vms);
                candidate.setScore(score);

                if (score <= foods.get(i).getScore()) { // greedy
                    foods.set(i, candidate);
                    trial[i] = 0;
                } else {
                    trial[i]++;
                }
            }
           

            // onlooker bees
            double[] probs = calcProbabilities();
            for (int i = 0; i < foodCount; i++) {
                if (rnd.nextDouble() < probs[i]) {
                    Solution candidate = mutate(foods.get(i), cycle, tasks.size(), vms.size());
                    double score = obj.objective(new Mapping(tasks, vms, candidate), tasks, vms);
                    candidate.setScore(score);

                    if (score <= foods.get(i).getScore()) { // greedy
                        foods.set(i, candidate);
                        trial[i] = 0;
                    } else {
                        trial[i]++;
                    }
                }
            }

            // scouts
            for (int i = 0; i < foodCount; i++) {
                if (trial[i] > limit) {
                    Solution s = Solution.random(tasks.size(), vms.size(), rnd);
                    double score = obj.objective(new Mapping(tasks, vms, s), tasks, vms);
                    s.setScore(score);
                    foods.set(i, s);
                    trial[i] = 0;
                }
            }

            // update global best
            Solution cycleBest = foods.stream().min(Comparator.comparingDouble(Solution::getScore)).get();
            if (cycleBest.getScore() < best.getScore()) {
                best = cycleBest.copy();
            }
            convergence.add(best.getScore());
        }

        return new Mapping(tasks, vms, best);
    }*/
  // keep your old API
public Mapping plan(List<Cloudlet> tasks, List<Vm> vms) {
    return plan(tasks, vms, null);
}

// NEW overload that accepts seed solutions
public Mapping plan(List<Cloudlet> tasks, List<Vm> vms, List<Solution> seedSolutions) {
    convergence.clear();

    // --- build initial food sources ---
    List<Solution> foods = new ArrayList<>();

    // 1) inject GA seeds if provided
    if (seedSolutions != null && !seedSolutions.isEmpty()) {
        for (Solution s : seedSolutions) {
            // copy each seed so we don’t modify the original
            Solution copy = s.copy();
            copy.setScore(obj.objective(new Mapping(tasks, vms, copy), tasks, vms));
            foods.add(copy);
            if (foods.size() >= foodSources) break; // stop if full
        }
    }

    // 2) fill the rest randomly
    while (foods.size() < foodSources) {
        Solution s = Solution.random(tasks.size(), vms.size(), rnd);
        s.setScore(obj.objective(new Mapping(tasks, vms, s), tasks, vms));
        foods.add(s);
    }

    // --- ABC main loop ---
    Solution best = foods.stream()
                         .min(Comparator.comparingDouble(Solution::getScore))
                         .orElse(foods.get(0));
    convergence.add(best.getScore());

    for (int cycle = 0; cycle < cycles; cycle++) {
        // --- Employed bee phase ---
        for (int i = 0; i < foods.size(); i++) {
            Solution candidate = foods.get(i).neighbor(rnd, vms.size());
            candidate.setScore(obj.objective(new Mapping(tasks, vms, candidate), tasks, vms));
            if (candidate.getScore() < foods.get(i).getScore()) {
                foods.set(i, candidate);
            }
        }

        // --- Onlooker phase ---
        foods.sort(Comparator.comparingDouble(Solution::getScore));
        int top = Math.max(1, foods.size() / 2);
        for (int i = 0; i < top; i++) {
            Solution candidate = foods.get(i).neighbor(rnd, vms.size());
            candidate.setScore(obj.objective(new Mapping(tasks, vms, candidate), tasks, vms));
            if (candidate.getScore() < foods.get(i).getScore()) {
                foods.set(i, candidate);
            }
        }

        // --- Scout phase (replace worst with random) ---
        while (foods.size() > foodSources) foods.remove(foods.size() - 1);
        while (foods.size() < foodSources) {
            Solution s = Solution.random(tasks.size(), vms.size(), rnd);
            s.setScore(obj.objective(new Mapping(tasks, vms, s), tasks, vms));
            foods.add(s);
        }

        // update best + convergence
        Solution cycleBest = foods.stream().min(Comparator.comparingDouble(Solution::getScore)).get();
       if (cycleBest.getScore() < best.getScore())
    best = cycleBest.copy();

bestSolutions.add(best.copy());
convergence.add(best.getScore());
    }

    return new Mapping(tasks, vms, best);
}


    private Solution mutate(Solution base, int cycle, int numTasks, int numVMs) {
        Solution cand = base.copy();
        int t = rnd.nextInt(numTasks);

        // adaptive phi: shrinks over cycles
        double phi = (rnd.nextDouble() * 2 - 1) * (1.0 - (cycle / (double) cycles));

        int partnerIdx = rnd.nextInt(foods.size());
        while (foods.get(partnerIdx) == base) {
            partnerIdx = rnd.nextInt(foods.size());
        }
        int newVm = (int) Math.round(
                cand.getAssignment()[t] +
                        phi * (cand.getAssignment()[t] - foods.get(partnerIdx).getAssignment()[t])
        );

        // clip
        newVm = Math.max(0, Math.min(numVMs - 1, newVm));
        cand.getAssignment()[t] = newVm;
        return cand;
    }

    private double[] calcProbabilities() {
        double[] probs = new double[foods.size()];
        double sumFit = foods.stream().mapToDouble(s -> 1.0 / (1.0 + s.getScore())).sum();

        for (int i = 0; i < foods.size(); i++) {
            probs[i] = (1.0 / (1.0 + foods.get(i).getScore())) / sumFit;
        }
        return probs;
    }

    public List<Double> getConvergence() {
        return convergence;
    }
public List<Solution> getBestSolutions() {
    return bestSolutions;
}    
}
