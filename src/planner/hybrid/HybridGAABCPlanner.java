package planner.hybrid;

import eval.Objectives;
import planner.Planner;
import planner.abc.ABCPlanner;
import planner.ga.GAPlanner;
import planner.model.Mapping;
import planner.model.Solution;

import java.util.*;

/**
 * Hybrid GA + ABC planner with local search intensification.
 *
 * Flow:
 * 1. GA for global exploration
 * 2. ABC seeded with GA elites for exploitation
 * 3. Local search on ABC best solution
 */
public class HybridGAABCPlanner implements Planner {
    

    private final GAPlanner ga;
    private final ABCPlanner abc;
    private final Objectives obj;
    private final Random rnd;
    private final List<Double> convergence = new ArrayList<>();
    private final List<Solution> bestSolutions = new ArrayList<>();

    public HybridGAABCPlanner(Objectives obj, long seed) {
        this.obj = obj;
        this.rnd = new Random(seed);

        // GA parameters
        this.ga = new GAPlanner(obj, seed, 60, 40, 0.8, 0.2);

        // ABC parameters
        this.abc = new ABCPlanner(obj, seed, 30, 30, 5);
    }

    @Override
    public String name() {
        return "Hybrid";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mapping plan(final List tasks, final List vms) {

        convergence.clear();
        double bestSoFar = Double.POSITIVE_INFINITY;

        // ---------- 1. GA exploration ----------
        Mapping gaMap = ga.plan(tasks, vms);
        Solution gaBest = new Solution(gaMap, obj, tasks, vms);
        double gaScore = gaBest.getScore();

        bestSoFar = gaScore;
    int i = 0;
for (double val : ga.getConvergence()) {
    bestSoFar = Math.min(bestSoFar, val);
    convergence.add(bestSoFar);

    // store corresponding GA solution
    Solution s = ga.getTopK(1).get(0).copy();
    bestSolutions.add(s);

    i++;
}

        // ---------- 2. ABC exploitation ----------
        List<Solution> seeds = new ArrayList<>();
        seeds.add(gaBest);
        seeds.addAll(ga.getTopK(4));

        Mapping abcMap = abc.plan(tasks, vms, seeds);
        Solution abcBest = new Solution(abcMap, obj, tasks, vms);
        double abcScore = abcBest.getScore();

   for (double val : abc.getConvergence()) {
    bestSoFar = Math.min(bestSoFar, val);
    convergence.add(bestSoFar);

    // store ABC solution snapshot
    Solution s = abcBest.copy();
    bestSolutions.add(s);
}

        // ---------- 3. Local Search Intensification ----------
        Solution lsBest = abcBest.copy();
        double lsScore = abcScore;

        final int LS_ITERS = 50;
        for (int j = 0; j < LS_ITERS; j++) {
            Solution neighbor = lsBest.neighbor(rnd, vms.size());
            Mapping nMap = new Mapping(tasks, vms, neighbor);
            double nScore = obj.objective(nMap, tasks, vms);

            if (nScore < lsScore) {
                lsBest = neighbor;
                lsScore = nScore;
            }
        }

        // ---------- 4. Select Global Best ----------
        Mapping bestMap = gaMap;
        double bestScore = gaScore;

        if (abcScore < bestScore) {
            bestMap = abcMap;
            bestScore = abcScore;
        }

        if (lsScore < bestScore) {
            bestMap = new Mapping(tasks, vms, lsBest);
            bestScore = lsScore;
        }

        return bestMap;
    }

    public List<Double> getConvergence() {
        return convergence;
    }
    public List<Solution> getBestSolutions() {
    return bestSolutions;
}
}
