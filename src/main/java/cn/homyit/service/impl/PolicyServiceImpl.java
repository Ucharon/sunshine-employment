package cn.homyit.service.impl;

import cn.homyit.entity.DO.Direct;
import cn.homyit.entity.DTO.PolicyPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.service.DirectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.Policy;
import cn.homyit.service.PolicyService;
import cn.homyit.mapper.PolicyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author charon
 * @description 针对表【tb_policy】的数据库操作Service实现
 * @createDate 2023-04-08 12:59:29
 */
@Service
public class PolicyServiceImpl extends ServiceImpl<PolicyMapper, Policy>
        implements PolicyService {

    @Resource
    private DirectService directService;

    @Override
    public PageVo<?> listPolicy(PolicyPageDto policyPageDto) {
        Page<Policy> page = new Page<>(policyPageDto.getPageNum(), policyPageDto.getPageSize());
        lambdaQuery()
                .eq(Objects.nonNull(policyPageDto.getDirectId()),
                        Policy::getDirectId, policyPageDto.getDirectId())
                .like(Objects.nonNull(policyPageDto.getName()),
                        Policy::getName, policyPageDto.getName())
                .orderByDesc(Policy::getUpdateTime)
                .page(page);
        //封装方向信息
        Map<Integer, String> map = directService.list().stream()
                .collect(Collectors.toMap(Direct::getId, Direct::getName));
        List<Policy> records = page.getRecords();
        records.forEach(policy -> policy.setDirectName(map.get(policy.getDirectId())));

        return new PageVo<>(records, page.getTotal());
    }
}




