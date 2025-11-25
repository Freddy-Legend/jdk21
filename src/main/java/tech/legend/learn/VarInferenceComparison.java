package tech.legend.learn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 局部变量类型推断（var）对比示例
 * Local Variable Type Inference (var) Comparison Example
 *
 * 版本历史 / Version History:
 * - JDK 10: 引入 var 用于局部变量 / Introduced var for local variables
 * - JDK 11: 允许 var 用于 lambda 形参（便于加注解）/ var in lambda parameters
 */
public class VarInferenceComparison {

    public static void main(String[] args) {
        System.out.println("=== var Inference Comparison (JDK 8 vs JDK 10/11) ===");

        // JDK 8：显式类型
        Map<String, List<Integer>> m1 = new HashMap<>();
        System.out.println("JDK 8 type: " + m1.getClass().getName());

        // JDK 10：var 仅用于局部变量，类型依然由编译器推断（静态类型）
        var m2 = new HashMap<String, List<Integer>>();
        System.out.println("JDK 10 var type: " + m2.getClass().getName());

        // JDK 11：var 可用于 lambda 参数（当需要注解/修饰符时很有用）
        Stream.of("a", "b").map((var s) -> s.toUpperCase()).forEach(System.out::println);
    }
}
