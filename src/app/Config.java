package app;

public class Config {

    // --- Profile Selector ---
    //WEB, MIXED< ANALYTICS
    public static final WorkloadProfile PROFILE = WorkloadProfile.ANALYTICS;

    // --- Shared Parameters ---
    public static final int TASK_PES = 1;
    public static final long SEED = 42L;

    // Parameters that can change by profile
    public static int NUM_PMs;
    public static int NUM_VMs;
    public static int NUM_TASKS;

    public static int PM_CORES;
    public static int PM_MIPS_PER_CORE;
    public static int PM_BW_MBPS;

    public static int VM_CORES;
    public static int VM_MIPS_PER_CORE;
    public static int VM_BW_MBPS;
    public static int VM_RAM_MB;

    public static int TASK_MI_MIN;
    public static int TASK_MI_MAX;
    public static int TASK_SIZE_MB_MIN;
    public static int TASK_SIZE_MB_MAX;

    // Migration & Swap factors
    public static double ALPHA_SWAP;
    public static double T_MIG_SEC;

    // --- Apply profile settings ---
    public static void applyProfile() {
        switch (PROFILE) {
            case WEB:
                NUM_PMs = 4;
                NUM_VMs = 16;
                NUM_TASKS = 400;

                PM_CORES = 8; PM_MIPS_PER_CORE = 6000; PM_BW_MBPS = 800;
                VM_CORES = 2; VM_MIPS_PER_CORE = 4000; VM_BW_MBPS = 200; VM_RAM_MB = 4096;

                TASK_MI_MIN = 500; TASK_MI_MAX = 20_000;
                TASK_SIZE_MB_MIN = 1; TASK_SIZE_MB_MAX = 10;

                ALPHA_SWAP = 0.2; T_MIG_SEC = 0.02;
                break;

            case ANALYTICS:
                NUM_PMs = 8;
                NUM_VMs = 32;
                NUM_TASKS = 300;

                PM_CORES = 16; PM_MIPS_PER_CORE = 7000; PM_BW_MBPS = 1200;
                VM_CORES = 4; VM_MIPS_PER_CORE = 5000; VM_BW_MBPS = 400; VM_RAM_MB = 8192;

                TASK_MI_MIN = 50_000; TASK_MI_MAX = 2_000_000;
                TASK_SIZE_MB_MIN = 50; TASK_SIZE_MB_MAX = 1000;

                ALPHA_SWAP = 0.6; T_MIG_SEC = 0.15;
                break;

            case MIXED:
                NUM_PMs = 6;
                NUM_VMs = 24;
                NUM_TASKS = 500;

                PM_CORES = 12; PM_MIPS_PER_CORE = 6500; PM_BW_MBPS = 1000;
                VM_CORES = 2; VM_MIPS_PER_CORE = 4500; VM_BW_MBPS = 300; VM_RAM_MB = 6144;

                TASK_MI_MIN = 1_000; TASK_MI_MAX = 1_000_000;
                TASK_SIZE_MB_MIN = 2; TASK_SIZE_MB_MAX = 500;

                ALPHA_SWAP = 0.4; T_MIG_SEC = 0.08;
                break;
        }
    }
}
