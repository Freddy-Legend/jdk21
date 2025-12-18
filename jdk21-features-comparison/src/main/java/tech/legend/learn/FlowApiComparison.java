package tech.legend.learn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

/**
 * java.util.concurrent.Flow 使用示例与对比
 * Flow API Usage and Comparison Example
 * <p>
 * 说明 / Notes:
 * - Flow API（发布者/订阅者/处理器）在 Java 9 引入，但在 JDK 21 仍是常用并发基建之一。
 * - 本示例对比“传统监听器回调”与“Flow 响应式流”的写法与优势。
 */
public class FlowApiComparison {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Flow API Comparison ===");

        legacyListenerDemo();
        System.out.println();
        flowApiDemo();
    }

    // 传统方式：自定义监听器接口 + 同步回调
    // Legacy approach: custom listener interface + synchronous callbacks
    private static void legacyListenerDemo() {
        System.out.println("-- Legacy Listener Demo --");

        LegacyPublisher publisher = new LegacyPublisher();
        publisher.addListener(msg -> System.out.println("LegacyListener#1 received: " + msg));
        publisher.addListener(msg -> System.out.println("LegacyListener#2 received: " + msg));

        publisher.publish("Hello");
        publisher.publish("World");
    }

    // JDK Flow：支持背压（Backpressure）、异步发布、与响应式流规范兼容
    // JDK Flow: supports backpressure, async publishing, compatible with Reactive Streams
    private static void flowApiDemo() throws InterruptedException {
        System.out.println("-- Flow API Demo --");

        // 用于等待两个订阅者处理完成
        CountDownLatch done = new CountDownLatch(2);

        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
            // 订阅者：逐个请求（背压），处理完再请求下一个
            // Subscriber that requests one item at a time
            Flow.Subscriber<String> slowSubscriber = new Flow.Subscriber<>() {
                private Flow.Subscription subscription;

                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    this.subscription = subscription;
                    System.out.println("SlowSubscriber subscribed");
                    subscription.request(1); // 先请求一个元素
                }

                @Override
                public void onNext(String item) {
                    System.out.println("SlowSubscriber received: " + item);
                    try {
                        TimeUnit.MILLISECONDS.sleep(150); // 模拟慢消费
                    } catch (InterruptedException ignored) {
                    }
                    subscription.request(1); // 处理完再请求下一个
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("SlowSubscriber error: " + throwable);
                    done.countDown();
                }

                @Override
                public void onComplete() {
                    System.out.println("SlowSubscriber complete");
                    done.countDown();
                }
            };

            Flow.Subscriber<String> fastSubscriber = new SimpleLoggingSubscriber("FastSubscriber", done);

            publisher.subscribe(slowSubscriber);
            publisher.subscribe(fastSubscriber);

            // 提交数据（异步），内部有缓冲队列
            List<String> data = List.of("A", "B", "C", "D", "E");
            data.forEach(publisher::submit);

            // 关闭发布者，发出完成信号
            publisher.close();
        }

        // 等待订阅者处理完成
        done.await(3, TimeUnit.SECONDS);
    }

    // 简单的日志订阅者实现
    static class SimpleLoggingSubscriber implements Flow.Subscriber<String> {
        private final String name;
        private final CountDownLatch done;
        private Flow.Subscription subscription;

        SimpleLoggingSubscriber(String name, CountDownLatch done) {
            this.name = Objects.requireNonNull(name);
            this.done = done;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            System.out.println(name + " subscribed");
            subscription.request(Long.MAX_VALUE); // 一次请求尽可能多（快消费者）
        }

        @Override
        public void onNext(String item) {
            System.out.println(name + " received: " + item);
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println(name + " error: " + throwable);
            if (done != null) done.countDown();
        }

        @Override
        public void onComplete() {
            System.out.println(name + " complete");
            if (done != null) done.countDown();
        }
    }

    // 简化的“传统发布者”实现，仅用于演示对比
    static class LegacyPublisher {
        interface LegacyListener { void onMessage(String msg); }

        private final List<LegacyListener> listeners = new ArrayList<>();

        void addListener(LegacyListener l) { listeners.add(l); }

        void publish(String msg) {
            for (LegacyListener l : listeners) {
                l.onMessage(msg); // 同步逐个回调，无背压、无异步边界
            }
        }
    }
}
