package tech.legend.learn;

/**
 * Sealed Classes/Interfaces（密封类/接口）对比示例（JDK 17）
 * Sealed Classes/Interfaces comparison example (JDK 17)
 *
 * 目标：
 * - 展示 sealed/permits 的受限继承模型，避免未授权的扩展
 * - 对比传统开放层次的风险，演示 switch 的穷尽性（exhaustiveness）检查
 */
public class SealedClassesComparison {

    public static void main(String[] args) {
        System.out.println("=== Sealed Classes/Interfaces Comparison (JDK 17) ===");

        Shape s1 = new Circle(2.0);
        Shape s2 = new Rectangle(2.0, 3.0);

        // 使用 switch 表达式对密封层次进行穷尽匹配
        double area1 = area(s1);
        double area2 = area(s2);
        System.out.println("area(circle) = " + area1);
        System.out.println("area(rectangle) = " + area2);

        // 若尝试新增未许可的子类（如 class Triangle implements Shape），编译器将报错
        // If you try to add a non-permitted subclass, compilation will fail
    }

    // 对密封层次进行穷尽 switch，若遗漏某个 permitted 子类将导致编译警告/错误
    private static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
        };
    }

    // 定义密封接口，仅允许列出的子类型实现
    sealed interface Shape permits Circle, Rectangle {}

    // 具体实现可以是 final、sealed、或 non-sealed
    record Circle(double radius) implements Shape {}

    record Rectangle(double width, double height) implements Shape {}
}
