package planner.ga;

import eval.Objectives;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import planner.Planner;
import planner.model.Mapping;
import planner.model.Solution;

import java.util.*;

/**
 * Simple GA Planner with seeding support.
 */
public class GAPlanner implements Planner {
    private final Objectives obj;
    private final Random rnd;
private final List<Solution> bestSolutions = new ArrayList<>();
    private final int populationSize;
    private final int generations;
    private final double crossoverRate;
    private final double mutationRate;

    private List<Solution> pop = new ArrayList<>();
    private Solution best;
    private final List<Double> convergence = new ArrayList<>();

    // 👇 NEW: seeds from Hybrid
    private List<Solution> seedSolutions = new ArrayList<>();

    public GAPlanner(Objectives obj, long seed) {
        this(obj, seed, 40, 50, 0.8, 0.2);
    }

    public GAPlanner(Objectives obj, long seed, int populationSize, int generations,
                     double crossoverRate, double mutationRate) {
        this.obj = obj;
        this.rnd = new Random(seed);
        this.populationSize = populationSize;
        this.generations = generations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    // 👇 allow Hybrid to inject seeds
    public void injectSeeds(List<Solution> seeds) {
        this.seedSolutions = (seeds == null) ? new ArrayList<>() : new ArrayList<>(seeds);
    }

    @Override
    public String name() {
        return "GA";
    }

    /*@Override
    public Mapping plan(List<Cloudlet> tasks, List<Vm> vms) {
        initPopulation(tasks, vms);

        for (int gen = 0; gen < generations; gen++) {
            step(tasks, vms);
        }

        return new Mapping(tasks, vms, best);
    }*/
    
    @Override
public Mapping plan(List<Cloudlet> tasks, List<Vm> vms) {
    return plan(tasks, vms, null); // delegate to new overload
}


public Mapping plan(List<Cloudlet> tasks, List<Vm> vms, List<Solution> seedSolutions) {
    pop.clear();

    // --- build initial random population ---
    for (int i = 0; i < populationSize; i++) {
        Solution s = Solution.random(tasks.size(), vms.size(), rnd);
        double score = obj.objective(new Mapping(tasks, vms, s), tasks, vms);
        s.setScore(score);
        pop.add(s);
    }

    // --- inject seeds if provided ---
    if (seedSolutions != null && !seedSolutions.isEmpty()) {
        int i = 0;
        for (Solution s : seedSolutions) {
            if (i >= pop.size()) break;
            pop.set(i++, s.copy());
        }
    }

    // --- initial best ---
    best = pop.stream().min(Comparator.comparingDouble(Solution::getScore)).orElse(pop.get(0));
    convergence.add(best.getScore());

    // --- GA loop ---
    for (int gen = 0; gen < generations; gen++) {
        List<Solution> next = new ArrayList<>();
        next.add(best.copy()); // elitism

        while (next.size() < populationSize) {
            Solution p1 = selectTournament();
            Solution p2 = selectTournament();
            Solution child = p1.crossover(p2, rnd).mutate(rnd, vms.size());

            double score = obj.objective(new Mapping(tasks, vms, child), tasks, vms);
            child.setScore(score);
            next.add(child);
        }

        pop = next;

        Solution genBest = pop.stream().min(Comparator.comparingDouble(Solution::getScore)).get();
    if (genBest.getScore() < best.getScore())
    best = genBest.copy();

bestSolutions.add(best.copy());
convergence.add(best.getScore());
    }

    return new Mapping(tasks, vms, best);
}

    private void initPopulation(List<Cloudlet> tasks, List<Vm> vms) {
        pop.clear();
        for (int i = 0; i < populationSize; i++) {
            Solution s = Solution.random(tasks.size(), vms.size(), rnd);
            double score = obj.objective(new Mapping(tasks, vms, s), tasks, vms);
            s.setScore(score);
            pop.add(s);
        }

        // 👇 inject seeds (overwrite first few individuals)
        if (seedSolutions != null && !seedSolutions.isEmpty()) {
            int i = 0;
            for (Solution s : seedSolutions) {
                if (i >= pop.size()) break;
                pop.set(i++, s.copy());
            }
        }

        best = pop.stream().min(Comparator.comparingDouble(Solution::getScore)).get();
        convergence.add(best.getScore());
    }

    private void step(List<Cloudlet> tasks, List<Vm> vms) {
        List<Solution> next = new ArrayList<>();

        // Elitism
        next.add(best.copy());

        while (next.size() < populationSize) {
            Solution p1 = pop.get(rnd.nextInt(pop.size()));
            Solution p2 = pop.get(rnd.nextInt(pop.size()));
            Solution child;

            if (rnd.nextDouble() < crossoverRate) {
                child = p1.crossover(p2, rnd);
            } else {
                child = p1.copy();
            }

            if (rnd.nextDouble() < mutationRate) {
                child.mutate(rnd, vms.size());
            }

            double score = obj.objective(new Mapping(tasks, vms, child), tasks, vms);
            child.setScore(score);
            next.add(child);
        }

        pop = next;
        Solution genBest = pop.stream().min(Comparator.comparingDouble(Solution::getScore)).get();
        if (genBest.getScore() < best.getScore()) {
            best = genBest.copy();
        }
        convergence.add(best.getScore());
    }

    public List<Double> getConvergence() {
        return convergence;
    }

    public Solution getBest() {
        return best;
    }
    /** Tournament selection to pick parents */
private Solution selectTournament() {
    int tsize = 3; // tournament size
    Solution best = null;
    for (int i = 0; i < tsize; i++) {
        Solution cand = pop.get(rnd.nextInt(pop.size()));
        if (best == null || cand.getScore() < best.getScore()) {
            best = cand;
        }
    }
    return best.copy();
}
// --- GAPlanner.java ---
/** Return a copy of the best K individuals in the current population (sorted by score). */
public List<Solution> getTopK(int k) {
    return pop.stream()
              .sorted(Comparator.comparingDouble(Solution::getScore))
              .limit(k)
              .map(Solution::copy)
              .toList();
}
public List<Solution> getBestSolutions() {
    return bestSolutions;
}


}
