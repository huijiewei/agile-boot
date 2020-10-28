package com.huijiewei.agile.console.command;

import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.app.district.adapter.persistence.repository.DistrictJpaRepository;
import org.beryx.textio.TextIO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author huijiewei
 */

@Component
public class DistrictClosureCommand implements Consumer<TextIO> {
    private final DistrictJpaRepository districtJpaRepository;

    public DistrictClosureCommand(DistrictJpaRepository districtJpaRepository) {
        this.districtJpaRepository = districtJpaRepository;
    }

    @Override
    public void accept(TextIO textIO) {
        List<District> districts = this.districtJpaRepository
                .findAll(Sort.by(Sort.Direction.ASC, "parentId", "id"));

        this.districtJpaRepository.truncateClosures(new District());

        for (District district : districts) {
            this.districtJpaRepository.insertClosures(district);
        }
    }

    @Override
    public String toString() {
        return "地区树形闭包表生成";
    }
}
