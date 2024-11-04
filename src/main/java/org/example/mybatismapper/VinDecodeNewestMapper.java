package org.example.mybatismapper;

import com.domain.VinDecodeNewest;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VinDecodeNewestMapper {
    int insert(VinDecodeNewest record);

    int insertSelective(VinDecodeNewest record);

    int batchInsert(@Param("list") List<VinDecodeNewest> list);

    List<VinDecodeNewest> selectByVin(@Param("vin")String vin);
}