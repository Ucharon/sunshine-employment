package cn.homyit.service;

import cn.homyit.entity.DO.ScoreRecords;
import cn.homyit.entity.DTO.ScoreRecordsPageDto;
import cn.homyit.entity.VO.PageVo;
import cn.homyit.enums.ScoreRecordTypesEnum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author charon
* @description 针对表【score_records】的数据库操作Service
* @createDate 2023-04-01 16:06:13
*/
public interface ScoreRecordsService extends IService<ScoreRecords> {

    void insertRecord(Long beforeScore, Long score, Long id, ScoreRecordTypesEnum activityIn);

    void insertRecord(Long beforeScore, Long score, Long id, ScoreRecordTypesEnum activityIn, Long userId);

    PageVo<?> getScoreRecords(ScoreRecordsPageDto scoreRecordsPageDto);
}
