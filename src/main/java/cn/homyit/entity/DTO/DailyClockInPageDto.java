package cn.homyit.entity.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-04-08 14:54
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class DailyClockInPageDto extends PageDto {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 时间范围
     */
    private LocalDateTime beginTime;
    private LocalDateTime endTime;


}
