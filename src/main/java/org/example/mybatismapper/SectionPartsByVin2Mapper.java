package org.example.mybatismapper;

import com.domain.SectionPartsByVin2;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SectionPartsByVin2Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(SectionPartsByVin2 record);

    int insertSelective(SectionPartsByVin2 record);

    SectionPartsByVin2 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SectionPartsByVin2 record);

    int updateByPrimaryKey(SectionPartsByVin2 record);

    int batchInsert(@Param("list") List<SectionPartsByVin2> list);
    List<SectionPartsByVin2> selectByCatalogCode(@Param("catalogCode")String catalogCode, @Param("start")long start);


}