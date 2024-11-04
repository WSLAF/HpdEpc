package com.mapper;

import com.domain.Catalog;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CatalogMapper {
    int insert(Catalog record);

    int insertSelective(Catalog record);

    int batchInsert(@Param("list") List<Catalog> list);
}