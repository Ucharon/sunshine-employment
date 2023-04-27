package cn.homyit.service.impl;

import cn.homyit.entity.DO.User;
import cn.homyit.entity.DTO.ScoreRecordsPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.enums.ScoreRecordTypesEnum;
import cn.homyit.service.UserService;
import cn.homyit.utils.UserUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.homyit.entity.DO.ScoreRecords;
import cn.homyit.service.ScoreRecordsService;
import cn.homyit.mapper.ScoreRecordsMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author charon
 * @description 针对表【score_records】的数据库操作Service实现
 * @createDate 2023-04-01 16:06:13
 */
@Service
public class ScoreRecordsServiceImpl extends ServiceImpl<ScoreRecordsMapper, ScoreRecords>
        implements ScoreRecordsService {

    /**
     * 该方法是将积分流水信息存入表中
     * 该方法异步执行
     *
     * @param score
     * @param id
     * @param activityIn
     */
    @Override
    @Async
    public void insertRecord(Long beforeScore, Long score, Long id, ScoreRecordTypesEnum activityIn) {
        User user = UserUtils.getUser().getUser();
        ScoreRecords scoreRecords = new ScoreRecords();
        scoreRecords.setId(id);
        scoreRecords.setType(activityIn);
        scoreRecords.setUserId(user.getId());
        scoreRecords.setScoreBefore(beforeScore);
        scoreRecords.setScoreAfter(beforeScore + score);
        //插入
        save(scoreRecords);
    }

    @Override
    @Async
    public void insertRecord(Long beforeScore, Long score, Long id, ScoreRecordTypesEnum activityIn, Long userId) {
        ScoreRecords scoreRecords = new ScoreRecords();
        if (Objects.nonNull(id)) {
            scoreRecords.setId(id);
        }
        scoreRecords.setType(activityIn);
        scoreRecords.setUserId(userId);
        scoreRecords.setScoreBefore(beforeScore);
        scoreRecords.setScoreAfter(beforeScore + score);
        //插入
        save(scoreRecords);
    }


    @Override
    public PageVo<?> getScoreRecords(ScoreRecordsPageDto scoreRecordsPageDto) {
        Page<ScoreRecords> page = new Page<>(scoreRecordsPageDto.getPageNum(), scoreRecordsPageDto.getPageSize());
        lambdaQuery()
                //查询类型
                .eq(Objects.nonNull(scoreRecordsPageDto.getType()),
                        ScoreRecords::getType, scoreRecordsPageDto.getType())
                //指定开始时间
                .ge(Objects.nonNull(scoreRecordsPageDto.getBeginTime()),
                        ScoreRecords::getCreateTime, scoreRecordsPageDto.getBeginTime())
                .le(Objects.nonNull(scoreRecordsPageDto.getEndTime()),
                        ScoreRecords::getCreateTime, scoreRecordsPageDto.getEndTime())
                .orderByDesc(ScoreRecords::getCreateTime)
                .page(page);
        List<ScoreRecords> scoreRecordsList = page.getRecords();
        return new PageVo<>(scoreRecordsList, page.getTotal());
    }
}




