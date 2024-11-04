package org.example.mapper;
import java.util.Collection;

import com.domain.Vin;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VinMapper {
    int insert(Vin record);

    int insertSelective(Vin record);

    int batchInsert(@Param("list") List<Vin> list);

    List<String> selectByCatalogcodein(@Param("start")int start, @Param("end")int end);

    Vin selectByVincode(@Param("vincode")String vincode);

    Long selectMaxId();


}