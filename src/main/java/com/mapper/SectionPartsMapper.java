package com.mapper;

import com.domain.SectionParts;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SectionPartsMapper {
    int insert(SectionParts record);

    int insertSelective(SectionParts record);

    int batchInsert(@Param("list") List<SectionParts> list);
}