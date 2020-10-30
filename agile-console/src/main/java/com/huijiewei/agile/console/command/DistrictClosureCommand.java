package com.huijiewei.agile.console.command;

import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.app.district.adapter.persistence.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.beryx.textio.TextIO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author huijiewei
 */

@Component
@RequiredArgsConstructor
public class DistrictClosureCommand implements Consumer<TextIO> {
    private final DistrictRepository districtRepository;

    @Override
    public void accept(TextIO textIO) {
        List<District> districts = this.districtRepository
                .findAll(Sort.by(Sort.Direction.ASC, "parentId", "id"));

        this.districtRepository.truncateClosures(new District());

        for (District district : districts) {
            this.districtRepository.insertClosures(district);
        }
    }

    @Override
    public String toString() {
        return "区域数据树形闭包表生成";
    }
}
