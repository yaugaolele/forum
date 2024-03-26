package com.knowledgeplanet.forum.dao;

import com.knowledgeplanet.forum.model.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insert(Board row);

    int insertSelective(Board row);

    Board selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Board row);

    int updateByPrimaryKey(Board row);

    /**
     * 查询前 N个正常状态的版块
     * @param num 查询的个数
     * @return 前N个版块的集合
     */
    List<Board> selectTopByNum (@Param("num") Integer num);

    /**
     * 查询所有正常状态的版块
     *
     * @return 所有正常状态的版块
     */
    List<Board> selectAllNormal ();

    /**
     * 帖子数加1
     * @param id
     * @return
     */
    int updateArticleCountById(Long id);
}