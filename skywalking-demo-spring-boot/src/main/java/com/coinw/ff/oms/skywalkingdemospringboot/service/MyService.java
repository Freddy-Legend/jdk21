package com.coinw.ff.oms.skywalkingdemospringboot.service;

import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Tags;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    // 入口 Span
    @Trace(operationName = "handleRequest")
    public void handleRequest() {
        // 将逻辑拆分为独立方法，利用 AOP 生成子 Span
        dbQuery();
        apiCall();
        businessCalc();
        System.out.println("Request handling completed.");
    }

    // 操作1: 模拟数据库查询
    // @Trace 生成一个新的 Span，operationName 为自定义名称
    @Trace(operationName = "db_query")
    // @Tag 可以直接加在方法上，自动打标签
    @Tags({@Tag(key = "DB_TYPE", value = "MySQL"), @Tag(key = "operation", value = "db_query")})
    private void dbQuery() {
        try {
            Thread.sleep(500);
            System.out.println("Database query completed.");
        } catch (InterruptedException e) {
            ActiveSpan.error(e);
            // 恢复中断状态
            Thread.currentThread().interrupt();
        }
    }

    // 操作2: 模拟外部 API 调用
    @Trace(operationName = "api_call")
    @Tags({
            @Tag(key = "HTTP_METHOD", value = "GET"),
            @Tag(key = "HTTP_URL", value = "https://external-api.com")
    })
    private void apiCall() {
        try {
            Thread.sleep(300);
            System.out.println("API call completed.");
        } catch (InterruptedException e) {
            ActiveSpan.error(e);
            Thread.currentThread().interrupt();
        }
    }

    // 操作3: 模拟业务计算
    @Trace(operationName = "business_calc")
    @Tag(key = "calc_type", value = "complex")
    private void businessCalc() {
        try {
            // 如果需要动态打标签（例如根据运行时数据），仍然可以使用 ActiveSpan
            // 这里的 ActiveSpan.tag 会作用于当前方法 "business_calc" 的 Span
            ActiveSpan.tag("dynamic_info", "some_runtime_value");

            Thread.sleep(200);
            System.out.println("Business calculation completed.");
        } catch (InterruptedException e) {
            ActiveSpan.error(e);
            Thread.currentThread().interrupt();
        }
    }
}