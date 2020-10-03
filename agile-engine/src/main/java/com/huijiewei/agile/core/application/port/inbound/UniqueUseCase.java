package com.huijiewei.agile.core.application.port.inbound;

import java.util.Map;

/**
 * @author huijiewei
 */

public interface UniqueUseCase {
    Boolean unique(Map<String, String> values, String primaryKey, String primaryValue);
}
