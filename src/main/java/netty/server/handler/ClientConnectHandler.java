package netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import netty.NettyServer;
import netty.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author XYC
 * 服务端处理客户端连接的处理器
 */
@ChannelHandler.Sharable
public class ClientConnectHandler extends ChannelDuplexHandler {
    private final NettyServer server;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final Logger logger = LoggerFactory.getLogger(ClientConnectHandler.class);

    public ClientConnectHandler(NettyServer server) {
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        Channel channel = context.channel();
        Map<String, String> msgMap = (Map<String, String>) msg;
        // 判断该客户端有无连接记录
        String sessionId = msgMap.get("sessionId");
        if (StringUtils.hasText(sessionId)) {
            Session session = server.getSession(sessionId);
            if (session != null) {
                // 判断channel是否活跃
                Channel sessionChannel = session.getChannel();
                if (sessionChannel != null && sessionChannel.isActive()) {
                    super.channelRead(context, msg);
                    return;
                } else {
                    // 如果不活跃，证明客户端出现过断开连接情况，这种情况下储存在session中的channel 已经 close 了，需要更新 session 的 channel。
                    // 为了保证客户端发过来的sessionId绝对安全可靠用，
                    // 用 记录在session中的 上次连接的时间 与 客户端中记录sessionId文本的修改时间 作比较（）
                    String lastConnectTime = session.getAttribute("lastConnectTime").toString();
                    logger.info("server channelRead lastConnectTime => " + lastConnectTime);
                    if (msgMap.get("lastConnectTime").toString().equals(lastConnectTime)) {
                        logger.info("session记录认证成功 -- 更新 session");
                        session.setChannel(channel);
                        super.channelRead(context, msg);
                        return;
                    } else {
                        logger.info("session记录认证失败 -- 移除 session");
                        server.removeSession(sessionId);
                    }
                }
            }
        }
        // 不存在session记录 以及 session记录认证失败 的情况下执行
        context.fireChannelActive();
        logger.info("不存在session记录 -- 设置 session");
        sessionId = UUID.randomUUID().toString();
        Session session = new Session(channel);
        session.setAttribute("lastConnectTime", dateFormat.format(new Date()));
        server.setSession(sessionId, session);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("msg", "hello client");
        channel.writeAndFlush(map);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) throws Exception {
        IdleStateEvent idleStateEvent = (IdleStateEvent) event;
        if (idleStateEvent.state() == IdleState.READER_IDLE) {
            logger.info("客户端已掉线");
            context.channel().close();
        }
        super.userEventTriggered(context, event);
    }
}
