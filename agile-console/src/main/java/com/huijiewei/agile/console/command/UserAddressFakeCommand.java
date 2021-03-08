package com.huijiewei.agile.console.command;

import com.github.javafaker.Faker;
import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import com.huijiewei.agile.app.user.adapter.persistence.entity.UserAddress;
import com.huijiewei.agile.app.user.adapter.persistence.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.beryx.textio.TextIO;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author huijiewei
 */

@Component
@RequiredArgsConstructor
public class UserAddressFakeCommand implements Consumer<TextIO> {
    private final EntityManager entityManager;
    private final UserAddressRepository userAddressRepository;

    @Override
    @SuppressWarnings("unchecked")
    public void accept(TextIO textIO) {
        var count = textIO.newIntInputReader().withDefaultValue(10).read("生成数量");

        var chineseFaker = new Faker(Locale.CHINA);

        var users = (List<User>) this.entityManager
                .createNativeQuery(String.format("SELECT * FROM %s ORDER BY RAND() LIMIT :count", User.tableName(User.class)), User.class)
                .setParameter("count", count)
                .getResultList();

        var districts = (List<District>) this.entityManager
                .createNativeQuery(String.format("SELECT * FROM %s WHERE LENGTH(code) = :length ORDER BY RAND() LIMIT :count", District.tableName(District.class)), District.class)
                .setParameter("length", DistrictEntity.LEAF_CODE_LENGTH)
                .setParameter("count", count)
                .getResultList();

        for (int i = 0; i < count; i++) {
            var userAddress = new UserAddress();
            userAddress.setUserId(users.get(i).getId());
            userAddress.setDistrictCode(districts.get(i).getCode());
            userAddress.setName(users.get(i).getName());
            userAddress.setPhone(users.get(i).getPhone());
            userAddress.setAlias(chineseFaker.address().cityName());
            userAddress.setAddress(chineseFaker.address().streetAddress(true));

            this.userAddressRepository.save(userAddress);

            textIO.getTextTerminal().println("生成成功");
        }
    }

    @Override
    public String toString() {
        return "用户地址数据生成";
    }
}
