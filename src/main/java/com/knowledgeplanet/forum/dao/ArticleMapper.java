package com.knowledgeplanet.forum.dao;

import com.knowledgeplanet.forum.model.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insert(Article row);

    int insertSelective(Article row);

    Article selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Article row);

    int updateByPrimaryKeyWithBLOBs(Article row);

    int updateByPrimaryKey(Article row);

    /**
     * 主页中显示的帖子列表以发布时间降序排列
     *
     * @return 帖子列表
     */
    List<Article> selectAll ();

    /**
     * 根据版块Id查询帖子列表以发布时间降序排列
     *
     * @param boardId 版块Id
     * @return 帖子列表
     */
    List<Article> selectByBoardId (Long boardId);

    /**
     * 根据Id查询帖子详情
     * @param id 帖子Id
     * @return 帖子详情
     */
    Article selectDetailById(Long id);

    /**
     * 根据用户Id查询帖子列表以发布时间降序排列
     * @param userId 用户Id
     * @return 用户发布的帖子集合
     */
    List<Article> selectByUserId (Long userId);
}