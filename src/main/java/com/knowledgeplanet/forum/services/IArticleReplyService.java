package com.knowledgeplanet.forum.services;

import com.knowledgeplanet.forum.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IArticleReplyService {
    /**
     * 新增回复
     *
     * @param articleReply 回复信息
     */
    // 事务管理
    @Transactional
    void create(ArticleReply articleReply);

    /**
     * 查询帖子对应的回复
     * @param articleId 帖子Id
     * @return
     */
    List<ArticleReply> selectByArticleId (Long articleId);

}
