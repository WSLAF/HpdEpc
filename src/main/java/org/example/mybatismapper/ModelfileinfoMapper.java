package org.example.mybatismapper;

import com.domain.Modelfileinfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ModelfileinfoMapper {
    int insert(Modelfileinfo record);

    int insertSelective(Modelfileinfo record);

    int batchInsert(@Param("list") List<Modelfileinfo> list);
    List<Modelfileinfo> selectAllByCataloguecode(@Param("cataloguecode")String cataloguecode);
}