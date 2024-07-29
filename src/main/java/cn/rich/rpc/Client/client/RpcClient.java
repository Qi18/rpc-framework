package cn.rich.rpc.Client.client;

import cn.rich.rpc.Common.message.RpcRequest;
import cn.rich.rpc.Common.message.RpcResponse;

/**
 * @author rich
 * @version 1.0
 * @create 2024/07/24
 */

public interface  RpcClient {
    RpcResponse sendRequest(RpcRequest request);
}
