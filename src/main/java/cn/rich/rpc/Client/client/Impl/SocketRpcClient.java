package cn.rich.rpc.Client.client.Impl;


import cn.rich.rpc.Client.client.RpcClient;
import cn.rich.rpc.Common.message.RpcRequest;
import cn.rich.rpc.Common.message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketRpcClient implements RpcClient {
    //这里负责底层与服务端的通信，发送request，返回response
    private final String host;
    private final int port;

    public SocketRpcClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request){
        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse response = (RpcResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
