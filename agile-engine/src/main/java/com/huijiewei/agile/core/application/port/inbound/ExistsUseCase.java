package com.huijiewei.agile.core.application.port.inbound;

import java.util.List;

/**
 * @author huijiewei
 */

public interface ExistsUseCase {
    Boolean exists(String targetProperty, List<String> values);
}
