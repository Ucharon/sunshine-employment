package cn.homyit.utils;

import cn.homyit.enums.ZoneEnum;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-31 20:18
 **/
public class TimeUtils {

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone()));
    }
}
