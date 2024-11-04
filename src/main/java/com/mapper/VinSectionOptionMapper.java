package com.mapper;

import com.domain.VinSectionOption;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface VinSectionOptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VinSectionOption record);

    int insertSelective(VinSectionOption record);

    VinSectionOption selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VinSectionOption record);

    int updateByPrimaryKey(VinSectionOption record);

    int batchInsert(@Param("list") List<VinSectionOption> list);
}