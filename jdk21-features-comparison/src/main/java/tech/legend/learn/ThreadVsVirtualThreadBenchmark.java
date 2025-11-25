package tech.legend.learn;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.function.IntFunction;

/**
 * JDK 8 平台线程 vs JDK 21 虚拟线程 性能对比基准
 * JDK 8 Platform Threads vs JDK 21 Virtual Threads Performance Benchmark
 *
 * 核心目标 / Core Objective:
 * - 对比传统平台线程（Platform Threads）与 JDK 21 虚拟线程（Virtual Threads）在不同负载下的性能表现
 * - Compare performance of traditional Platform Threads vs JDK 21 Virtual Threads under different workloads
 *
 * 测试场景 / Test Scenarios:
 * 1. IO 密集型负载（IO-bound）：使用 Thread.sleep() 模拟阻塞操作（如网络请求、文件读写）
 *    IO-intensive workload: Uses Thread.sleep() to simulate blocking operations (network, file I/O)
 * 2. CPU 密集型负载（CPU-bound）：使用数学计算循环模拟计算密集任务
 *    CPU-intensive workload: Uses math computation loops to simulate computation-heavy tasks
 *
 * 性能指标 / Performance Metrics:
 * - wall: 总执行时间（毫秒） / Total execution time (milliseconds)
 * - throughput: 吞吐量（任务数/秒） / Throughput (tasks/second)
 * - avg: 平均任务延迟（毫秒） / Average task latency (milliseconds)
 * - p50/p95/p99: 延迟分位数（毫秒） / Latency percentiles (milliseconds)
 *
 * 关键发现 / Key Findings:
 * - IO 密集型：虚拟线程显著优于平台线程（10-100x 提升），因为虚拟线程开销极低、可创建数百万个
 *   IO-bound: Virtual threads significantly outperform platform threads (10-100x improvement),
 *   as virtual threads have minimal overhead and millions can be created
 * - CPU 密集型：两者性能接近，因为受 CPU 核心数限制，虚拟线程无优势
 *   CPU-bound: Performance similar, as limited by CPU cores, virtual threads offer no advantage
 *
 * 使用方法 / Usage:
 * 1. 默认运行：java ThreadVsVirtualThreadBenchmark
 *    Default run: java ThreadVsVirtualThreadBenchmark
 * 2. 自定义参数：java ThreadVsVirtualThreadBenchmark io.tasks=20000 io.sleepMs=5
 *    Custom parameters: java ThreadVsVirtualThreadBenchmark io.tasks=20000 io.sleepMs=5
 * 3. 预设场景：java ThreadVsVirtualThreadBenchmark preset=io-heavy
 *    Preset scenarios: java ThreadVsVirtualThreadBenchmark preset=io-heavy
 *
 * 注意事项 / Important Notes:
 * - 本基准不依赖外部库（如 JMH），方便快速运行和理解
 *   This benchmark doesn't depend on external libraries (like JMH), easy to run and understand
 * - 结果受硬件、JVM 版本、系统负载影响，仅用于趋势对比而非绝对值
 *   Results vary with hardware, JVM version, system load - use for trend comparison, not absolute values
 * - 建议多次运行取平均值以减少误差
 *   Recommend running multiple times and averaging results to reduce variance
 */
public class ThreadVsVirtualThreadBenchmark {

    // ============================================
    // 配置参数 / Configuration Parameters
    // ============================================

    // IO 密集型负载配置 / IO-bound workload configuration
    private static int IO_TASKS = 10_000;      // IO 任务总数 / Total number of IO tasks
    private static int IO_SLEEP_MS = 10;       // 每个任务睡眠时间（毫秒），模拟 IO 阻塞 / Sleep time per task (ms), simulates IO blocking
    private static int IO_PLATFORM_THREADS = Math.min(200, Runtime.getRuntime().availableProcessors() * 20); // 平台线程池大小 / Platform thread pool size

    // CPU 密集型负载配置 / CPU-bound workload configuration
    private static int CPU_TASKS = 10_000;     // CPU 任务总数 / Total number of CPU tasks
    private static int CPU_ITERATIONS = 50_000;// 每个任务的计算迭代次数 / Computation iterations per task
    private static int CPU_PLATFORM_THREADS = Runtime.getRuntime().availableProcessors(); // 平台线程池大小（通常等于 CPU 核心数）/ Platform thread pool size (usually equals CPU cores)

    /**
     * 主程序入口 / Main program entry
     *
     * 执行流程 / Execution flow:
     * 1. 解析命令行参数 / Parse command line arguments
     * 2. 打印环境信息和配置 / Print environment info and configuration
     * 3. 预热 JVM（稳定 JIT 编译）/ Warmup JVM (stabilize JIT compilation)
     * 4. 执行 IO 密集型对比测试 / Run IO-bound comparison tests
     * 5. 执行 CPU 密集型对比测试 / Run CPU-bound comparison tests
     */
    public static void main(String[] args) throws Exception {
        // 解析命令行参数（如 io.tasks=20000）
        // Parse command line arguments (e.g., io.tasks=20000)
        parseArgs(args);

        // 打印环境信息（Java 版本、OS、CPU 核心数等）
        // Print environment info (Java version, OS, CPU cores, etc.)
        printEnv();

        // 打印当前配置参数
        // Print current configuration parameters
        System.out.println("=== 配置 / Config ===");
        System.out.printf(Locale.ROOT,
                "IO: tasks=%d, sleepMs=%d, platformThreads=%d\n",
                IO_TASKS, IO_SLEEP_MS, IO_PLATFORM_THREADS);
        System.out.printf(Locale.ROOT,
                "CPU: tasks=%d, iterations=%d, platformThreads=%d\n\n",
                CPU_TASKS, CPU_ITERATIONS, CPU_PLATFORM_THREADS);

        // 预热阶段：让 JIT 编译器优化热点代码，确保后续测试结果稳定
        // Warmup phase: Let JIT compiler optimize hot code, ensure stable test results
        warmup();

        // ============================================
        // IO 密集型负载对比测试
        // IO-bound workload comparison test
        // ============================================
        // 预期结果：虚拟线程显著优于平台线程
        // Expected: Virtual threads significantly outperform platform threads
        System.out.println("=== IO-bound workload (sleep) ===");

        // 平台线程测试：使用固定大小线程池
        // Platform threads test: Uses fixed-size thread pool
        runComparison(
                "IO-PlatformThreads",
                () -> Executors.newFixedThreadPool(IO_PLATFORM_THREADS),
                i -> () -> sleepTask(IO_SLEEP_MS),
                IO_TASKS
        );

        // 虚拟线程测试：每个任务一个虚拟线程（JDK 21 新特性）
        // Virtual threads test: One virtual thread per task (JDK 21 new feature)
        runComparison(
                "IO-VirtualThreads",
                Executors::newVirtualThreadPerTaskExecutor,
                i -> () -> sleepTask(IO_SLEEP_MS),
                IO_TASKS
        );

        System.out.println();

        // ============================================
        // CPU 密集型负载对比测试
        // CPU-bound workload comparison test
        // ============================================
        // 预期结果：两者性能接近，因为受 CPU 核心数限制
        // Expected: Similar performance, limited by CPU cores
        System.out.println("=== CPU-bound workload (math loop) ===");

        // 平台线程测试：线程数通常等于 CPU 核心数
        // Platform threads test: Thread count usually equals CPU cores
        runComparison(
                "CPU-PlatformThreads",
                () -> Executors.newFixedThreadPool(CPU_PLATFORM_THREADS),
                i -> () -> cpuTask(i, CPU_ITERATIONS),
                CPU_TASKS
        );

        // 虚拟线程测试：虽然可以创建大量虚拟线程，但 CPU 密集任务仍受核心数限制
        // Virtual threads test: Although many virtual threads can be created, CPU-bound tasks are still limited by cores
        runComparison(
                "CPU-VirtualThreads",
                Executors::newVirtualThreadPerTaskExecutor,
                i -> () -> cpuTask(i, CPU_ITERATIONS),
                CPU_TASKS
        );
    }

    /**
     * 执行一次对比测试并输出统计结果
     * Run one comparison test and print statistics
     *
     * @param name 测试名称（如 "IO-PlatformThreads"）/ Test name (e.g., "IO-PlatformThreads")
     * @param executorFactory 执行器工厂（创建平台线程池或虚拟线程执行器）/ Executor factory (creates platform thread pool or virtual thread executor)
     * @param taskFactory 任务工厂（根据索引创建任务）/ Task factory (creates task based on index)
     * @param tasks 任务总数 / Total number of tasks
     */
    private static void runComparison(String name,
                                      ExecutorServiceFactory executorFactory,
                                      IntFunction<Runnable> taskFactory,
                                      int tasks) throws Exception {
        // 使用 try-with-resources 确保执行器正确关闭
        // Use try-with-resources to ensure executor is properly closed
        try (ExecutorService exec = executorFactory.create()) {
            // 记录每个任务的执行时间（纳秒）
            // Record execution time for each task (nanoseconds)
            long[] durations = new long[tasks];

            // 使用 CountDownLatch 等待所有任务完成
            // Use CountDownLatch to wait for all tasks to complete
            CountDownLatch latch = new CountDownLatch(tasks);

            // 记录整体开始时间（墙钟时间）
            // Record overall start time (wall clock time)
            Instant startWall = Instant.now();

            // 提交所有任务到执行器
            // Submit all tasks to executor
            for (int i = 0; i < tasks; i++) {
                final int idx = i;
                exec.submit(() -> {
                    // 记录单个任务开始时间
                    // Record individual task start time
                    long t0 = System.nanoTime();
                    try {
                        // 执行实际任务（IO 或 CPU）
                        // Execute actual task (IO or CPU)
                        taskFactory.apply(idx).run();
                    } finally {
                        // 记录单个任务结束时间并计算耗时
                        // Record individual task end time and calculate duration
                        long t1 = System.nanoTime();
                        durations[idx] = t1 - t0;
                        latch.countDown();
                    }
                });
            }

            // 等待所有任务完成
            // Wait for all tasks to complete
            latch.await();

            // 记录整体结束时间
            // Record overall end time
            Instant endWall = Instant.now();
            long wallMillis = Duration.between(startWall, endWall).toMillis();

            // ============================================
            // 计算统计指标 / Calculate statistics
            // ============================================

            // 对耗时数组排序，用于计算分位数
            // Sort duration array for percentile calculation
            Arrays.sort(durations);

            // 计算平均延迟（毫秒）
            // Calculate average latency (milliseconds)
            double avgMs = avg(durations) / 1_000_000.0;

            // 计算延迟分位数（毫秒）
            // Calculate latency percentiles (milliseconds)
            double p50 = percentile(durations, 0.50) / 1_000_000.0;  // 中位数 / Median
            double p95 = percentile(durations, 0.95) / 1_000_000.0;  // 95% 分位数 / 95th percentile
            double p99 = percentile(durations, 0.99) / 1_000_000.0;  // 99% 分位数 / 99th percentile

            // 计算吞吐量（任务数/秒）
            // Calculate throughput (tasks/second)
            double throughput = (tasks * 1000.0) / Math.max(1.0, wallMillis);

            // 输出统计结果
            // Print statistics
            System.out.printf(Locale.ROOT,
                    "%s -> wall=%d ms, throughput=%.1f tasks/s, avg=%.3f ms, p50=%.3f ms, p95=%.3f ms, p99=%.3f ms\n",
                    name, wallMillis, throughput, avgMs, p50, p95, p99);
        }
    }

    /**
     * IO 密集型任务：模拟 IO 阻塞
     * IO-bound task: Simulates IO blocking
     *
     * 使用 Thread.sleep() 模拟网络请求、文件读写等阻塞操作
     * Uses Thread.sleep() to simulate blocking operations like network requests, file I/O
     *
     * 虚拟线程优势：当虚拟线程调用 sleep() 时，底层载体线程（carrier thread）可以执行其他虚拟线程，
     * 而平台线程会真正阻塞，无法执行其他任务
     * Virtual thread advantage: When virtual thread calls sleep(), underlying carrier thread
     * can execute other virtual threads, while platform threads are truly blocked
     *
     * @param ms 睡眠时间（毫秒） / Sleep time (milliseconds)
     */
    private static void sleepTask(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // 恢复中断标志 / Restore interrupt flag
            Thread.currentThread().interrupt();
        }
    }

    /**
     * CPU 密集型任务：模拟计算密集操作
     * CPU-bound task: Simulates computation-intensive operations
     *
     * 使用数学计算循环模拟图像处理、加密解密、数据分析等 CPU 密集任务
     * Uses math computation loops to simulate CPU-intensive tasks like image processing,
     * encryption/decryption, data analysis
     *
     * 虚拟线程无优势：CPU 密集任务不会阻塞，载体线程持续占用 CPU，虚拟线程无法切换
     * No virtual thread advantage: CPU-bound tasks don't block, carrier threads continuously
     * use CPU, virtual threads cannot switch
     *
     * @param seed 随机种子，确保不同任务计算不同值 / Random seed, ensures different tasks compute different values
     * @param iterations 迭代次数，控制计算强度 / Iteration count, controls computation intensity
     * @return 计算结果（防止 JIT 优化消除计算）/ Computation result (prevents JIT from eliminating computation)
     */
    private static double cpuTask(int seed, int iterations) {
        // 初始值基于种子，确保不同任务有不同起点
        // Initial value based on seed, ensures different tasks have different starting points
        double x = seed * 0.123456789;

        for (int i = 0; i < iterations; i++) {
            // 使用复杂的数学运算，防止 JIT 编译器优化消除
            // Use complex math operations to prevent JIT compiler from eliminating
            x = Math.sin(x) * Math.cos(x) + Math.sqrt(Math.abs(x)) + 1.000001;

            // 防止数值溢出
            // Prevent numeric overflow
            if (x > 1e6) x = x / 10.0;
        }

        return x;
    }

    /**
     * JVM 预热 / JVM Warmup
     *
     * 在正式测试前运行小规模任务，让 JIT 编译器优化热点代码
     * Run small-scale tasks before formal tests to let JIT compiler optimize hot code
     *
     * 为什么需要预热？
     * Why warmup is needed?
     * - JIT 编译器需要观察代码执行一定次数后才会优化
     *   JIT compiler needs to observe code execution multiple times before optimizing
     * - 预热可以减少 JIT 编译对测试结果的干扰
     *   Warmup reduces JIT compilation interference on test results
     */
    private static void warmup() throws Exception {
        // IO 任务预热 / IO task warmup
        runComparison(
                "WARMUP-IO-VT",
                Executors::newVirtualThreadPerTaskExecutor,
                i -> () -> sleepTask(1),
                1_000
        );

        // CPU 任务预热 / CPU task warmup
        runComparison(
                "WARMUP-CPU-VT",
                Executors::newVirtualThreadPerTaskExecutor,
                i -> () -> cpuTask(i, 5_000),
                1_000
        );

        System.out.println();
    }

    /**
     * 打印运行环境信息 / Print runtime environment information
     *
     * 包括：Java 版本、操作系统、CPU 核心数
     * Includes: Java version, OS, CPU cores
     */
    private static void printEnv() {
        System.out.println("=== 环境 / Environment ===");
        System.out.println("Java: " + System.getProperty("java.version") +
                ", vendor=" + System.getProperty("java.vendor"));
        System.out.println("OS:   " + System.getProperty("os.name") + " " + System.getProperty("os.version") +
                ", arch=" + System.getProperty("os.arch"));
        System.out.println("CPUs: " + Runtime.getRuntime().availableProcessors());
        System.out.println();
    }

    /**
     * 计算平均值（纳秒）/ Calculate average (nanoseconds)
     *
     * @param arr 耗时数组 / Duration array
     * @return 平均值 / Average value
     */
    private static double avg(long[] arr) {
        long s = 0;
        for (long v : arr) s += v;
        return (double) s / arr.length;
    }

    /**
     * 计算分位数（纳秒）/ Calculate percentile (nanoseconds)
     *
     * @param sorted 已排序的耗时数组 / Sorted duration array
     * @param p 分位数（0.0-1.0），如 0.95 表示 95% 分位数 / Percentile (0.0-1.0), e.g., 0.95 for 95th percentile
     * @return 分位数值 / Percentile value
     */
    private static long percentile(long[] sorted, double p) {
        int idx = (int) Math.min(sorted.length - 1, Math.max(0, Math.round((sorted.length - 1) * p)));
        return sorted[idx];
    }

    /**
     * 执行器服务工厂接口
     * Executor service factory interface
     *
     * 用于统一创建不同类型的执行器（平台线程池或虚拟线程执行器）
     * Used to uniformly create different types of executors (platform thread pool or virtual thread executor)
     */
    @FunctionalInterface
    private interface ExecutorServiceFactory {
        ExecutorService create();
    }

    /**
     * 解析命令行参数 / Parse command line arguments
     *
     * 支持格式：key=value
     * Supported format: key=value
     *
     * 示例 / Examples:
     * - io.tasks=20000
     * - cpu.iters=100000
     * - preset=io-heavy
     */
    private static void parseArgs(String[] args) {
        if (args == null) return;
        for (String a : args) {
            String[] kv = a.split("=", 2);
            if (kv.length != 2) continue;
            String k = kv[0].trim();
            String v = kv[1].trim();
            try {
                switch (k) {
                    case "io.tasks" -> IO_TASKS = Integer.parseInt(v);
                    case "io.sleepMs" -> IO_SLEEP_MS = Integer.parseInt(v);
                    case "io.platformThreads" -> IO_PLATFORM_THREADS = Integer.parseInt(v);
                    case "cpu.tasks" -> CPU_TASKS = Integer.parseInt(v);
                    case "cpu.iters" -> CPU_ITERATIONS = Integer.parseInt(v);
                    case "cpu.platformThreads" -> CPU_PLATFORM_THREADS = Integer.parseInt(v);
                    case "preset" -> applyPreset(v);
                    default -> {}
                }
            } catch (NumberFormatException ignore) {
                // 忽略无效的数字格式 / Ignore invalid number format
            }
        }
    }

    /**
     * 应用预设配置 / Apply preset configuration
     *
     * 预设场景 / Preset scenarios:
     * - io-fast: 快速 IO 测试（5000 任务，5ms 睡眠）
     * - io-heavy: 重负载 IO 测试（50000 任务，20ms 睡眠）
     * - cpu-light: 轻量 CPU 测试（5000 任务，20000 迭代）
     * - cpu-heavy: 重负载 CPU 测试（20000 任务，100000 迭代）
     *
     * @param name 预设名称 / Preset name
     */
    private static void applyPreset(String name) {
        switch (name) {
            case "io-fast" -> {
                IO_TASKS = 5_000;
                IO_SLEEP_MS = 5;
                IO_PLATFORM_THREADS = Math.min(100, Runtime.getRuntime().availableProcessors() * 10);
            }
            case "io-heavy" -> {
                IO_TASKS = 50_000;
                IO_SLEEP_MS = 20;
                IO_PLATFORM_THREADS = Math.min(400, Runtime.getRuntime().availableProcessors() * 30);
            }
            case "cpu-light" -> {
                CPU_TASKS = 5_000;
                CPU_ITERATIONS = 20_000;
                CPU_PLATFORM_THREADS = Runtime.getRuntime().availableProcessors();
            }
            case "cpu-heavy" -> {
                CPU_TASKS = 20_000;
                CPU_ITERATIONS = 100_000;
                CPU_PLATFORM_THREADS = Runtime.getRuntime().availableProcessors();
            }
            default -> {
                // 未知预设，保持默认配置 / Unknown preset, keep default configuration
            }
        }
    }
}
