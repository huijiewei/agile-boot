package com.huijiewei.agile.core.application.port.outbound;

import java.util.Map;

/**
 * @author huijiewei
 */

public interface UniquePort {
    boolean unique(Map<String, String> values, String primaryKey, String primaryValue);
}
