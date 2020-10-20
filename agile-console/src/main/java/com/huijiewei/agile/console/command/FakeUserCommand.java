package com.huijiewei.agile.console.command;

import com.github.javafaker.Faker;
import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import com.huijiewei.agile.app.user.adapter.persistence.repository.JpaUserRepository;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import org.beryx.textio.TextIO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */
@Component
public class FakeUserCommand implements Consumer<TextIO> {
    private final JpaUserRepository userRepository;

    public FakeUserCommand(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void accept(TextIO textIO) {
        Integer count = textIO.newIntInputReader().withDefaultValue(10).read("数量");

        Faker englishFaker = new Faker(Locale.ENGLISH);
        Faker chineseFaker = new Faker(Locale.CHINA);

        Page<User> users = this.userRepository.findAll(PageRequest.of(0, 1, Sort.Direction.DESC, "id"));

        long fakeCreatedAtTimestamp = users.getContent().isEmpty()
                ? LocalDateTime.of(2019, 5, 1, 12, 20, 32).toEpochSecond(ZoneOffset.of("+8"))
                : users.getContent().get(0).getCreatedAt().toEpochSecond(ZoneOffset.of("+8"));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        List<String> createdFromList = Arrays.stream(UserCreatedFrom.values()).map(UserCreatedFrom::getValue).collect(Collectors.toList());

        for (int i = 0; i < count; i++) {
            String fakePhone = chineseFaker.phoneNumber().cellPhone();

            User existPhoneUser = new User();
            existPhoneUser.setPhone(fakePhone);

            if (this.userRepository.exists(Example.of(existPhoneUser))) {
                textIO.getTextTerminal().println("电话号码：" + fakePhone + " 已存在，跳过");
                continue;
            }

            String fakeEmail = englishFaker.internet().emailAddress();

            User existEmailUser = new User();
            existEmailUser.setEmail(fakeEmail);

            if (this.userRepository.exists(Example.of(existEmailUser))) {
                textIO.getTextTerminal().println("电子邮件：" + fakeEmail + " 已存在，跳过");
                continue;
            }

            Collections.shuffle(createdFromList);

            User user = new User();
            user.setPhone(fakePhone);
            user.setEmail(fakeEmail);
            user.setPassword(passwordEncoder.encode(englishFaker.internet().password()));
            user.setName(chineseFaker.name().fullName());
            user.setCreatedIp(chineseFaker.internet().ipV4Address());
            user.setCreatedFrom(createdFromList.get(0));

            fakeCreatedAtTimestamp += new Random().nextInt((28800 - 30) + 1) + 30;

            LocalDateTime fakeCreatedAt = LocalDateTime.ofEpochSecond(
                    fakeCreatedAtTimestamp,
                    new Random().nextInt((28800 - 30) + 1) + 30,
                    ZoneOffset.of("+8")
            );

            user.setCreatedAt(fakeCreatedAt);

            int randomInt = new Random().nextInt((100 - 1) + 1) + 1;

            if (randomInt > 9) {
                int randomAvatarId = new Random().nextInt((100677 - 100001) + 1) + 100001;
                String randomAvatar = Integer.toString(randomAvatarId).substring(1);

                user.setAvatar("https://yuncars-other.oss-cn-shanghai.aliyuncs.com/boilerplate/avatar/a" + randomAvatar + ".png@!avatar");
            }

            this.userRepository.save(user);
        }

        textIO.getTextTerminal().println("fake-user" + count.toString());
        textIO.dispose();
    }

    @Override
    public String toString() {
        return "生成用户数据";
    }
}
