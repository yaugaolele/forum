package com.knowledgeplanet.forum.services.impl;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.dao.ArticleMapper;
import com.knowledgeplanet.forum.dao.ArticleReplyMapper;
import com.knowledgeplanet.forum.exception.ApplicationException;
import com.knowledgeplanet.forum.model.Article;
import com.knowledgeplanet.forum.model.ArticleReply;
import com.knowledgeplanet.forum.services.IArticleReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


// 日志注解
@Slf4j
// 加入到Spring容器
@Service
public class ArticleReplyServiceImpl implements IArticleReplyService {

    // 注入DAO层
    @Resource
    private ArticleReplyMapper articleReplyMapper;
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void create(ArticleReply articleReply) {
        // 非空校验
        if (articleReply == null || articleReply.getArticleId() == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询帖子信息
        Article article = articleMapper.selectByPrimaryKey(articleReply.getArticleId());
        // 校验帖子信息
        if (article == null) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString() + ". articleId = " + articleReply.getArticleId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        if (article.getState() != 0 || article.getDeleteState() != 0) {
            log.warn("帖子状态异常. articleId = " + article.getId() + ", state = " + article.getState() + ", delete state = "
                    + article.getDeleteState());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 填充默认数据
        articleReply.setState((byte) 0); // 状态
        articleReply.setDeleteState((byte) 0); // 是否删除
        articleReply.setLikeCount(0); // 点赞数量
        // 时间
        Date date = new Date();
        articleReply.setCreateTime(date); // 创建时间
        articleReply.setUpdateTime(date); // 更新时间
        // 写入回复数据
        int row = articleReplyMapper.insertSelective(articleReply);
        if (row != 1) {
            // 记录日志
            log.info("新增帖子回复失败, userId = " + articleReply.getPostUserId() + ", articleId = " + articleReply.getArticleId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 更新帖子回复数
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setReplyCount(article.getReplyCount() + 1);
        articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            // 记录日志
            log.info("帖子回复数量更新失败, userId = " + articleReply.getPostUserId() + ", articleId = "
                    + articleReply.getArticleId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 日志
        log.info("新增帖子回复成功, userId = " + articleReply.getPostUserId() + ", articleId = " + articleReply.getArticleId() + ", articleReplyId = " + articleReply.getId());
    }

    @Override
    public List<ArticleReply> selectByArticleId(Long articleId) {
        // 非空校验
        if (articleId == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询帖子是否存在
        Article article = articleMapper.selectByPrimaryKey(articleId);
        if (article == null || article.getState() == 1 || article.getDeleteState() == 1) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO层查询结果
        List<ArticleReply> result = articleReplyMapper.selectByArticleId(articleId);
        // 返回结果
        return result;
    }
}
