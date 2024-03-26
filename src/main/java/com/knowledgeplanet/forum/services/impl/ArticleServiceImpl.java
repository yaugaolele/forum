package com.knowledgeplanet.forum.services.impl;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.dao.ArticleMapper;
import com.knowledgeplanet.forum.dao.BoardMapper;
import com.knowledgeplanet.forum.dao.UserMapper;
import com.knowledgeplanet.forum.exception.ApplicationException;
import com.knowledgeplanet.forum.model.Article;
import com.knowledgeplanet.forum.services.IArticleService;
import com.knowledgeplanet.forum.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


// 日志注解
@Slf4j
// 加入到Spring容器
@Service
public class ArticleServiceImpl implements IArticleService {

    // 注入DAO层
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserMapper userMapper;

    @Resource
    private BoardMapper boardMapper;

    @Override
    public void create(Article article) {
        // 非空校验
        if (article == null || article.getUserId() == null || article.getBoardId() == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }

        // 填充默认数据
        article.setVisitCount(0); // 访问数量
        article.setReplyCount(0); // 回复数量
        article.setLikeCount(0); // 点赞数量
        article.setState((byte) 0); // 状态
        article.setDeleteState((byte) 0); // 是否删除
        // 时间
        Date date = new Date();
        article.setCreateTime(date); // 创建时间
        article.setUpdateTime(date); // 更新时间

        // 写入数据库
        int row = articleMapper.insertSelective(article);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.FAILED_CREATE.toString() + "userId = " + article.getUserId() + ", title = " + article.getTitle());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 更新版块帖子数
        int boardRow = boardMapper.updateArticleCountById(article.getBoardId());
        if (boardRow != 1) {
            // 记录日志
            log.info(ResultCode.FAILED_CREATE.toString() + "userId = " + article.getUserId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 更新用户发贴数
        int userRow = userMapper.updateArticleCountById(article.getUserId());
        if (userRow != 1) {
            // 记录日志
            log.info(ResultCode.FAILED_CREATE.toString() + "userId = " + article.getUserId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 记录日志, 记录一下返回Id
        log.info("新增帖子成功：userId = " + article.getUserId() + ", articleId = " + article.getId());
    }

    @Override
    public Article selectById(Long id) {
        // 非空校验
        if (id == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 调用DAO查询结果
        Article result = articleMapper.selectByPrimaryKey(id);
        // 返回结果
        return result;
    }

    @Override
    public List<Article> selectAll() {
        // 调用DAO层查询结果
        List<Article> result = articleMapper.selectAll();
        // 返回结果
        return result;
    }

    @Override
    public List<Article> selectByBoardId(Long boardId) {
        // 非空校验
        if (boardId == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 调用DAO层查询结果
        List<Article> result = articleMapper.selectByBoardId(boardId);
        // 返回结果
        return result;
    }

    @Override
    public Article selectDetailById(Long id) {
        // 非空校验
        if (id == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 调用DAO层查询结果
        Article result = articleMapper.selectDetailById(id);
        // 校验结果
        if (result == null) {
            // 记录日志
            log.warn(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 构造更新对象, 更新访问次数
        Article update = new Article();
        update.setId(result.getId());
        // 访问次数加 1
        update.setVisitCount(result.getVisitCount() + 1);
        // 更新
        int row = articleMapper.updateByPrimaryKeySelective(update);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.ERROR_SERVICES.toString() + "articleId = " + result.getId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
        // 返回的帖子访问数加1
        result.setVisitCount(result.getVisitCount() + 1);
        // 返回结果
        return result;
    }

    @Override
    public void modify(Long id, String content) {
        // 非空校验
        if (id == null || StringUtils.isEmpty(content)) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 构造帖子对象
        Article update = new Article();
        update.setId(id);
        update.setContent(content);
        update.setUpdateTime(new Date());
        int row = articleMapper.updateByPrimaryKeySelective(update);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.ERROR_SERVICES.toString() + "articleId = " + id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }

    @Override
    public List<Article> selectByUserId(Long userId) {
        // 非空校验
        if (userId == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 调用DAO层查询结果
        List<Article> result = articleMapper.selectByUserId(userId);
        // 返回结果
        return result;
    }

    @Override
    public void thumbsUpById(Long id) {
        // 非空校验
        if (id == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询帖子信息
        Article article = selectById(id);
        if (article == null || article.getState() == 1 || article.getDeleteState() == 1) {
            // FAILED_NOT_EXISTS
            // 记录日志
            log.info(ResultCode.FAILED_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 构造更新对象
        Article update = new Article();
        update.setId(article.getId());
        update.setLikeCount(article.getLikeCount() + 1);

        // 更新数据库
        int row = articleMapper.updateByPrimaryKeySelective(update);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.ERROR_SERVICES.toString() + "articleId = " + article.getId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }

    @Override
    public void deleteById(Long id) {
        // 非空校验
        if (id == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询帖子信息
        Article article = selectById(id);
        if (article == null || article.getDeleteState() == 1) {
            // FAILED_NOT_EXISTS
            // 记录日志
            log.info(ResultCode.FAILED_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }

        // 构造更新对象
        Article update = new Article();
        update.setId(article.getId());
        update.setDeleteState((byte) 1);

        // 更新数据库
        int row = articleMapper.updateByPrimaryKeySelective(update);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.ERROR_SERVICES.toString() + "articleId = " + article.getId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }
}
