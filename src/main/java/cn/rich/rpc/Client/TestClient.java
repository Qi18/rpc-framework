package cn.rich.rpc.Client;


import cn.rich.rpc.Client.proxy.ClientProxy;
import cn.rich.rpc.Common.pojo.User;
import cn.rich.rpc.Common.service.UserService;


public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1",9998);
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
