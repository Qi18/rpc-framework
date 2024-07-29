package cn.rich.rpc.Server.netty.handler;

import cn.rich.rpc.Common.Message.RpcRequest;
import cn.rich.rpc.Common.Message.RpcResponse;
import cn.rich.rpc.Server.provider.ServiceProvider;
import cn.rich.rpc.Server.ratelimit.RateLimit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author rich
 * @version 1.0
 * @create 2024/07/24
 * 因为是服务器端，我们知道接受到请求格式是RPCRequest
 * Object类型也行，强制转型就行
 */

@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private ServiceProvider serviceProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        //接收request，读取并调用服务
        RpcResponse response = getResponse(request);
//        System.out.println(request);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private RpcResponse getResponse(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName = rpcRequest.getInterfaceName();
        //接口限流降级
        RateLimit rateLimit = serviceProvider.getRateLimitProvider().getRateLimit(interfaceName);
        if(!rateLimit.getToken()) {
            //如果获取令牌失败，进行限流降级，快速返回结果
            System.out.println("限流中");
            return RpcResponse.fail("已被限流");
        }
        //得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
            Object invoke = method.invoke(service, rpcRequest.getParams());
            return RpcResponse.sussess(invoke);
//            return RpcResponse.fail();熔断测试
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }

}
