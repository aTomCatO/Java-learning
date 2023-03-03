package netty.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * @author XYC
 */
@Data
@AllArgsConstructor
public class Cookie {
    private String name;

    private String value;
}
