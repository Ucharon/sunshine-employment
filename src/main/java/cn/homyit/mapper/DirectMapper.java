package cn.homyit.mapper;

import cn.homyit.entity.DO.Direct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author charon
* @description 针对表【tb_direct(方向表)】的数据库操作Mapper
* @createDate 2023-03-26 16:29:16
* @Entity cn.homyit.entity.DO.Direct
*/
@Mapper
public interface DirectMapper extends BaseMapper<Direct> {

}




