package com.huijiewei.agile.app.user.application.port.outbound;

import com.huijiewei.agile.app.user.domain.UserEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author huijiewei
 */

public interface UserExportPort {
    void export(List<UserEntity> userEntities, OutputStream outputStream) throws IOException;
}
