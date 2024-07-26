package cn.rich.rpc.Server.provider;

import cn.rich.rpc.Server.serviceRegister.ServiceRegister;
import cn.rich.rpc.Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


//本地服务存放器
public class ServiceProvider {
    //集合中存放服务的实例
    private Map<String,Object> interfaceProvider;
    private String host;
    private int port;
    private ServiceRegister serviceRegister;

    public ServiceProvider(String host, int port){
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKServiceRegister();
    }

    //本地注册服务
    public void provideServiceInterface(Object service, boolean canRetry){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for (Class<?> clazz : interfaceName){
            //本地映射表
            interfaceProvider.put(clazz.getName(), service);
            //注册到注册中心
            serviceRegister.register(clazz.getName(), new InetSocketAddress(host, port), canRetry);
        }

    }

    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
