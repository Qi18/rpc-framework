package cn.rich.rpc.Server.server.Impl;


import cn.rich.rpc.Server.provider.ServiceProvider;
import cn.rich.rpc.Server.server.RpcServer;
import cn.rich.rpc.Server.work.WorkThread;
import lombok.AllArgsConstructor;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


@AllArgsConstructor
public class SimpleRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvide;
    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动了");
            while (true) {
                //如果没有连接，会堵塞在这里
                Socket socket = serverSocket.accept();
                //有连接，创建一个新的线程执行处理
                new Thread(new WorkThread(socket, serviceProvide)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
    }
}
