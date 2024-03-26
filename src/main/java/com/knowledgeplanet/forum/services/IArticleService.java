package com.knowledgeplanet.forum.services;

import com.knowledgeplanet.forum.model.Article;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IArticleService {

    /**
     * 新增帖子
     * @param article 帖子信息
     */
    // 事务管理
    @Transactional
    void create(Article article);

    /**
     * 按Id查询帖子详情
     *
     * @param id 帖子Id
     * @return 帖子详情
     */
    Article selectById(Long id);

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
     * 根据帖子Id更新帖子标题与内容
     * @param id 帖子Id
     * @param title 标题
     * @param content 内容
     */
    void modify(Long id, String content);

    /**
     * 根据用户Id查询帖子列表以发布时间降序排列
     * @param userId 用户Id
     * @return 用户发布的帖子集合
     */
    List<Article> selectByUserId (Long userId);

    /**
     * 点赞
     * @param id 帖子Id
     */
    void thumbsUpById (Long id);

    /**
     * 删除
     * @param id 帖子Id
     */
    void deleteById (Long id);
}
