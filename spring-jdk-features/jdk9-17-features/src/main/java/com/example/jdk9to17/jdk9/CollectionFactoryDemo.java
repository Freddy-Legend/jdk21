package com.example.jdk9to17.jdk9;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JDK 9: 集合工厂方法
 *
 * List.of(), Set.of(), Map.of() 创建不可变集合
 */
@Component
public class CollectionFactoryDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 9: 集合工厂方法 ===");

        // 不可变 List
        List<String> list = List.of("Spring", "Boot", "Cloud");
        System.out.println("List.of(): " + list);

        // 不可变 Set
        Set<Integer> set = Set.of(1, 2, 3, 4, 5);
        System.out.println("Set.of(): " + set);

        // 不可变 Map
        Map<String, Integer> map = Map.of(
            "Java", 21,
            "Spring", 6,
            "Boot", 3
        );
        System.out.println("Map.of(): " + map);

        // Map.ofEntries 用于超过 10 个键值对
        Map<String, String> largeMap = Map.ofEntries(
            Map.entry("feature1", "Collection Factory"),
            Map.entry("feature2", "Private Interface Methods"),
            Map.entry("feature3", "Stream API Enhancements")
        );
        System.out.println("Map.ofEntries(): " + largeMap);
    }
}
