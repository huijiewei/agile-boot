package com.huijiewei.agile.core.application.port.outbound;

import java.util.List;

/**
 * @author huijiewei
 */

public interface ExistsPort {
    boolean exists(String targetProperty, List<String> values);
}
