package org.example.mybatismapper;

import com.domain.VinPartOptionNewestCopy2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VinPartOptionNewestCopy2Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(VinPartOptionNewestCopy2 record);

    int insertSelective(VinPartOptionNewestCopy2 record);

    VinPartOptionNewestCopy2 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VinPartOptionNewestCopy2 record);

    int updateByPrimaryKey(VinPartOptionNewestCopy2 record);

    int batchInsert(@Param("list") List<VinPartOptionNewestCopy2> list);

    int batchInsert3(@Param("list") List<VinPartOptionNewestCopy2> list);

    List<VinPartOptionNewestCopy2> selectAll();

	List<VinPartOptionNewestCopy2> selectFromStart(@Param("start") int start);

    List<VinPartOptionNewestCopy2> selectFromStartNew(@Param("start") int start, @Param("catalog") String catalog);
}