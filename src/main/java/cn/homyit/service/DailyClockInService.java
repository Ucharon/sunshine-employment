package cn.homyit.service;

import cn.homyit.entity.DO.DailyClockIn;
import cn.homyit.entity.DTO.DailyClockInPageDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author charon
* @description 针对表【tb_daily_clock_in】的数据库操作Service
* @createDate 2023-04-04 18:35:54
*/
public interface DailyClockInService extends IService<DailyClockIn> {

    PageVo<?> listDailyClockIn(DailyClockInPageDto pageDto);
}
