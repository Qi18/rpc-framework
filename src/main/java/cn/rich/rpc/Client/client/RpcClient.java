package cn.rich.rpc.Client.client;

import cn.rich.rpc.Common.Message.RpcRequest;
import cn.rich.rpc.Common.Message.RpcResponse;

/**
 * @author rich
 * @version 1.0
 * @create 2024/07/24
 */

public interface  RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
