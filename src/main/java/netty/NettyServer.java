package netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import netty.common.handler.MessageHandler;
import netty.server.handler.ClientConnectHandler;
import netty.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XYC
 */
@Data
public class NettyServer {
    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    private final ServerBootstrap serverBootstrap =
            new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class);

    private final Map<String, Session> sessionMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public void setSession(String sessionId, Session session) {
        sessionMap.put(sessionId, session);
    }

    public Session getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    /**
     * 要点一：
     * <p>
     * 1、当channel有read事件时，按顺序调用ChannelInboundHandlerAdapter。
     * 依次调用的前提是需要显示的调用 super.channelRead(ctx,msg)
     * <p>
     * 2、当channel有write事件时，按逆序调用ChannelOutboundHandlerAdapter
     * 依次调用的前提是需要显示的调用 super.write(ctx, msg, promise)
     * <p>
     * 要点二：当在 Handler 执行中时，如果调用了 ctx.writeAndFlush() 方法，
     * 将在当前的这个 Handler 开始，逆序查找 ChannelOutboundHandlerAdapter 执行
     */
    public void server1() {
        serverBootstrap
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(
                                        new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                                        new StringDecoder(StandardCharsets.UTF_8),
                                        new StringEncoder(StandardCharsets.UTF_8),
                                        // new LoggingHandler(LogLevel.INFO),
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                logger.info("1 receive msg -- " + msg.toString());
                                                super.channelRead(ctx, msg);
                                            }
                                        },
                                        new ChannelOutboundHandlerAdapter() {
                                            @Override
                                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                                logger.info("2 send msg -- " + msg.toString());
                                                super.write(ctx, msg, promise);
                                            }
                                        },
                                        new ChannelOutboundHandlerAdapter() {
                                            @Override
                                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                                logger.info("3 send msg -- " + msg.toString());
                                                super.write(ctx, msg, promise);
                                            }
                                        },
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                logger.info("4 receive msg -- " + msg.toString());
                                                super.channelRead(ctx, msg);
                                            }
                                        },
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                logger.info("5 receive msg -- " + msg.toString());
                                                logger.info("5 send msg -- hello client");
                                                channel.writeAndFlush("hello client");
                                                // ctx.writeAndFlush("hello client");
                                            }

                                            @Override
                                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                                logger.info("5 send msg -- hello client");
                                                ctx.channel().writeAndFlush("hello client");
                                            }
                                        },
                                        new ChannelOutboundHandlerAdapter() {
                                            @Override
                                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                                logger.info("6 send msg -- " + msg.toString());
                                                super.write(ctx, msg, promise);
                                            }
                                        });

                    }
                })
                .bind(8888);
    }

    /**
     * 演示心跳机制、session
     * 对于 netty 客户端或服务端一方断网或网络异常，另外一端是不知道的。
     * 网络良好时一端 close 另一端会回调 InActive , 但断网或网络异常时是不会回调 InActive 的，
     * 因为没法回调，连接不通， 上层根本就不知道底层连接的状态。 且TCP 自带的心跳包默认是 2h。
     * 这时只有用应用层心跳包解决：无论客户端或服务端，只要一定时间内没有收到另一端发送的心跳包 （读超时），
     * 不管另一端是断网还是宕机等情况，就会认为连接不可用了。
     * <p>
     * netty中使用 IdleStateHandler 实现心跳机制
     * 读超时：指定的时间内没有从 Channel 读取到数据时，就触发读超时事件（IdleStateEvent.READER_IDLE）
     * 写超时：指定的时间内没有写入数据到 Channel 时，就触发写超时事件（IdleStateEvent.WRITER_IDLE）
     * 读写超时：指定的时间内没有读/写操作时, 会触发读写超时事件（IdleStateEvent.ALL_IDLE）
     */
    public void server2() throws InterruptedException {
        ClientConnectHandler clientConnectHandler = new ClientConnectHandler(this);
        serverBootstrap
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(
                                        new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                                        new StringDecoder(StandardCharsets.UTF_8),
                                        new StringEncoder(StandardCharsets.UTF_8),
                                        new LoggingHandler(LogLevel.INFO),
                                        // 服务端心跳机制：如果6秒内没有收到客户端的消息，可以将客户端通道关闭或断开连接，处理session等
                                        new IdleStateHandler(120, 0, 0),
                                        new MessageHandler(),
                                        clientConnectHandler
                                );
                    }
                })
                .bind(8888);
        logger.info("20秒后开始给所有已建立连接的客户端发送一条消息");
        Thread.sleep(20000);
        for (Session session : sessionMap.values()) {
            Channel channel = session.getChannel();
            channel.writeAndFlush("server群发");
        }
    }
    public static void main(String[] args) throws Exception {
        new NettyServer().server2();
    }
}
