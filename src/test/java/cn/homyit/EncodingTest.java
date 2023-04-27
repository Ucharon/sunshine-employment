package cn.homyit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @program: graduate-website
 * @description: 测试加密算法
 * @author: Charon
 * @create: 2023-03-25 16:38
 **/
@SpringBootTest
public class EncodingTest {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    void TestBCryptPasswordEncoder() {
        String encode = passwordEncoder.encode("1234");
        System.out.println(encode);
    }
}
