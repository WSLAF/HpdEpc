package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.FlatSections;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface FlatSectionsMapper {
    int insert(FlatSections record);

    int insertSelective(FlatSections record);

    int batchInsert(@Param("list") List<FlatSections> list);

    List<FlatSections> selectAllByCatalogCode(@Param("catalogCode")String catalogCode);

    List<String> selectCatalogCode();



}