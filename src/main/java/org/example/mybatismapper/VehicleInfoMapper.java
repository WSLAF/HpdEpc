package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.VehicleInfo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface VehicleInfoMapper {
    int insert(VehicleInfo record);

    int insertSelective(VehicleInfo record);

    int batchInsert(@Param("list") List<VehicleInfo> list);
    List<VehicleInfo> selectByVin(@Param("vin")String vin);

}