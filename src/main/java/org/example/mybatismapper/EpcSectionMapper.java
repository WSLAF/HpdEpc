package org.example.mybatismapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.example.constant.DBConstants;
import org.example.mybatismapper.SelectParentuniqueidAndCatalogcodeAndApplicableResult;

import com.domain.EpcSection;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface EpcSectionMapper {
    int insert(EpcSection record);

    int insertSelective(EpcSection record);

    int batchInsert(@Param("list") List<EpcSection> list);
    List<EpcSection> selectByCatalogcodeAndApplicableAndParentuniqueid(@Param("catalogcode")String catalogcode,@Param("applicable")String applicable,@Param("parentuniqueid")String parentuniqueid);
    Set<String> selectCatalogcode();

}