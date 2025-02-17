package cn.rich.rpc.Common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    //状态码
    private int code;
    //状态信息
    private String message;
    //具体数据
    private Object data;
    //传输数据的类型，以便在自定义序列化器中解析
    private Class<?> dataType;
    //构造成功信息
    public static RpcResponse sussess(Object data){
        return RpcResponse.builder().code(200).dataType(data.getClass()).data(data).build();
    }
    //构造失败信息
    public static RpcResponse fail(){
        return RpcResponse.builder().code(500).message("服务器发生错误").build();
    }

    public static RpcResponse fail(String message){
        return RpcResponse.builder().code(500).message(message).build();
    }
}