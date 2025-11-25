package com.example.jdk21.virtualthreads;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * JDK 21: Virtual Threads (虚拟线程)
 *
 * Virtual Threads 是 Project Loom 的核心特性，提供轻量级线程
 * 可以创建数百万个虚拟线程而不会耗尽系统资源
 */
@Component
public class VirtualThreadDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 21: Virtual Threads ===");

        // 1. 创建单个虚拟线程
        demonstrateSingleVirtualThread();

        // 2. 比较平台线程 vs 虚拟线程
        compareThreadTypes();

        // 3. 使用虚拟线程执行器
        demonstrateVirtualThreadExecutor();
        
        // 4. Web server simulation
        demonstrateWebServerScenario();
    }

    /**
     * 创建单个虚拟线程
     */
    private void demonstrateSingleVirtualThread() {
        System.out.println("\n1. 创建单个虚拟线程:");

        // 使用 Thread.ofVirtual()
        Thread vThread = Thread.ofVirtual().start(() -> {
            System.out.println("  虚拟线程执行中: " + Thread.currentThread());
        });

        try {
            vThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 使用 Thread.startVirtualThread()
        Thread.startVirtualThread(() -> {
            System.out.println("  另一个虚拟线程: " + Thread.currentThread());
        });

        try {
            Thread.sleep(100); // 等待虚拟线程完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 比较平台线程和虚拟线程的性能
     */
    private void compareThreadTypes() {
        System.out.println("\n2. 平台线程 vs 虚拟线程:");

        int taskCount = 10_000;

        // 平台线程池
        System.out.println("\n  使用平台线程池执行 " + taskCount + " 个任务:");
        Instant start = Instant.now();
        try (ExecutorService platformExecutor = Executors.newFixedThreadPool(100)) {
            IntStream.range(0, taskCount).forEach(i -> {
                platformExecutor.submit(() -> {
                    try {
                        Thread.sleep(1); // 模拟 I/O 操作
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }
        Duration platformDuration = Duration.between(start, Instant.now());
        System.out.println("  平台线程耗时: " + platformDuration.toMillis() + "ms");

        // 虚拟线程池
        System.out.println("\n  使用虚拟线程执行 " + taskCount + " 个任务:");
        start = Instant.now();
        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, taskCount).forEach(i -> {
                virtualExecutor.submit(() -> {
                    try {
                        Thread.sleep(1); // 模拟 I/O 操作
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }
        Duration virtualDuration = Duration.between(start, Instant.now());
        System.out.println("  虚拟线程耗时: " + virtualDuration.toMillis() + "ms");

        if (virtualDuration.toMillis() > 0) {
            System.out.println("\n  性能提升: " +
                String.format("%.2f", (double) platformDuration.toMillis() / virtualDuration.toMillis()) + "x");
        } else {
            System.out.println("\n  性能提升: 虚拟线程执行时间太短，无法准确计算性能提升");
        }
    }

    /**
     * 使用虚拟线程执行器
     */
    private void demonstrateVirtualThreadExecutor() {
        System.out.println("\n3. 虚拟线程执行器:");

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 提交多个任务
            for (int i = 0; i < 5; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    System.out.println("  任务 " + taskId + " 在虚拟线程中执行: " +
                        Thread.currentThread());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("  任务 " + taskId + " 完成");
                });
            }
        } // executor 自动关闭并等待所有任务完成

        System.out.println("  所有任务已完成");
    }
    
    /**
     * Web server scenario simulation
     */
    private void demonstrateWebServerScenario() {
        System.out.println("\n4. Web Server Simulation:");
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Simulate handling multiple concurrent requests
            for (int i = 0; i < 100; i++) {
                final int requestId = i;
                executor.submit(() -> {
                    // Simulate processing time
                    try {
                        Thread.sleep((long) (Math.random() * 100));
                        System.out.println("  Processed request #" + requestId + 
                                         " on thread: " + Thread.currentThread());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
        
        try {
            Thread.sleep(500); // Wait for completion
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("  All requests processed!");
    }

    /**
     * Spring Boot 中使用虚拟线程
     */
    public void demonstrateWithSpring() {
        System.out.println("\n=== Spring Boot 中使用虚拟线程 ===");

        System.out.println("""
            在 Spring Boot 3.2+ 中启用虚拟线程:

            1. application.properties 配置:
               spring.threads.virtual.enabled=true

            2. 所有 @Async 方法将自动使用虚拟线程

            3. Web 请求处理将使用虚拟线程（Tomcat/Jetty）

            4. 手动配置 TaskExecutor:
               @Bean
               public AsyncTaskExecutor applicationTaskExecutor() {
                   return new TaskExecutorAdapter(
                       Executors.newVirtualThreadPerTaskExecutor()
                   );
               }
            """);
    }
}