package com.example.jdk21.sequenced;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * JDK 21: Sequenced Collections
 *
 * 新的集合接口层次结构，提供统一的首尾元素访问方法
 */
@Component
public class SequencedCollectionDemo {

    public void demonstrate() {
        System.out.println("\n=== JDK 21: Sequenced Collections ===");

        // 1. SequencedCollection
        demonstrateSequencedCollection();

        // 2. SequencedSet
        demonstrateSequencedSet();

        // 3. SequencedMap
        demonstrateSequencedMap();

        // 4. 实际应用
        demonstrateRealWorldUsage();
    }

    /**
     * SequencedCollection 接口
     * 新方法: addFirst(), addLast(), getFirst(), getLast(), removeFirst(), removeLast(), reversed()
     */
    private void demonstrateSequencedCollection() {
        System.out.println("\n1. SequencedCollection:");

        List<String> list = new ArrayList<>();
        list.add("中间");

        // 在开头添加
        list.addFirst("开头");

        // 在末尾添加
        list.addLast("末尾");

        System.out.println("  列表: " + list);
        System.out.println("  第一个元素: " + list.getFirst());
        System.out.println("  最后一个元素: " + list.getLast());

        // 反转视图
        List<String> reversed = list.reversed();
        System.out.println("  反转后: " + reversed);

        // Deque 也实现了 SequencedCollection
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addLast(2);
        deque.addLast(3);
        System.out.println("\n  Deque: " + deque);
        System.out.println("  移除第一个: " + deque.removeFirst());
        System.out.println("  移除最后一个: " + deque.removeLast());
        System.out.println("  剩余: " + deque);
    }

    /**
     * SequencedSet 接口
     */
    private void demonstrateSequencedSet() {
        System.out.println("\n2. SequencedSet:");

        // LinkedHashSet 实现 SequencedSet
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("Apple");
        set.add("Banana");
        set.add("Cherry");

        System.out.println("  Set: " + set);
        System.out.println("  第一个: " + set.getFirst());
        System.out.println("  最后一个: " + set.getLast());

        // 反转 Set
        SequencedSet<String> reversedSet = set.reversed();
        System.out.println("  反转后: " + reversedSet);

        // TreeSet 也实现 SequencedSet
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.addAll(List.of(5, 2, 8, 1, 9));
        System.out.println("\n  TreeSet (自动排序): " + treeSet);
        System.out.println("  最小值: " + treeSet.getFirst());
        System.out.println("  最大值: " + treeSet.getLast());
    }

    /**
     * SequencedMap 接口
     */
    private void demonstrateSequencedMap() {
        System.out.println("\n3. SequencedMap:");

        // LinkedHashMap 实现 SequencedMap
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("first", 1);
        map.put("second", 2);
        map.put("third", 3);

        System.out.println("  Map: " + map);

        // 获取第一个和最后一个 Entry
        Map.Entry<String, Integer> firstEntry = map.firstEntry();
        Map.Entry<String, Integer> lastEntry = map.lastEntry();

        System.out.println("  第一个 Entry: " + firstEntry.getKey() + " = " + firstEntry.getValue());
        System.out.println("  最后一个 Entry: " + lastEntry.getKey() + " = " + lastEntry.getValue());

        // 反转 Map
        SequencedMap<String, Integer> reversedMap = map.reversed();
        System.out.println("  反转后: " + reversedMap);

        // putFirst 和 putLast
        map.putFirst("zero", 0);
        map.putLast("fourth", 4);
        System.out.println("  添加后: " + map);

        // pollFirstEntry 和 pollLastEntry
        System.out.println("  移除第一个: " + map.pollFirstEntry());
        System.out.println("  移除最后一个: " + map.pollLastEntry());
        System.out.println("  剩余: " + map);
    }

    /**
     * 实际应用场景
     */
    private void demonstrateRealWorldUsage() {
        System.out.println("\n4. 实际应用场景:");

        // 场景1: LRU 缓存
        demonstrateLRUCache();

        // 场景2: 最近访问记录
        demonstrateRecentAccess();

        // 场景3: 消息队列
        demonstrateMessageQueue();
    }

    /**
     * LRU 缓存实现
     */
    private void demonstrateLRUCache() {
        System.out.println("\n  LRU 缓存:");

        class LRUCache<K, V> extends LinkedHashMap<K, V> {
            private final int capacity;

            public LRUCache(int capacity) {
                super(capacity, 0.75f, true);
                this.capacity = capacity;
            }

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        }

        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        System.out.println("    缓存: " + cache);

        // 访问 key1，使其成为最近使用
        cache.get("key1");

        // 添加新元素，key2 应该被移除
        cache.put("key4", "value4");
        System.out.println("    添加 key4 后: " + cache);
        System.out.println("    最旧的元素: " + cache.firstEntry().getKey());
        System.out.println("    最新的元素: " + cache.lastEntry().getKey());
    }

    /**
     * 最近访问记录
     */
    private void demonstrateRecentAccess() {
        System.out.println("\n  最近访问记录:");

        LinkedHashSet<String> recentPages = new LinkedHashSet<>();

        // 模拟页面访问
        String[] visits = {"home", "products", "about", "contact", "products", "home"};

        for (String page : visits) {
            // 如果已存在，先移除再添加（移到末尾）
            recentPages.remove(page);
            recentPages.add(page);

            // 只保留最近5个
            if (recentPages.size() > 5) {
                recentPages.removeFirst();
            }
        }

        System.out.println("    访问历史: " + recentPages);
        System.out.println("    最早访问: " + recentPages.getFirst());
        System.out.println("    最近访问: " + recentPages.getLast());
    }

    /**
     * 消息队列
     */
    private void demonstrateMessageQueue() {
        System.out.println("\n  消息队列:");

        Deque<String> messageQueue = new ArrayDeque<>();

        // 添加消息
        messageQueue.addLast("消息1: 用户登录");
        messageQueue.addLast("消息2: 数据更新");
        messageQueue.addLast("消息3: 发送邮件");

        System.out.println("    队列: " + messageQueue);

        // 处理消息 (FIFO)
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.removeFirst();
            System.out.println("    处理: " + message);
        }

        // 优先级消息（添加到队首）
        messageQueue.addLast("普通消息1");
        messageQueue.addLast("普通消息2");
        messageQueue.addFirst("紧急消息！");

        System.out.println("    优先级队列: " + messageQueue);
    }

    /**
     * 在 Spring 中的应用
     */
    public void demonstrateSpringUsage() {
        System.out.println("\n=== Spring 中使用 Sequenced Collections ===");

        System.out.println("""

            应用场景:
            1. 请求日志: 保持最近的请求记录
            2. 会话管理: 跟踪用户会话的顺序
            3. 审计日志: 保持操作的时间顺序
            4. 配置管理: 按优先级排序的配置项
            5. 任务队列: 有序的任务调度
            """);

        // 示例: API 请求日志
        record ApiRequest(String endpoint, String method, long timestamp) {}

        Deque<ApiRequest> requestLog = new ArrayDeque<>();
        requestLog.addLast(new ApiRequest("/api/users", "GET", System.currentTimeMillis()));
        requestLog.addLast(new ApiRequest("/api/products", "POST", System.currentTimeMillis()));

        System.out.println("  最新请求: " + requestLog.getLast());
        System.out.println("  最早请求: " + requestLog.getFirst());
    }
}
