package com.huijiewei.agile.core.application.port.outbound;

import java.util.List;

/**
 * @author huijiewei
 */

public interface ExistsPort {
    Boolean exist(String targetProperty, List<String> values);
}
