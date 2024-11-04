package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.VinPartOption;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface VinPartOptionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VinPartOption record);

    int insertSelective(VinPartOption record);

    VinPartOption selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VinPartOption record);

    int updateByPrimaryKey(VinPartOption record);

    int batchInsert(@Param("list") List<VinPartOption> list);

    List<VinPartOption> selectByCatalogCodeAndSectionId(@Param("catalogCode")String catalogCode, @Param("sectionId") String sectionId);


}