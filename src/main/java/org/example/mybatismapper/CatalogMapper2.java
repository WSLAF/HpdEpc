package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.Catalog;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface CatalogMapper2 {
    int insert(Catalog record);

    int insertSelective(Catalog record);

    int batchInsert(@Param("list") List<Catalog> list);

    List<Catalog> selectAll();

    List<String> selectCatalogCode();

    List<Catalog> selectByCatalogCode(@Param("catalogcode")String catalogcode);

}