package cn.rich.rpc.Server.ratelimit;


public interface RateLimit {
    //获取访问许可
    boolean getToken();
}
