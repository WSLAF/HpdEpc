package org.example.mapper;

import com.domain.VinPartOptionNew;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VinPartOptionNewMapper {
    int insert(VinPartOptionNew record);

    int insertSelective(VinPartOptionNew record);

    int batchInsert(@Param("list") List<VinPartOptionNew> list);
    List<VinPartOptionNew> selectAll();

}