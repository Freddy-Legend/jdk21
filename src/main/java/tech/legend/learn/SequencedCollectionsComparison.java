package tech.legend.learn;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequenced Collections 功能对比示例
 * Sequenced Collections Feature Comparison Example
 * <p>
 * 本类演示 JDK 21 中引入的 Sequenced Collections 新特性，对比传统方式与新方式的差异
 * This class demonstrates the new Sequenced Collections feature introduced in JDK 21,
 * comparing traditional approaches with new methods
 * <p>
 * 主要改进：
 * Key Improvements:
 * 1. 提供统一的 API 访问集合的首尾元素 (getFirst/getLast)
 * Unified API to access first and last elements of collections
 * 2. 代码更简洁、可读性更强
 * More concise and readable code
 * 3. 避免索引越界等常见错误
 * Prevents common errors like index out of bounds
 */
public class SequencedCollectionsComparison {
    public static void main(String[] args) {
        System.out.println("=== Sequenced Collections Comparison ===");

        // 创建示例列表并添加数据
        // Create sample list and add data
        List<String> list = new ArrayList<>();
        list.add("First");
        list.add("Middle");
        list.add("Last");

        // JDK 8 及更早版本：冗长的访问方式
        // JDK 8 and earlier: Verbose access pattern
        // 缺点：需要手动计算索引，容易出错（如 size() - 1）
        // Drawback: Manual index calculation, error-prone (e.g., size() - 1)
        String firstOld = list.get(0);
        String lastOld = list.get(list.size() - 1);
        System.out.println("JDK 8: First=" + firstOld + ", Last=" + lastOld);

        // JDK 21 新特性：Sequenced Collections
        // JDK 21 new feature: Sequenced Collections
        // 优点：语义清晰、代码简洁、更安全
        // Advantages: Clear semantics, concise code, safer
        // getFirst() - 获取第一个元素 / Get first element
        // getLast() - 获取最后一个元素 / Get last element
        String firstNew = list.getFirst();
        String lastNew = list.getLast();
        System.out.println("JDK 21: First=" + firstNew + ", Last=" + lastNew);
    }
}
