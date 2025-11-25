package com.example.jdk9to17.jdk10;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDK 10: 局部变量类型推断 (var)
 *
 * 使用 var 关键字简化局部变量声明
 */
@Component
public class VarKeywordDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 10: var 局部变量类型推断 ===");

        // 基本类型推断
        var message = "Hello Spring Boot";
        System.out.println("var String: " + message);

        var number = 42;
        System.out.println("var int: " + number);

        // 集合类型推断
        var list = new ArrayList<String>();
        list.add("Spring");
        list.add("Boot");
        System.out.println("var List: " + list);

        var map = new HashMap<String, Integer>();
        map.put("Java", 21);
        map.put("Spring", 6);
        System.out.println("var Map: " + map);

        // 在 for 循环中使用
        for (var item : list) {
            System.out.println("  - " + item);
        }

        // 在流式操作中使用
        var result = List.of(1, 2, 3, 4, 5)
            .stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * 2)
            .toList();
        System.out.println("var with Stream: " + result);
    }
}
