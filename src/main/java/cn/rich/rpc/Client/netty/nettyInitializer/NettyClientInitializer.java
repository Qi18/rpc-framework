package cn.rich.rpc.Client.netty.nettyInitializer;


import cn.rich.rpc.Client.netty.handler.NettyClientHandler;
import cn.rich.rpc.Common.serializer.myCode.MyDecoder;
import cn.rich.rpc.Common.serializer.myCode.MyEncoder;
import cn.rich.rpc.Common.serializer.mySerializer.JsonSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //使用自定义的编解码器
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}
