package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.VinSectionOption;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface VinSectionOptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VinSectionOption record);

    int insertSelective(VinSectionOption record);

    VinSectionOption selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VinSectionOption record);

    int updateByPrimaryKey(VinSectionOption record);

    int batchInsert(@Param("list") List<VinSectionOption> list);
}