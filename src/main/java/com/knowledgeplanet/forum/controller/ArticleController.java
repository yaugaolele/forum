package com.knowledgeplanet.forum.controller;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.config.AppConfig;
import com.knowledgeplanet.forum.model.Article;
import com.knowledgeplanet.forum.model.ArticleReply;
import com.knowledgeplanet.forum.model.User;
import com.knowledgeplanet.forum.services.IArticleReplyService;
import com.knowledgeplanet.forum.services.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.List;


@Slf4j
@Api(tags = "帖子接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    // 注入业务层
    @Resource
    private IArticleService articleService;
    @Resource
    private IArticleReplyService articleReplyService;

    /**
     * 发新帖
     *
     * @param boardId 版块Id
     * @param title   文章标题
     * @param content 帖子内容
     * @return
     */
    @ApiOperation("发新帖")
    @PostMapping("/create")
    public AppResult create(HttpServletRequest request,
                            @ApiParam(value = "版块Id") @RequestParam(value = "boardId") @NonNull Long boardId,
                            @ApiParam(value = "文章标题") @RequestParam(value = "title") @NonNull String title,
                            @ApiParam(value = "帖子内容") @RequestParam(value = "content") @NonNull String content) {
        // 获取用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断是否被禁言
        if (user.getState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 构造帖子对象
        Article article = new Article();
        article.setBoardId(boardId);
        article.setUserId(user.getId());
        article.setTitle(title);
        article.setContent(content);
        // 调用业务层
        articleService.create(article);
        // 更新用户发贴数
        user.setArticleCount(user.getArticleCount() + 1);
        // 返回结果
        return AppResult.success();
    }

    /**
     * 查询主页中显示的帖子列表
     *
     * @return 帖子列表
     */
    @ApiOperation("查询主页中显示的帖子列表")
    @GetMapping("/getAll")
    public AppResult<List<Article>> getAll() {
        // 调用Service层获取帖子列表
        List<Article> result = articleService.selectAll();
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 根据版块Id查询帖子列表
     *
     * @param boardId 版块Id
     * @return 指定版块的帖子列表
     */
    @ApiOperation("根据版块Id查询帖子列表")
    @GetMapping("/getAllByBoardId")
    public AppResult<List<Article>> getAllByBoardId(@ApiParam(value = "版块Id") @RequestParam(value = "boardId",
            required = false) Long boardId) {
        // 定义返回的集对象
        List<Article> result = null;
        // 根据传的参数返回不同板块的帖子列表
        if (boardId == null) {
            // 参数为空时表示首页帖子列表
            result = articleService.selectAll();
            log.info("查询所有帖子列表");
        } else {
            // 参数i 为空时表示指定版本的帖子列表
            result = articleService.selectByBoardId(boardId);
            log.info("查询版块帖子列表, boardId = " + boardId);
        }
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 根据用户Id查询帖子列表
     *
     * @param userId 用户Id
     * @return 指定版块的帖子列表
     */
    @ApiOperation("根据用户Id查询帖子列表")
    @GetMapping("/getAllByUserId")
    public AppResult<List<Article>> getAllByUserId(HttpServletRequest request,
                                                   @ApiParam(value = "用户Id") @RequestParam(value = "userId", required = false) Long userId) {

        // 根据参数中的userId是否为空决定返回哪个用户的帖子列表
        // 1. userId 为空，获取当前登录用户Id
        // 2. userId 不为空，指定id的用户
        if (userId == null) {
            // 获取Session中的用户信息
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(AppConfig.USER_SESSION);
            // 获取当前登录用户的Id
            userId = user.getId();
        }
        // 查询用户的帖子列表
        List<Article> result = articleService.selectByUserId(userId);
        log.info("查询用户帖子列表, userId = " + userId);
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 根据Id查询帖子详情
     *
     * @param id 版块Id
     * @return 指定Id的帖子详情
     */
    @ApiOperation("根据Id查询帖子详情")
    @GetMapping("/getById")
    public AppResult<Article> getById(HttpServletRequest request,
                                      @ApiParam(value = "帖子Id") @RequestParam(value = "id") @NonNull Long id) {
        // 获取用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 调用Service层获取帖子详情
        Article result = articleService.selectDetailById(id);
        // 当前用户是否作者
        if (user.getId() == result.getUserId()) {
            result.setOwn(true);
        }
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 编辑帖子
     *
     * @param id      帖子Id
     * @param content 帖子内容
     * @return AppResult
     */
    @ApiOperation("编辑帖子")
    @PostMapping("/modify")
    public AppResult modify(HttpServletRequest request,
                            @ApiParam(value = "帖子Id") @RequestParam(value = "id") @NonNull Long id,
                            @ApiParam(value = "帖子内容") @RequestParam(value = "content") @NonNull String content) {
        // 获取用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断是否被禁言
        if (user == null || user.getState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 获取帖子原始数据
        Article article = articleService.selectById(id);
        // 数据校验
        if (article == null) {
            log.warn("修改的帖子不存在. articleId = " + id);
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        // 发帖人不是当前用户
        if (article.getUserId() != user.getId()) {
            log.warn(MessageFormat.format("发帖人不是当前用户. article id = {0}, article user id = {1}, " +
                    "current user id = {2}", article.getId(), article.getUserId(), user.getId()));
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        // 帖子状态异常
        if (article.getState() != 0 || article.getDeleteState() != 0) {
            log.warn("帖子状态异常. articleId = " + id + ", state = " + article.getState() + ", delete state = " + article.getDeleteState());
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        // 调用业务层
        articleService.modify(id, content);
        // 日志
        log.info(MessageFormat.format("帖子修改成功. id = {0}, user id = {1}",
                id, user.getId()));
        // 返回结果
        return AppResult.success();
    }

    /**
     * 点赞
     *
     * @param id 帖子Id
     * @return AppResult
     */
    @ApiOperation("点赞")
    @PostMapping("/thumbsUp")
    public AppResult thumbsUp(HttpServletRequest request,
                            @ApiParam(value = "帖子Id") @RequestParam(value = "id") @NonNull Long id) {
        // 获取用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断是否被禁言
        if (user.getState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 更新点赞数
        articleService.thumbsUpById(id);

        // 返回结果
        return AppResult.success();
    }

    /**
     * 回复帖子
     *
     * @param articleId 帖子Id
     * @param content   回复内容
     * @return AppResult
     */
    @ApiOperation("回复帖子")
    @PostMapping("/reply")
    public AppResult reply(HttpServletRequest request,
                           @ApiParam(value = "帖子Id") @RequestParam(value = "articleId") @NonNull Long articleId,
                           @ApiParam(value = "回复内容") @RequestParam(value = "content") @NonNull String content) {
        // 获取用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断是否被禁言
        if (user.getState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 获取帖子信息
        Article article = articleService.selectById(articleId);
        // 校验帖子信息
        if (article == null) {
            log.warn("修改的帖子不存在. articleId = " + articleId);
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        if (article.getState() != 0 || article.getDeleteState() != 0) {
            log.warn("帖子状态异常. articleId = " + articleId + ", state = " + article.getState() + ", delete state = " + article.getDeleteState());
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        // 构造数据
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(articleId);
        articleReply.setPostUserId(user.getId());
        articleReply.setContent(content);
        articleReply.setReplyId(null);
        articleReply.setReplyUserId(null);
        // 调用业务层
        articleReplyService.create(articleReply);
        // 返回结果
        return AppResult.success();
    }

    /**
     * 回复帖子
     *
     * @param articleId 帖子Id
     * @return AppResult
     */
    @ApiOperation("回复帖子")
    @GetMapping("/getReplies")
    public AppResult<List<ArticleReply>> getRepliesByArticleId(@ApiParam(value = "帖子Id") @RequestParam(value = "articleId") @NonNull Long articleId) {
        // 查询结果
        List<ArticleReply> result = articleReplyService.selectByArticleId(articleId);
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 根据Id删除帖子
     *
     * @param id 帖子Id
     * @return
     */
    @ApiOperation("根据Id删除帖子")
    @GetMapping("/delete")
    public AppResult deleteById(HttpServletRequest request,
                                      @ApiParam(value = "帖子Id") @RequestParam(value = "id") @NonNull Long id) {
        // 获取用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 调用Service层获取帖子详情
        Article result = articleService.selectById(id);
        // 当前用户是否作者
        if (result == null || user.getId() != result.getUserId()) {
            // 日志
            log.warn(ResultCode.FAILED_UNAUTHORIZED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        // 调用删除方法
        articleService.deleteById(result.getId());
        // 返回结果
        return AppResult.success();
    }


}
