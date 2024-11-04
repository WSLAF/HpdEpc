package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.example.constant.DBConstants;
import org.example.model.SectionOptionApplicability;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface SectionOptionApplicabilityMapper {
    int insert(SectionOptionApplicability record);

    int insertSelective(SectionOptionApplicability record);

    int batchInsert(@Param("list") List<SectionOptionApplicability> list);
}