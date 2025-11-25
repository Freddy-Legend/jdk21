package tech.legend.learn;

import java.util.*;

/**
 * 集合工厂方法对比示例
 * Collection Factory Methods Comparison Example
 *
 * 主要展示 JDK 9 引入的 List.of/Set.of/Map.of/Map.ofEntries 与 JDK 8 旧写法的差异
 * Demonstrates JDK 9 List.of/Set.of/Map.of/Map.ofEntries vs JDK 8 older patterns
 *
 * 版本历史 / Version History:
 * - JDK 9: List/Set/Map 工厂方法（不可变集合）/ Collection factory methods (immutable)
 */
@SuppressWarnings("unused")
public class CollectionFactoriesComparison {

    public static void main(String[] args) {
        System.out.println("=== Collection Factories Comparison (JDK 8 vs JDK 9) ===");

        // JDK 8：常见旧写法（可变集合）
        // JDK 8: Verbose, creates mutable collections
        List<String> oldList = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Set<String> oldSet = new HashSet<>(Arrays.asList("x", "y", "z"));
        Map<String, Integer> oldMap = new HashMap<>();
        oldMap.put("k1", 1);
        oldMap.put("k2", 2);

        System.out.println("JDK 8 List mutable? " + isMutable(oldList));
        System.out.println("JDK 8 Set mutable?  " + isMutable(oldSet));
        System.out.println("JDK 8 Map mutable?  " + isMutableMap(oldMap));

        // JDK 9：不可变集合工厂方法
        // JDK 9: Immutable collection factories
        List<String> newList = List.of("a", "b", "c");
        Set<String> newSet = Set.of("x", "y", "z");
        Map<String, Integer> newMap = Map.of("k1", 1, "k2", 2);
        Map<String, Integer> newMap2 = Map.ofEntries(
                Map.entry("k1", 1),
                Map.entry("k2", 2),
                Map.entry("k3", 3)
        );

        System.out.println("JDK 9 List mutable? " + isMutable(newList));
        System.out.println("JDK 9 Set mutable?  " + isMutable(newSet));
        System.out.println("JDK 9 Map mutable?  " + isImmutableMap(newMap));
        System.out.println("JDK 9 Map2 mutable? " + isImmutableMap(newMap2));
    }

    private static boolean isMutable(Collection<?> c) {
        try { c.add(null); return true; } catch (UnsupportedOperationException e) { return false; }
    }

    private static boolean isMutableMap(Map<String, Integer> m) {
        try { 
            m.put("testKey", 1); 
            return true; // 如果能成功添加，则是可变的
        } catch (UnsupportedOperationException e) { 
            return false; // 如果抛出异常，则是不可变的
        }
    }

    private static boolean isImmutableMap(Map<String, Integer> m) {
        return !isMutableMap(m);
    }
}