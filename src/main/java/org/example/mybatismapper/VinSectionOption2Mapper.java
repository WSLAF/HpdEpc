package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.VinSectionOption2;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface VinSectionOption2Mapper {
    int insert(VinSectionOption2 record);

    int insertSelective(VinSectionOption2 record);

    int batchInsert(@Param("list") List<VinSectionOption2> list);

    List<VinSectionOption2> selectByUccCodesAndCatalogCodeAndSectionCode(@Param("uccCodes")List<String> uccCodes,@Param("catalogCode")String catalogCode,@Param("sectionCode")String sectionCode);


}