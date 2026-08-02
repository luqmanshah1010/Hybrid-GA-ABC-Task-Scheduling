# A Hybrid GA-ABC Optimization Model for Load Balancing in Cloud Computing"

## Overview

This repository contains the implementation of the **Hybrid Genetic Algorithm–Artificial Bee Colony (GA-ABC)** task scheduling algorithm developed for load balancing in cloud computing using **CloudSim Plus**.

The proposed hybrid scheduler optimizes cloud task allocation by improving scheduling performance across different workload profiles. The implementation was developed as part of a research study on multi-objective cloud task scheduling and includes all source code, configuration files, datasets, and simulation outputs required to reproduce the experimental results.

---

## Features

- Hybrid GA-ABC task scheduling algorithm
- CloudSim Plus simulation environment
- Cloud task scheduling
- Multiple workload profiles
- Automatic Graphs Generation
- Automatic CSV result generation
- Convergence graphs generation
- Convergence data generation
- Reproducible simulation experiments

---

## Development Environment

| Component | Version |
|-----------|----------|
| IDE | Apache NetBeans 21 |
| Java | JDK 21 |
| Simulation Framework | CloudSim Plus 5.0.0 |
| Chart Library | XChart 3.8.6 |
| Logging | slf4j-simple 2.0.7 |
| Logging API | slf4j-api 2.0.7 |

---

## Required Libraries

Before running the project, ensure the following libraries are available in your project.

- cloudsim-plus-5.0.0.jar
- xchart-3.8.6.jar
- slf4j-simple-2.0.7.jar
- slf4j-api-2.0.7.jar

---

## Workload Profiles

The project supports **three different workload profiles**.

| Profile | Description |
|----------|-------------|
| Web | Web application workload |
| Analytics | Data analytics workload |
| Mixed | Mixed heterogeneous workload |

### Selecting a Workload

Before running the simulation, open the **configuration file** and change the workload profile.

Example:

For the **Mixed** workload:

```
profile = mixed
```

For the **Analytics** workload:

```
profile = analytics
```

For the **Web** workload:

```
profile = web
```

Save the configuration file and execute the simulation.

---

## Running the Project

1. Open the project using **Apache NetBeans 21**.
2. Make sure **JDK 21** is selected.
3. Add all required JAR libraries.
4. Select the desired workload profile in the configuration file.
5. Run the main project.

After execution, simulation results will automatically be generated.

---

## Generated Output

The simulation automatically generates:

- Comparison and single fitness graphs for all performance metrics
- CSV files containing raw experimental results
- Performance metric values
- Iteration-wise convergence data


---

## Convergence Graphs

Some convergence graphs presented in the associated research paper are **not generated directly by the Java project**.

Instead:

1. Run the simulation.
2. Obtain the generated CSV files.
3. Import the CSV files into Microsoft Excel.
4. Generate the convergence plots using Excel charts.

---

## Consolidated Result Tables

The consolidated performance tables reported in the research paper were created by post-processing the generated CSV files using Microsoft Excel.

The Java project produces the raw simulation data, while Excel was used for formatting and preparing the final tables for publication.

---

## Performance Metrics

The implementation evaluates several scheduling performance metrics, including:

- Makespan
- Energy Consumption
- Average Resource Utilization
- Average Response Time
- Load Imbalance
- Single Fitness Value for all performance metrics

Additional metrics may also be available depending on the selected experiment.

---

## Reproducibility

To reproduce the experimental results reported in the paper:

1. Use the specified software versions.
2. Select the required workload profile.
3. Execute the simulation.
4. Use the generated CSV files for analysis.
5. Generate convergence plots and consolidated tables in Microsoft Excel.

---

## Citation

If you use this repository in your research, please cite the associated publication.

```
Author: Syed Luqman Shah

Title:
A Hybrid Optimization Model for Load Balancing in Cloud Computing

Year: 2026
```

---

## License

This repository is provided for **research and academic purposes**.

If you use this code in your research, please cite the associated publication.

---

## Contact

**Syed Luqman Shah**


Research Interests

- Cloud Computing
- Metaheuristic Optimization
- Task Scheduling
- Multi-objective Optimization
- Energy-Efficient Computing

GitHub:
https://github.com/luqmanshah1010

---

## Acknowledgements

This implementation was developed as part of doctoral research on intelligent cloud task scheduling using hybrid metaheuristic optimization algorithms. The project utilizes CloudSim Plus to simulate cloud computing environments and evaluate scheduling performance under different workload scenarios.