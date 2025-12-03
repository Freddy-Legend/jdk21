package com.coinw.ff.oms.skywalkingdemospringboot;

import com.coinw.ff.oms.skywalkingdemospringboot.service.MyService;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/")
class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);
    @Autowired
    MyService myService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("hello")
    public String hello() {
        return "Hello, SkyWalking!";
    }

    // 新增一个接口，用于发起远程调用
    @GetMapping("/call-provider")
    public String callProvider() {
        logger.info("Consumer is calling provider...");
        // 这里直接调用 provider-service 的地址
        String providerUrl = "http://localhost:9091/data";
        String response = restTemplate.getForObject(providerUrl, String.class);
        return "Consumer received: [" + response + "]";
    }

    // 新增一个接口，用于发起远程调用
    @GetMapping("/data")
    public String getData() throws InterruptedException {
        logger.info("Provider service received a request.");
        // 模拟一些数据库查询或耗时操作
        TimeUnit.MILLISECONDS.sleep(200);
        return "Data from Provider Service (Port 9091)";
    }


    /**
     * @Trace 注解创建整个请求的顶级 Span。
     */
    @Trace(operationName = "OrderController/handleOrderRequest")
    @GetMapping("/order")
    public String handleOrder(@RequestParam String id) {
        callProvider();
        myService.handleRequest();
        return callProvider();
    }

}

