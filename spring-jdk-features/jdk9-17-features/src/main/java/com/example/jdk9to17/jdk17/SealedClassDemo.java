package com.example.jdk9to17.jdk17;

import org.springframework.stereotype.Component;

/**
 * JDK 17: 密封类（Sealed Classes）
 *
 * 密封类可以限制哪些类可以继承它，提供更好的类型控制
 */
@Component
public class SealedClassDemo {

    // 定义密封接口
    public sealed interface Payment permits CreditCardPayment, AlipayPayment, WeChatPayment {
        String processPayment(double amount);
    }

    // 允许的实现类
    public final class CreditCardPayment implements Payment {
        private final String cardNumber;

        public CreditCardPayment(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        @Override
        public String processPayment(double amount) {
            return "Credit Card payment: $" + amount + " (Card: ****" +
                cardNumber.substring(cardNumber.length() - 4) + ")";
        }
    }

    public final class AlipayPayment implements Payment {
        private final String account;

        public AlipayPayment(String account) {
            this.account = account;
        }

        @Override
        public String processPayment(double amount) {
            return "Alipay payment: ¥" + amount + " (Account: " + account + ")";
        }
    }

    public final class WeChatPayment implements Payment {
        private final String openId;

        public WeChatPayment(String openId) {
            this.openId = openId;
        }

        @Override
        public String processPayment(double amount) {
            return "WeChat payment: ¥" + amount + " (OpenID: " + openId + ")";
        }
    }

    // 密封抽象类示例
    public sealed abstract class Vehicle permits Car, Truck, Motorcycle {
        protected String brand;

        public Vehicle(String brand) {
            this.brand = brand;
        }

        public abstract String getType();
    }

    public final class Car extends Vehicle {
        public Car(String brand) {
            super(brand);
        }

        @Override
        public String getType() {
            return "Car: " + brand;
        }
    }

    public final class Truck extends Vehicle {
        public Truck(String brand) {
            super(brand);
        }

        @Override
        public String getType() {
            return "Truck: " + brand;
        }
    }

    public non-sealed class Motorcycle extends Vehicle {
        public Motorcycle(String brand) {
            super(brand);
        }

        @Override
        public String getType() {
            return "Motorcycle: " + brand;
        }
    }

    public void demonstrate() {
        System.out.println("\n=== JDK 17: 密封类 ===");

        // 支付示例
        var payments = java.util.List.of(
            new CreditCardPayment("1234567890123456"),
            new AlipayPayment("zhangsan@alipay.com"),
            new WeChatPayment("wx_123456")
        );

        System.out.println("支付处理:");
        for (Payment payment : payments) {
            String result = payment.processPayment(100.0);
            System.out.println("  - " + result);
        }

        // 车辆示例
        var vehicles = java.util.List.of(
            new Car("Tesla"),
            new Truck("Volvo"),
            new Motorcycle("Harley-Davidson")
        );

        System.out.println("\n车辆类型:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("  - " + vehicle.getType());
        }

        // 使用模式匹配（结合 JDK 17 特性）
        demonstrateWithPatternMatching();
    }

    /**
     * 密封类与模式匹配结合使用
     */
    public void demonstrateWithPatternMatching() {
        System.out.println("\n=== 密封类 + 模式匹配 ===");

        Payment payment = new AlipayPayment("test@alipay.com");

        // 使用 instanceof 模式匹配
        String description = getPaymentDescription(payment);
        System.out.println(description);

        Vehicle vehicle = new Car("BMW");
        String vehicleInfo = getVehicleInfo(vehicle);
        System.out.println(vehicleInfo);
    }

    private String getPaymentDescription(Payment payment) {
        if (payment instanceof CreditCardPayment cc) {
            return "信用卡支付: " + cc.processPayment(100.0);
        } else if (payment instanceof AlipayPayment alipay) {
            return "支付宝支付: " + alipay.processPayment(100.0);
        } else if (payment instanceof WeChatPayment wechat) {
            return "微信支付: " + wechat.processPayment(100.0);
        }
        return "未知支付方式";
    }

    private String getVehicleInfo(Vehicle vehicle) {
        return switch (vehicle) {
            case Car c -> "轿车: " + c.brand;
            case Truck t -> "卡车: " + t.brand;
            case Motorcycle m -> "摩托车: " + m.brand;
        };
    }
}
