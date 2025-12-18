package tech.legend.learn;

/**
 * 模式匹配 for switch 增强示例（JDK 21 最终发布）
 * Pattern Matching for switch enhanced examples (JDK 21 Final)
 *
 * 展示：
 * - 类型模式匹配 + 守卫子句（when）
 * - 记录模式（record patterns）与嵌套匹配
 * - null 处理策略
 * - 对比旧式 if-else 的样板代码
 */
public class SwitchComparisonPatternMatching {

    public static void main(String[] args) {
        System.out.println("=== Pattern Matching for switch (JDK 21) ===");

        Object[] samples = {
                "hello",
                42,
                new Point(3, 4),
                new LabeledPoint("P", new Point(1, 2)),
                null
        };

        for (Object o : samples) {
            System.out.println(describe(o));
        }
    }

    // 展示类型模式 + 守卫子句、记录模式的综合用法
    static String describe(Object obj) {
        return switch (obj) {
            case null -> "null value"; // 显式处理 null
            case String s when s.length() > 3 -> "String(>3): " + s.toUpperCase();
            case String s -> "String(<=3): " + s;
            case Integer i when i % 2 == 0 -> "Even int: " + i;
            case Integer i -> "Odd int: " + i;
            case Point(int x, int y) -> "Point x=" + x + ", y=" + y; // 记录模式拆解
            case LabeledPoint(String label, Point(int x, int y)) -> // 嵌套记录模式
                    "LabeledPoint label=" + label + " -> (" + x + "," + y + ")";
            default -> "Other: " + obj;
        };
    }

    // 简单记录类型用于记录模式演示
    record Point(int x, int y) {}

    record LabeledPoint(String label, Point p) {}
}
