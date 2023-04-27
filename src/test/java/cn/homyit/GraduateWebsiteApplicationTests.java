package cn.homyit;

import cn.homyit.entity.DO.Goods;
import cn.homyit.service.AppointService;
import cn.homyit.service.GoodsService;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GraduateWebsiteApplicationTests {

    @Resource
    private GoodsService goodsService;
    @Resource
    private AppointService appointService;

    @Test
    void contextLoads() {
        List<String> urlList = new ArrayList<>();
        urlList.add("https://typora-1312272916.cos.ap-shanghai.myqcloud.com//imgMultiavatar-6c0cda4f1a413ab2.png");
        urlList.add("https://typora-1312272916.cos.ap-shanghai.myqcloud.com//imgFAE3028BAC500F707EAC913E37A7D7EF.jpg");
        JSON parse = JSONUtil.parse(urlList);
        String s = JSONUtil.toJsonStr(parse);
        goodsService.lambdaUpdate()
                .set(Goods::getDetailedPicture, s)
                .update();
    }

    @Test
    void test01() {
        System.out.println(appointService.getById(1643874322126741505L));
    }

}
