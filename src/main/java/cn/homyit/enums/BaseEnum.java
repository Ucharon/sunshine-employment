package cn.homyit.enums;


public interface BaseEnum<E extends Enum<?>, T> {

    //值，数据库中，以及程序中一般传递的都是这个值
    public T getCode();

    public String getName();
}
