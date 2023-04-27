package cn.homyit.service;

import cn.homyit.entity.DO.Policy;
import cn.homyit.entity.DTO.PolicyPageDto;
import cn.homyit.entity.VO.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author charon
* @description 针对表【tb_policy】的数据库操作Service
* @createDate 2023-04-08 12:59:29
*/
public interface PolicyService extends IService<Policy> {

    PageVo<?> listPolicy(PolicyPageDto policyPageDto);
}
