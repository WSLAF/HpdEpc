package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.VinData;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.example.model.VinDataDto;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface VinDataMapper {
    int insert(VinData record);

    int insertSelective(VinData record);
    int batchInsert(@Param("list") List<VinData> list);
    List<VinData> selectByCatalog(@Param("catalog")String catalog);
    List<VinDataDto> selectByCatalog2(@Param("catalog")String catalog, @Param("start") int start);

}