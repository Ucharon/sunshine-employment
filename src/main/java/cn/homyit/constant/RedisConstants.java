package cn.homyit.constant;

/**
 * @program: graduate-website
 * @description: redis前缀
 * @author: Charon
 * @create: 2023-03-25 17:50
 **/
public class RedisConstants {

    public static final String LOGIN_TOKEN_KEY = "user:token:";
    public static final String USER_CACHE_KEY = "cache:user:";
    //TODO token有效期暂时设置为一年
    public static final Integer LOGIN_USER_TTL = 60 * 60 * 24 * 365;
    //public static final Integer LOGIN_USER_TTL = 60 * 2;
    public static final Integer USER_CACHE_TTL = 12;
    public static final String LOGIN_STATUS_KEY = "user:status:";

    public static final String QRCODE_KEY = "qrcode:";
    public static final Integer QRCODE_TTL = 30;


    public static final Long CACHE_NULL_TTL = 2L;

    public static final String LOCK_USER_KEY = "lock:user:";
    public static final Integer LOCK_USER_TTL = 10;
    //public static final Integer LOGIN_USER_TTL = 60 * 24 * 14;

    public static final String GOODS_CACHE_KEY = "cache:goods:";
    public static final String GOODS_LOCK_KEY = "lock:goods:";
    public static final Long GOODS_CACHE_TTL = 12L;

    public static final String ACTIVITY_CACHE_KEY = "cache:activity:";
    public static final String ACTIVITY_LOCK_KEY = "lock:activity:";
    public static final Long ACTIVITY_CACHE_TTL = 12L;

    //打卡bitmap相关
    public static final String DATE_FORMATTER = ":yyyyMM";


    public static final String ACTIVITY_CLOCK_KEY = "clock:activity:";
    public static final String DAILY_CLOCK_KEY = "clock:daily:";

}
