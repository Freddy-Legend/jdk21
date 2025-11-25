# jdk21
JDK 8 to JDK 21 Coding Feature Changes / JDK 8 到 JDK 21 编码特性变动

## Overview / 概述
This repository demonstrates the key Java language features introduced from JDK 8 to JDK 21 with practical examples and comparisons.
该仓库通过实际示例和对比展示了从 JDK 8 到 JDK 21 引入的关键 Java 语言特性。

## Features Covered / 涵盖的特性

### JDK 8 Baseline / JDK 8 基准特性
- Lambda expressions / Lambda 表达式
- Stream API
- Optional class / Optional 类
- New Date and Time API / 新日期时间 API
- Default and static methods in interfaces / 接口中的默认方法和静态方法

### JDK 9-10 Features / JDK 9-10 特性
- Collection factory methods (`List.of`, `Set.of`, `Map.of`) / 集合工厂方法
- Private interface methods / 接口私有方法
- Enhanced try-with-resources / 增强的 try-with-resources
- Var inference for local variables / 局部变量类型推断

### JDK 11-14 Features / JDK 11-14 特性
- HTTP Client (standard) / HTTP 客户端（标准化）
- Pattern matching for instanceof / instanceof 模式匹配
- Switch expressions / Switch 表达式
- Text blocks / 文本块

### JDK 15-16 Features / JDK 15-16 特性
- Records / 记录类
- Sealed classes (not included in examples) / 密封类（示例中未包含）

### JDK 17-21 Features / JDK 17-21 特性
- Pattern matching for switch / Switch 模式匹配
- Virtual threads (performance benchmark) / 虚拟线程（性能基准测试）
- Sequenced collections / 有序集合

## Running the Examples / 运行示例

Compile and run individual examples:
编译并运行单个示例：

```bash
javac src/main/java/tech/legend/learn/*.java
java tech.legend.learn.CollectionFactoriesComparison
```

Or use Maven:
或者使用 Maven：

```bash
mvn compile
mvn exec:java -Dexec.mainClass="tech.legend.learn.CollectionFactoriesComparison"
```

## Detailed Comparisons / 详细对比

1. [CollectionFactoriesComparison.java](src/main/java/tech/legend/learn/CollectionFactoriesComparison.java) - JDK 8 vs JDK 9 Collection Factories / 集合工厂方法
2. [EnhancedTryWithResourcesComparison.java](src/main/java/tech/legend/learn/EnhancedTryWithResourcesComparison.java) - JDK 7-8 vs JDK 9 Try-With-Resources / 增强的 Try-With-Resources
3. [HttpClientComparison.java](src/main/java/tech/legend/learn/HttpClientComparison.java) - JDK 8 vs JDK 11 HTTP Client / HTTP 客户端
4. [OptionalEnhancementsComparison.java](src/main/java/tech/legend/learn/OptionalEnhancementsComparison.java) - JDK 8 vs JDK 9/11 Optional Enhancements / Optional 增强
5. [PatternMatchingComparison.java](src/main/java/tech/legend/learn/PatternMatchingComparison.java) - Traditional vs Pattern Matching / 传统方式 vs 模式匹配
6. [PrivateInterfaceMethodsComparison.java](src/main/java/tech/legend/learn/PrivateInterfaceMethodsComparison.java) - JDK 8 vs JDK 9 Interface Methods / 接口方法
7. [RecordsComparison.java](src/main/java/tech/legend/learn/RecordsComparison.java) - JDK 8 POJOs vs JDK 16 Records / POJO vs 记录类
8. [SequencedCollectionsComparison.java](src/main/java/tech/legend/learn/SequencedCollectionsComparison.java) - Traditional vs JDK 21 Sequenced Collections / 传统方式 vs 有序集合
9. [SwitchComparison.java](src/main/java/tech/legend/learn/SwitchComparison.java) - JDK 8 vs JDK 14 Switch Expressions / Switch 表达式
10. [TextBlocksComparison.java](src/main/java/tech/legend/learn/TextBlocksComparison.java) - String concatenation vs JDK 15 Text Blocks / 字符串拼接 vs 文本块
11. [ThreadVsVirtualThreadBenchmark.java](src/main/java/tech/legend/learn/ThreadVsVirtualThreadBenchmark.java) - Platform vs Virtual Threads / 平台线程 vs 虚拟线程
12. [VarInferenceComparison.java](src/main/java/tech/legend/learn/VarInferenceComparison.java) - Explicit typing vs JDK 10 Var Inference / 显式类型 vs 类型推断