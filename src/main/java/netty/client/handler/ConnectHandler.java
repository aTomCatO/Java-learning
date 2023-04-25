package netty.client.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author XYC
 * 客户端连接处理器
 */
@ChannelHandler.Sharable
public class ConnectHandler extends ChannelDuplexHandler {
    private final Logger logger = LoggerFactory.getLogger(ConnectHandler.class);
    private final NettyClient client;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ConnectHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        // 如果服务端写回sessionId，需将其处理
        Map<String, String> msgMap = (Map<String, String>) msg;
        String sessionId = msgMap.get("sessionId");
        if (sessionId != null) {
            client.setMessage("sessionId", sessionId);
            FileChannel fileChannel = FileChannel.open(Paths.get("D:\\JavaWorld\\Demo\\Java\\src\\main\\resources\\sessionId.txt"), StandardOpenOption.WRITE);
            fileChannel.truncate(0).write(ByteBuffer.wrap(sessionId.getBytes(StandardCharsets.UTF_8)));
            fileChannel.close();
            client.removeMessage("lastConnectTime");
        }
        super.channelRead(context, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        String sessionId;
        Object value = client.getMessage("sessionId");
        File file = new File("D:\\JavaWorld\\Demo\\Java\\src\\main\\resources\\sessionId.txt");

        // 获取文件的修改时间，也就是上一次向文件写入 sessionId 的时间
        // 当服务端没有记录该连接的session时，服务端会生成session并记录向里面记录生成的时间，
        // 并将sessionId发送给客户端，客户端会将该sessionId写入文件中，这时 文件修改时间 就会与 session记录的生成时间 一致
        // 当服务端有记录该连接的session时，为了保证不是人为的修改sessionId文件，
        // 就会用 文件的修改时间与session中记录的时间作比较，从而保证session过继给新连接的安全性。
        long lastModified = file.lastModified();
        String lastConnectTime = dateFormat.format(new Date(lastModified));
        logger.info("client channelActive lastConnectTime => " + lastConnectTime);
        client.setMessage("lastConnectTime", lastConnectTime);

        if (value == null) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file), 300);
            sessionId = bufferedReader.readLine();
            if (StringUtils.hasText(sessionId)) {
                client.setMessage("sessionId", sessionId);
            }
        }
        client.sendMessage();
        super.channelActive(context);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        logger.info("【channelInactive】10秒后开始重新建立连接");
        EventLoop eventLoop = context.channel().eventLoop();
        eventLoop.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    for (int i = 0; i < 10; i++) {
                        if (client.connect()) {
                            logger.info("建立连接成功");
                            break;
                        }
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 搭配 IdleStateHandler 使用
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception {
        IdleStateEvent idleStateEvent = (IdleStateEvent) event;
        if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
            context.channel().writeAndFlush("客户端心跳机制触发");
        }
    }
}
