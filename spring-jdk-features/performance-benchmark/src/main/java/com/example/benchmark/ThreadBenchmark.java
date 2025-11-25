package com.example.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 性能基准测试：平台线程 vs 虚拟线程
 *
 * <p>此基准测试用于比较 Java 传统平台线程和 JDK 21 引入的虚拟线程在处理大量 I/O 密集型任务时的性能差异。
 *
 * <p>注解说明：
 * <ul>
 *   <li>@BenchmarkMode(Mode.AverageTime) - 测量平均执行时间</li>
 *   <li>@OutputTimeUnit(TimeUnit.MILLISECONDS) - 结果以毫秒为单位输出</li>
 *   <li>@State(Scope.Benchmark) - 所有线程共享同一个实例</li>
 *   <li>@Fork(1) - 使用单独的 JVM 进程运行测试</li>
 *   <li>@Warmup(iterations = 2, time = 3) - 预热 2 轮，每轮 3 秒</li>
 *   <li>@Measurement(iterations = 3, time = 5) - 正式测试 3 轮，每轮 5 秒</li>
 * </ul>
 *
 * @see <a href="https://openjdk.org/jeps/444">JEP 444: Virtual Threads</a>
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 3, time = 5)
public class ThreadBenchmark {

    /**
     * 任务数量：每次基准测试执行 10,000 个任务
     * 用于模拟高并发场景下的线程处理能力
     */
    private static final int TASK_COUNT = 10_000;

    /**
     * 使用传统平台线程执行任务的基准测试方法
     *
     * <p>创建一个固定大小为 100 的线程池来执行 10,000 个任务。
     * 由于线程池大小的限制，任务会在队列中等待可用线程。
     *
     * <p>特点：
     * <ul>
     *   <li>使用操作系统原生线程（重量级）</li>
     *   <li>线程数量受限于系统资源</li>
     *   <li>线程创建和销毁成本高</li>
     *   <li>适合 CPU 密集型任务</li>
     * </ul>
     *
     * @see Executors#newFixedThreadPool(int)
     */
    @Benchmark
    @Threads(1)
    public void platformThreads() {
        try (ExecutorService executor = Executors.newFixedThreadPool(100)) {
            IntStream.range(0, TASK_COUNT).forEach(i -> {
                executor.submit(() -> {
                    try {
                        // 模拟 I/O 操作（如数据库查询、网络请求等）
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // 恢复中断状态，确保线程能够正确响应中断信号
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }
    }

    /**
     * 使用虚拟线程执行任务的基准测试方法
     *
     * <p>为每个任务创建一个新的虚拟线程，能够轻松处理大量并发任务。
     * 虚拟线程是 JDK 21 引入的轻量级线程实现（Project Loom）。
     *
     * <p>特点：
     * <ul>
     *   <li>由 JVM 管理的轻量级线程（用户态线程）</li>
     *   <li>可以创建数百万个虚拟线程而不耗尽系统资源</li>
     *   <li>自动进行线程挂起和恢复，无需手动管理线程池</li>
     *   <li>非常适合 I/O 密集型任务</li>
     *   <li>在阻塞操作时，虚拟线程会被自动卸载，底层平台线程可以执行其他虚拟线程</li>
     * </ul>
     *
     * <p>性能优势：在 I/O 密集型场景下，虚拟线程通常比平台线程表现更好，
     * 因为可以创建更多线程而不增加太多开销。
     *
     * @see Executors#newVirtualThreadPerTaskExecutor()
     */
    @Benchmark
    @Threads(1)
    public void virtualThreads() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, TASK_COUNT).forEach(i -> {
                executor.submit(() -> {
                    try {
                        // 模拟 I/O 操作（如数据库查询、网络请求等）
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // 恢复中断状态，确保线程能够正确响应中断信号
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }
    }

    /**
     * 基准测试的主入口方法
     *
     * <p>配置并运行 JMH 基准测试，测试结果将输出到控制台。
     *
     * <p>运行方式：
     * <pre>{@code
     * mvn clean install
     * java -jar target/benchmarks.jar
     * }</pre>
     *
     * 或者直接运行此 main 方法。
     *
     * @param args 命令行参数（未使用）
     * @throws RunnerException 当基准测试运行失败时抛出
     */
    public static void main(String[] args) throws RunnerException {
        // 配置基准测试选项
        Options opt = new OptionsBuilder()
                .include(ThreadBenchmark.class.getSimpleName())  // 包含当前类的所有基准测试方法
                .build();

        // 启动基准测试
        new Runner(opt).run();
    }
}