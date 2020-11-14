package com.huijiewei.agile.core.application.port.inbound;

import java.util.List;

/**
 * @author huijiewei
 */

public interface ExistsUseCase {
    boolean exists(String targetProperty, List<String> values);
}
