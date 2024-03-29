package com.huijiewei.agile.console.command;

import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import com.huijiewei.agile.app.user.adapter.persistence.repository.UserRepository;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.beryx.textio.TextIO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */
@Component
@RequiredArgsConstructor
public class UserFakeCommand implements Consumer<TextIO> {
    private final UserRepository userRepository;

    @Override
    public void accept(TextIO textIO) {
        var count = textIO.newIntInputReader().withDefaultValue(10).read("生成数量");

        var englishFaker = new Faker(Locale.ENGLISH);
        var chineseFaker = new Faker(Locale.CHINA);

        var users = this.userRepository.findAll(PageRequest.of(0, 1, Sort.Direction.DESC, "id"));

        long fakeCreatedAtTimestamp = users.getContent().isEmpty()
                ? LocalDateTime.of(2019, 5, 1, 12, 20, 32).toEpochSecond(ZoneOffset.of("+8"))
                : users.getContent().getFirst().getCreatedAt().toEpochSecond(ZoneOffset.of("+8"));

        var passwordEncoder = new BCryptPasswordEncoder();

        var createdFromList = Arrays.stream(UserCreatedFrom.values()).map(UserCreatedFrom::getValue).collect(Collectors.toList());

        for (int i = 0; i < count; i++) {
            var fakePhone = chineseFaker.phoneNumber().cellPhone();

            var existPhoneUser = new User();
            existPhoneUser.setPhone(fakePhone);

            if (this.userRepository.exists(Example.of(existPhoneUser))) {
                textIO.getTextTerminal().println("电话号码：" + fakePhone + " 已存在，跳过");
                continue;
            }

            var fakeEmail = englishFaker.internet().emailAddress();

            var existEmailUser = new User();
            existEmailUser.setEmail(fakeEmail);

            if (this.userRepository.exists(Example.of(existEmailUser))) {
                textIO.getTextTerminal().println("电子邮件：" + fakeEmail + " 已存在，跳过");
                continue;
            }

            Collections.shuffle(createdFromList);

            var user = new User();
            user.setPhone(fakePhone);
            user.setEmail(fakeEmail);
            user.setPassword(passwordEncoder.encode(englishFaker.internet().password()));
            user.setName(chineseFaker.name().fullName());
            user.setCreatedIp(chineseFaker.internet().ipV4Address());
            user.setCreatedFrom(createdFromList.getFirst());

            fakeCreatedAtTimestamp += new Random().nextInt((28800 - 30) + 1) + 30;

            var fakeCreatedAt = LocalDateTime.ofEpochSecond(
                    fakeCreatedAtTimestamp,
                    new Random().nextInt((28800 - 30) + 1) + 30,
                    ZoneOffset.of("+8")
            );

            user.setCreatedAt(fakeCreatedAt);

            var randomInt = new Random().nextInt((100 - 1) + 1) + 1;

            if (randomInt > 9) {
                var randomAvatarId = new Random().nextInt((100677 - 100001) + 1) + 100001;
                var randomAvatar = Integer.toString(randomAvatarId).substring(1);

                user.setAvatar("https://yuncars-other.oss-cn-shanghai.aliyuncs.com/boilerplate/avatar/a" + randomAvatar + ".png@!avatar");
            }

            this.userRepository.save(user);
        }

        textIO.getTextTerminal().println("生成用户数据：" + count);
        textIO.dispose();
    }

    @Override
    public String toString() {
        return "用户数据生成";
    }
}
