package com.knowledgeplanet.forum.services;

import com.knowledgeplanet.forum.model.Board;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IBoardService {
    /**
     * 新建版块
     * @param board 版块信息
     */
    void create(Board board);

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
     * 根据版块Id查询版块详情
     * @param id 版块Id
     * @return Board 对象
     */
    Board selectById (Long id);

}
