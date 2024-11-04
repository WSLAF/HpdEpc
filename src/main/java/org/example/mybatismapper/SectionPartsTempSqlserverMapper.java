//package org.example.mybatismapper;
//
//import com.baomidou.dynamic.datasource.annotation.DS;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.domain.SectionPartsTempSqlserver;
//import org.apache.ibatis.annotations.Param;
//import org.example.constant.DBConstants;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@DS(DBConstants.DATASOURCE_SQLSERVER)
//public interface SectionPartsTempSqlserverMapper extends BaseMapper<SectionPartsTempSqlserver> {
//    int deleteByPrimaryKey(Long id);
//
//    int insert(SectionPartsTempSqlserver record);
//
//    int insertSelective(SectionPartsTempSqlserver record);
//
//    SectionPartsTempSqlserver selectByPrimaryKey(Long id);
//
//    int updateByPrimaryKeySelective(SectionPartsTempSqlserver record);
//
//    int updateByPrimaryKey(SectionPartsTempSqlserver record);
//
//    int batchInsert(@Param("list") List<SectionPartsTempSqlserver> list);
//
//}