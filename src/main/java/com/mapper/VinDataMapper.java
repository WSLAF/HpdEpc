package com.mapper;

import com.domain.VinData;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

public interface VinDataMapper {
    int insert(VinData record);

    int insertSelective(VinData record);

    int batchInsert(@Param("list") List<VinData> list);
}