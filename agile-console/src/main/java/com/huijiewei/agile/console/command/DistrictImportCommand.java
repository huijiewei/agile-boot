package com.huijiewei.agile.console.command;

import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.app.district.adapter.persistence.repository.DistrictJpaRepository;
import com.huijiewei.agile.app.district.application.port.outbound.DistrictPersistencePort;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import lombok.SneakyThrows;
import org.beryx.textio.TextIO;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.util.function.Consumer;

/**
 * @author huijiewei
 */

@Component
public class DistrictImportCommand implements Consumer<TextIO> {
    private final DistrictPersistencePort districtPersistencePort;
    private final DistrictJpaRepository districtJpaRepository;

    public DistrictImportCommand(DistrictPersistencePort districtPersistencePort, DistrictJpaRepository districtJpaRepository) {
        this.districtPersistencePort = districtPersistencePort;
        this.districtJpaRepository = districtJpaRepository;
    }

    @SneakyThrows
    @Override
    public void accept(TextIO textIO) {
        this.districtJpaRepository.truncateClosures(new District());

        var connection = DriverManager.getConnection("jdbc:sqlite:./database/district.sqlite");
        var provinces = connection.createStatement().executeQuery("SELECT * FROM province");

        while (provinces.next()) {
            DistrictEntity province = new DistrictEntity();
            province.setParentId(0);
            province.setName(provinces.getString("name"));
            province.setCode(provinces.getString("code"));

            Integer provinceId = this.districtPersistencePort.save(province);

            var citiesPs = connection.prepareStatement("SELECT * FROM city WHERE provinceCode = ?");
            citiesPs.setString(1, province.getCode());

            var cities = citiesPs.executeQuery();

            while (cities.next()) {
                DistrictEntity city = new DistrictEntity();
                city.setParentId(provinceId);
                city.setName(cities.getString("name"));
                city.setCode(cities.getString("code"));

                Integer cityId = this.districtPersistencePort.save(city);

                var areasPs = connection.prepareStatement("SELECT * FROM area WHERE cityCode = ?");
                areasPs.setString(1, city.getCode());

                var areas = areasPs.executeQuery();

                while (areas.next()) {
                    DistrictEntity area = new DistrictEntity();
                    area.setParentId(cityId);
                    area.setName(areas.getString("name"));
                    area.setCode(areas.getString("code"));

                    Integer areaId = this.districtPersistencePort.save(area);

                    var streetsPs = connection.prepareStatement("SELECT * FROM street WHERE areaCode = ?");
                    streetsPs.setString(1, area.getCode());

                    var streets = streetsPs.executeQuery();

                    while (streets.next()) {
                        DistrictEntity street = new DistrictEntity();
                        street.setParentId(areaId);
                        street.setName(streets.getString("name"));
                        street.setCode(streets.getString("code"));

                        this.districtPersistencePort.save(street);
                    }
                }
            }

            textIO.getTextTerminal().println(province.getName() + "导入成功");
        }

        provinces.close();
        connection.close();

    }

    @Override
    public String toString() {
        return "区域导入";
    }
}
