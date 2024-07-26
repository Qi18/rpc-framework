package cn.rich.rpc.Client.serviceCenter;

import cn.rich.rpc.Client.cache.ServiceCache;
import cn.rich.rpc.Client.serviceCenter.ZkWatcher.watchZK;
import cn.rich.rpc.Client.serviceCenter.balance.impl.ConsistencyHashBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class ZKServiceCenter implements ServiceCenter{

    // curator 提供的zookeeper客户端
    private CuratorFramework client;

    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";

    private static final String RETRY = "CanRetry";

    //serviceCache
    private ServiceCache cache;

    //负责zookeeper客户端的初始化，并与zookeeper服务端进行连接
    public ZKServiceCenter() throws InterruptedException {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        // 使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper 连接成功");
        // 缓存初始化
        cache = new ServiceCache();
        // 加入zookeeper事件监听器
        watchZK watcher = new watchZK(client, cache);
        // 监听启动
        watcher.watchToUpdate(ROOT_PATH);
    }

    //根据服务名（接口名）返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //先从本地缓存中找
            System.out.println("缓存值:" + ServiceCache.cache);
            List<String> addressList = cache.getServiceFromCache(serviceName);

            if (addressList == null) {
                addressList = client.getChildren().forPath("/" + serviceName);
            }
            System.out.println("addressList:" + addressList);
            String address = new ConsistencyHashBalance().balance(addressList);
            return parseAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkRetry(String serviceName) {
        boolean canRetry =false;
        try {
            List<String> serviceList = client.getChildren().forPath("/" + RETRY);
            for(String s : serviceList){
                //如果列表中有该服务
                if(s.equals(serviceName)){
                    System.out.println("服务" + serviceName + "在白名单上，可进行重试");
                    canRetry=true;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return canRetry;
    }


    // 地址 -> XXX.XXX.XXX.XXX:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() +
                ":" +
                serverAddress.getPort();
    }

    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
