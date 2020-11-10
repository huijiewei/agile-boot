package com.huijiewei.agile.app.cms.application.port.outbound;

import java.util.List;

/**
 * @author huijiewei
 */

public interface CmsArticlePersistencePort {
    Boolean existsByCmsCategoryIds(List<Integer> cmsCategoryIds);
}
