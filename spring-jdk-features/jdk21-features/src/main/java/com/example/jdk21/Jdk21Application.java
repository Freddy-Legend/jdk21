package com.example.jdk21;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * JDK 21 新特性演示应用
 * <p>
 * 重点特性：
 * 1. Virtual Threads (虚拟线程) - Project Loom
 * 2. Pattern Matching for switch
 * 3. Record Patterns
 * 4. Sequenced Collections
 * 5. String Templates (Preview)
 */
@SpringBootApplication
public class Jdk21Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Jdk21Application.class, args);

        System.out.println("=================================");
        System.out.println("JDK 21 特性演示应用已启动");
        System.out.println("JDK 版本: " + Runtime.version());
        System.out.println("=================================");

        // Run String Templates demo
        context.getBean(com.example.jdk21.stringtemplates.StringTemplatesDemo.class).demonstrate();

        // Run Record Patterns demo
        context.getBean(com.example.jdk21.recordpatterns.RecordPatternsDemo.class).demonstrate();


        context.getBean(com.example.jdk21.sequenced.SequencedCollectionDemo.class).demonstrate();

        context.getBean(com.example.jdk21.virtualthreads.VirtualThreadDemo.class).demonstrate();

        context.getBean(com.example.jdk21.patternmatching.PatternMatchingDemo.class).demonstrate();

        System.exit(0);

    }
}