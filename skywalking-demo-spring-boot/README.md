```shell
java -javaagent:/Users/freddy_zhang/Downloads/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=skywalking-demo-spring-boot \
     -Dskywalking.agent.resttemplate_intercept_enabled=true \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -jar /Users/freddy_zhang/jdk21/skywalking-demo-spring-boot/target/skywalking-demo-spring-boot-0.0.1-SNAPSHOT.jar

java -javaagent:/Users/freddy_zhang/Downloads/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=skywalking-demo-spring-boot-provider \
     -Dskywalking.collector.backend_service=localhost:11800 \
     -Dskywalking.agent.resttemplate_intercept_enabled=true \
     -jar /Users/freddy_zhang/jdk21/skywalking-demo-spring-boot/target/skywalking-demo-spring-boot-0.0.1-SNAPSHOT.jar \
     --server.port=9091 --spring.application.name=skywalking-demo-spring-boot-provider

```

optional-plugins 下 apm-resttemplate-6.x-plugin-9.5.0.jar 移动到 plugin