package cn.homyit.service;

import cn.homyit.entity.DO.Appoint;
import cn.homyit.entity.DTO.AppointPageDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author charon
* @description 针对表【tb_appoint(预约表)】的数据库操作Service
* @createDate 2023-04-05 23:36:38
*/
public interface AppointService extends IService<Appoint> {

    void saveAppoint(Appoint appoint);

    PageVo<?> appointList(AppointPageDto appointPageDto);

    void teacherUpdateAppoint(Appoint appoint);
}
