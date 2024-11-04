package org.example.mybatismapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.HpdEpcPartByVinDto;
import com.domain.HpdEpcPartDto;
import com.domain.SectionParts;

import com.domain.SectionPartsByModelUltimate;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.example.model.SectionPart;
import org.example.model.SectionPartDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface SectionPartsMapper {
    int insert(SectionPart record);

    int insertSelective(SectionPart record);

    int batchInsert(@Param("list") List<SectionPart> list);
    int batchInsert2(@Param("list") List<SectionPart> list);
    int batchInsert3(@Param("list") List<SectionPart> list);
    int batchInsert4(@Param("list") List<SectionPart> list);
    int batchInsert5(@Param("list") List<SectionPart> list);
    int batchInsert6(@Param("list") List<SectionPart> list);
    int batchInsert7(@Param("list") List<SectionPart> list);
    int batchInsert8(@Param("list") List<SectionPart> list);
    int batchInsert9(@Param("list") List<SectionPart> list);
    int batchInsert10(@Param("list") List<SectionPart> list);
    int batchInsert11(@Param("list") List<SectionPart> list);

    int batchInsert12(@Param("list") List<SectionPart> list);

    int batchInsert13(@Param("list") List<SectionPart> list);

    int batchInsert14(@Param("list") List<SectionPart> list);

    int batchInsert15(@Param("list") List<SectionPart> list);
    int batchInsertUltimate(@Param("list") List<SectionPartsByModelUltimate> list);

    List<HpdEpcPartDto> select(@Param("catalog") String catalog, @Param("start") Integer start);
    List<HpdEpcPartByVinDto> selectPartByVin(@Param("catalog") String catalog, @Param("start") Integer start);
    List<HpdEpcPartByVinDto> selectPartByVinNew(@Param("catalog") String catalog, @Param("start") Integer start);
    List<SectionPartDto> selectPartByModel(@Param("catalog") String catalog, @Param("start") Long start);

    List<SectionPartDto> selectPartByModelNew(@Param("catalog") String catalog, @Param("start") Long start);

    List<SectionPartDto> selectPartByModelNew2(@Param("catalog") String catalog, @Param("start") Long start);

    List<SectionPartDto> selectPartById(@Param("id") Integer id);


}