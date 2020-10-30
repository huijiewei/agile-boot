package com.huijiewei.agile.app.user.adapter.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.huijiewei.agile.app.user.application.port.outbound.UserExportPort;
import com.huijiewei.agile.app.user.domain.UserEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@Component
public class UserExcelAdapter implements UserExportPort {
    @Override
    public void export(List<UserEntity> userEntities, OutputStream outputStream) throws IOException {
        List<ExcelExportEntity> entities = new ArrayList<>();
        entities.add(new ExcelExportEntity("Id", "id"));
        entities.add(new ExcelExportEntity("手机号码", "phone", 30));
        entities.add(new ExcelExportEntity("电子邮件", "email", 50));
        entities.add(new ExcelExportEntity("名称", "name"));
        entities.add(new ExcelExportEntity("注册 IP", "createdIp", 15));
        entities.add(new ExcelExportEntity("注册来源", "createdFrom.description"));
        entities.add(new ExcelExportEntity("创建时间", "createdAt", 30));

        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName("用户列表");
        exportParams.setType(ExcelType.XSSF);

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, entities, userEntities);

        workbook.write(outputStream);

        workbook.close();
    }
}
