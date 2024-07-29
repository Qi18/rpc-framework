package cn.rich.rpc.Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;


public class CircuitBreakerProvider {

    private Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();

    public synchronized CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;
        if (circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker = circuitBreakerMap.get(serviceName);
        } else {
            System.out.println("创建一个新的熔断器");
            circuitBreaker = new CircuitBreaker(3,0.5,10000);
            circuitBreakerMap.put(serviceName, circuitBreaker);
        }
        return circuitBreaker;
    }
}
