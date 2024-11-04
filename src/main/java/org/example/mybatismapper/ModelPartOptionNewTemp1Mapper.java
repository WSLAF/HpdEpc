package org.example.mybatismapper;

import com.domain.ModelPartOptionNewTemp1;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModelPartOptionNewTemp1Mapper {
    int insert(ModelPartOptionNewTemp1 record);

    int insertSelective(ModelPartOptionNewTemp1 record);

    int batchInsert(@Param("list") List<ModelPartOptionNewTemp1> list);

    int batchInsert2(@Param("list") List<ModelPartOptionNewTemp1> list);

    int batchInsertModelPartOptionNewestTemp2(@Param("list") List<ModelPartOptionNewTemp1> list);
    int batchInsertModelPartOptionNewestTemp3(@Param("list") List<ModelPartOptionNewTemp1> list);

    int selectMaxId();
}