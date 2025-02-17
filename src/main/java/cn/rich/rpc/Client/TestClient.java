package cn.rich.rpc.Client;


import cn.rich.rpc.Client.proxy.ClientProxy;
import cn.rich.rpc.Common.pojo.User;
import cn.rich.rpc.Common.service.UserService;


public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy = new ClientProxy();
        UserService proxy = clientProxy.getProxy(UserService.class);

        for (int i = 0; i < 100; i++) {
            try {
                new Thread(() -> {
                    User user = proxy.getUserByUserId(1);
                    System.out.println("从服务端得到的user=" + user.toString());
                }).start();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                User user = proxy.getUserByUserId(1);
                System.out.println("从服务端得到的user=" + user.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        User user = proxy.getUserByUserId(1);
//        System.out.println("从服务端得到的user=" + user.toString());

//        User u=User.builder().id(100).userName("wxx").sex(true).build();
//        Integer id = proxy.insertUserId(u);
//        System.out.println("向服务端插入user的id" + id);

//        for (int i = 0; i < 15; i++) {
//            new Thread(sendRequest).start();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
    }
}
