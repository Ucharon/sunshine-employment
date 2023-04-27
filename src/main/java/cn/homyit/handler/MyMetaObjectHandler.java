package cn.homyit.handler;

import cn.homyit.enums.ZoneEnum;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
        this.setFieldValByName("deleteStatus", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
        this.strictUpdateFill(metaObject, "modifiedTime", LocalDateTime.class,
                LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
    }

}