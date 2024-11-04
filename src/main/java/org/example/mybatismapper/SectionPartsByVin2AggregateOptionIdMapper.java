package org.example.mybatismapper;

import com.domain.SectionPartsByVin2AggregateOptionId;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SectionPartsByVin2AggregateOptionIdMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SectionPartsByVin2AggregateOptionId record);

    int insertSelective(SectionPartsByVin2AggregateOptionId record);

    SectionPartsByVin2AggregateOptionId selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SectionPartsByVin2AggregateOptionId record);

    int updateByPrimaryKey(SectionPartsByVin2AggregateOptionId record);

    int batchInsert(@Param("list") List<SectionPartsByVin2AggregateOptionId> list);
}