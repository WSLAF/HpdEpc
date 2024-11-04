package org.example.mybatismapper;

import com.domain.ModelSectionPartsTemp1;
import java.util.List;

import com.domain.ModelSectionPartsTemp1Newest;
import org.apache.ibatis.annotations.Param;
import org.example.model.SectionPartNewDto;

public interface ModelSectionPartsTemp1Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(ModelSectionPartsTemp1 record);

    int insertSelective(ModelSectionPartsTemp1 record);

    ModelSectionPartsTemp1 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ModelSectionPartsTemp1 record);

    int updateByPrimaryKey(ModelSectionPartsTemp1 record);

    int batchInsert(@Param("list") List<ModelSectionPartsTemp1> list);

    int batchInsert2(@Param("list") List<ModelSectionPartsTemp1> list);

    int batchInsertNewest(@Param("list") List<ModelSectionPartsTemp1Newest> list);

    List<SectionPartNewDto> selectAggregateOptionId(@Param("catalog") String catalog, @Param("start") int from, @Param("limit") int limit);

    int batchInsertPartByModelNew(@Param("list") List<ModelSectionPartsTemp1> list);

    int batchInsertPartByModelNew2(@Param("list") List<ModelSectionPartsTemp1> list);

}