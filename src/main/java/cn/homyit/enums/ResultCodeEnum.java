package cn.homyit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回结果枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    INTERNAL_SERVER_ERROR(500, "未知异常"),
    OBJECT_NOT_EXIST(501, "对象不存在"),
    ORDER_CODE_ERROR(502, "排序字段不存在"),

    //以下为失败枚举

    /**
     * 用户
     */
    NEED_LOGIN(600, "用户未登录"),
    LOGIN_ERROR(601, "用户名或密码错误或用户已停用"),
    LOGIN_INPUT_ERROR(602, "输入格式不正确"),
    LOGIN_INFORMATION_ILLEGAL(603, "登录信息失效"),
    TOKEN_ILLEGAL(604, "Token非法"),
    PASSWORD_ERROR(605, "密码错误！"),
    PASSWORD_NULL(606, "密码为空"),
    SAME_PASSWORD(606, "两次密码不能保持一致"),
    EMAIL_NULL(607, "用户未绑定邮箱，请联系管理员"),
    CODE_ERROR(608, "验证码错误"),
    ACCOUNT_DISABLE(610, "账号已停用"),
    QR_ERROR(611, "二维码操作异常"),
    QR_FAILURE(612, "二维码失效"),
    RESUME_NOT_UPLOADED(613, "个人简历未上传"),

    /**
     * 权限
     */
    FORBIDDEN(700, "权限不足"),
    INSUFFICIENT_INVENTORY(701, "库存不足，请联系管理员"),


    /**
     * 参数校验
     */
    ERROR_PARAM(900, "参数错误"),

    /**
     * 用户信息
     */
    USER_NOT_EXISTS(1000, "该账号不存在"),
    USER_REPETITION(1002, "角色重复"),
    USER_NOT_EXITS(1003, "用户不存在"),

    /**
     * EXCEL
     */
    EXCEL_FORMAT_ILLEGAL(1100, "EXCEL格式错误"),

    /**
     * 订单相关
     */
    INSUFFICIENT_SCORE(1200, "积分不足"),
    ORDER_NOT_EXIST(1201, "订单不存在"),

    /**
     * 商城相关
     */
    GOODS_NOT_EXIST(1300, "商品不存在或已下架！"),

    /**
     * 活动相关
     */
    ACTIVITY_NOT_EXIST(1400, "活动不存在"),
    DUPLICATE_REGISTRATION(1401, "重复报名"),
    ACTIVITY_IS_END(1402, "活动不存在或已结束"),
    ACTIVITY_OUT_OF_RANGE(1403, "活动不存在或不在活动时间范围"),
    NOT_SIGN(1404, "未报名该活动或重复打卡"),


    /**
     * 每日打卡相关
     */
    DUPLICATE_CLOCK_IN(1500, "今天已经打卡过了，请每天再试吧！"),

    /**
     * 图床
     */
    IMG_ERROR(4001, "上传图片协调出错"),

    /**
     * 预约
     */
    DUPLICATE_SUBMISSION(5001, "请勿重复提交"),
    TAG_DELETE_ERROR(5002, "删除失败，该标签下存在文章"),

    /**
     * 图片
     */
    IMG_NULL_ERROR(6001, "文件为空"),
    BUCKET_NOT_EXIST(6002, "存储库不存在"),

    /**
     * 文件
     */
    FILE_ERROR(7001, "文件系统出错"),
    FILE_DOWN_ERROR(7002, "文件下载出错"),
    FILE_NULL(7003, "找不到文件"),
    FILE_NAME_ERROR(2004, "文件名称出错");
    //状态码
    private final Integer code;
    //描述
    private final String desc;
}
