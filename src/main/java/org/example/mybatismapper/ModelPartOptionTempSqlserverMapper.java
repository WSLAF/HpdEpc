//package org.example.mybatismapper;
//
//import com.baomidou.dynamic.datasource.annotation.DS;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.domain.ModelPartOptionTempSqlserver;
//import java.util.List;
//import org.apache.ibatis.annotations.Param;
//import org.example.constant.DBConstants;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@DS(DBConstants.DATASOURCE_SQLSERVER)
//public interface ModelPartOptionTempSqlserverMapper extends BaseMapper<ModelPartOptionTempSqlserver> {
//    int insert(ModelPartOptionTempSqlserver record);
//
//    int insertSelective(ModelPartOptionTempSqlserver record);
//
//    int batchInsert(@Param("list") List<ModelPartOptionTempSqlserver> list);
//}