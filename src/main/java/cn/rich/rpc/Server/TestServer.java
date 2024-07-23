package cn.rich.rpc.Server;


import cn.rich.rpc.Common.service.Impl.UserServiceImpl;
import cn.rich.rpc.Common.service.UserService;
import cn.rich.rpc.Server.provider.ServiceProvider;
import cn.rich.rpc.Server.server.Impl.SimpleRPCRPCServer;
import cn.rich.rpc.Server.server.RpcServer;


public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new SimpleRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
