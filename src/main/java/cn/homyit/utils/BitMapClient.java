package cn.homyit.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.homyit.constant.RedisConstants.DATE_FORMATTER;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-04 18:04
 **/

@Component
public class BitMapClient {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @param keyPrefix 前缀
     * @return 返回值为设置前的值
     */
    public Boolean recordsClockIn(String keyPrefix) {
        //1. 获取当前登录情况
        Long userId = UserUtils.getUser().getUser().getId();
        //2. 获取日期
        LocalDateTime now = TimeUtils.getCurrentTime();
        //3. 拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        String key = keyPrefix + userId + keySuffix;
        //4. 获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        //5. 写入redis setbit key offset 1
        return stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
    }

    /**
     * 根据年月返回当月打卡情况
     *
     * @param keyPrefix 前缀
     * @param yearMonth 年月
     * @return 打卡情况
     */
    public Map<Integer, Boolean> getClockIn(String keyPrefix, String yearMonth) {
        Long userId = UserUtils.getUser().getUser().getId();
        String key = keyPrefix + userId + ":" + yearMonth;
        List<Long> result = stringRedisTemplate.opsForValue().bitField(key,
                BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(32)).valueAt(0));
        if (CollectionUtil.isEmpty(result)) {
            return null;
        }
        Long num = result.get(0);
        if (Objects.isNull(num) || num == 0) {
            return null;
        }
        //取出每一位
        Map<Integer, Boolean> map = new HashMap<>();
        for (int i = 32; i >= 1; i--) {
            if ((num & 1) == 0) {
                map.put(i, false);
            } else {
                map.put(i, true);
            }
            num >>>= 1;
        }

        return map;
    }


}
