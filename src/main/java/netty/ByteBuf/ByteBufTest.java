package netty.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author XYC
 */
public class ByteBufTest {
    private final Logger logger = LoggerFactory.getLogger(ByteBufTest.class);
    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(16);

    {
        buf.writeBytes(" hello ByteBuf ".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void test1() {
        logger.info("readerIndex" + buf.readerIndex());
        logger.info("writerIndex: " + buf.writerIndex());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.writerIndex(); i++) {
            sb.append((char) buf.getByte(i));
            if (i % 6 == 0) {
                buf.writeBytes("h".getBytes(StandardCharsets.UTF_8));
                logger.info("readerIndex: " + buf.readerIndex());
                logger.info("writerIndex: " + buf.writerIndex());
            }
        }

        logger.info(sb.toString());
    }
}
