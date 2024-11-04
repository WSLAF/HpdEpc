package org.example.mybatismapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.domain.ModelPartOption;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.constant.DBConstants;
import org.springframework.stereotype.Repository;

@Repository
@DS(DBConstants.DATASOURCE_MYSQL)
public interface ModelPartOptionMapper {
    int insert(ModelPartOption record);

    int insertSelective(ModelPartOption record);

    int batchInsert(@Param("list") List<ModelPartOption> list);
    int batchInsert2(@Param("list") List<ModelPartOption> list);
    int batchInsert3(@Param("list") List<ModelPartOption> list);
    int batchInsert4(@Param("list") List<ModelPartOption> list);
    int batchInsert5(@Param("list") List<ModelPartOption> list);
    int batchInsert6(@Param("list") List<ModelPartOption> list);
    int batchInsert7(@Param("list") List<ModelPartOption> list);
    int batchInsert8(@Param("list") List<ModelPartOption> list);
    int batchInsert9(@Param("list") List<ModelPartOption> list);
    int batchInsert10(@Param("list") List<ModelPartOption> list);
    int batchInsert11(@Param("list") List<ModelPartOption> list);
    int batchInsert12(@Param("list") List<ModelPartOption> list);
    int batchInsert13(@Param("list") List<ModelPartOption> list);
    int batchInsert14(@Param("list") List<ModelPartOption> list);
    int batchInsert15(@Param("list") List<ModelPartOption> list);

    Long selectMaxId();

    Long selectMaxId2();
}