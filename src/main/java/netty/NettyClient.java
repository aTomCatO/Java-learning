package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import netty.client.handler.ConnectHandler;
import netty.common.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author XYC
 * new LoggingHandler(LogLevel.INFO)
 */
public class NettyClient {
    private final NioEventLoopGroup group = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class);
    private ChannelFuture channelFuture;
    private Channel channel;
    private final String host = "localhost";
    private final int port = 8888;
    private final Map<String, Object> messageMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    public NettyClient setMessage(String name, Object value) {
        messageMap.put(name, value);
        return this;
    }
    public Object getMessage(String name) {
        return messageMap.get(name);
    }

    public void removeMessage(String name) {
        messageMap.remove(name);
    }

    class ScannerTask implements Runnable {
        private final Scanner scanner = new Scanner(System.in);
        @Override
        public void run() {
            String msgContent;
            while (!(msgContent = scanner.nextLine()).equals("q")) {
                setMessage("msgContent", msgContent);
                sendMessage();
            }
            // 断开连接
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("断开连接");
        }
    }

    /**
     * 演示 handler 执行
     */
    public void client1() throws InterruptedException {
        Channel channel = bootstrap
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(
                                        new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                                        new StringEncoder(StandardCharsets.UTF_8),
                                        new StringDecoder(StandardCharsets.UTF_8),
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                logger.info("1 receive msg -- " + msg.toString());
                                                super.channelRead(ctx, msg);
                                            }
                                        },
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                logger.info("2 receive msg -- " + msg.toString());
                                                super.channelRead(ctx, msg);
                                            }
                                        }, new ChannelOutboundHandlerAdapter() {
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
                                        new ChannelOutboundHandlerAdapter() {
                                            @Override
                                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                                logger.info("5 send msg -- " + msg.toString());
                                                super.write(ctx, msg, promise);
                                            }
                                        },
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                                logger.info("6 receive msg -- " + msg.toString());
                                            }
                                        });

                    }
                })
                .connect("localhost", 8888)
                .sync()
                .channel();
        new Thread(new ScannerTask()).start();
    }

    /**
     * 演示心跳机制、断开连接、重新连接
     */
    public void client2() {
        ConnectHandler connectHandler = new ConnectHandler(this);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline()
                        .addLast(
                                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                                new StringEncoder(StandardCharsets.UTF_8),
                                new StringDecoder(StandardCharsets.UTF_8),
                                new LoggingHandler(LogLevel.INFO),
                                // 客户端心跳机制：如果客户端每3秒内没有主动给服务端发送消息，为了让服务端确认该客户端还在线，将被动发送消息给服务端
                                new IdleStateHandler(0, 60, 0),
                                new MessageHandler(),
                                connectHandler);
            }
        });
        connect();
        new Thread(new ScannerTask()).start();
    }

    public boolean connect() {
        try {
            channelFuture = bootstrap.connect(host, port);
            boolean flag = channelFuture.awaitUninterruptibly(30, TimeUnit.SECONDS);
            channel = channelFuture.channel();
            if (flag) {
                if (channel != null && channel.isActive()) {
                    new Thread(new ScannerTask()).start();
                    return true;
                }
            } else {
                logger.info("连接超时");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Channel sendMessage() {
        channel.writeAndFlush(messageMap);
        return channel;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new NettyClient().client2();
    }
}
