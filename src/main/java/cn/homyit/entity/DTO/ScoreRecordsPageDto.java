package cn.homyit.entity.DTO;

import cn.homyit.enums.ScoreRecordTypesEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-01 16:03
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ScoreRecordsPageDto extends PageDto {

    /**
     * 积分流水类型
     */
    private ScoreRecordTypesEnum type;

    /**
     * 订单开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 订单结束时间
     */
    private LocalDateTime endTime;
}
