package com.knowledgeplanet.forum.controller;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.config.AppConfig;
import com.knowledgeplanet.forum.model.Message;
import com.knowledgeplanet.forum.model.User;
import com.knowledgeplanet.forum.services.IMessageService;
import com.knowledgeplanet.forum.services.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Slf4j
@Api(tags = "站内信接口")
@RestController
@RequestMapping("/message")
public class MessageController {
    // 注入业务层
    @Resource
    private IMessageService messageService;
    @Resource
    private IUserService userService;

    /**
     * 发送站内信
     *
     * @param receiveUserId 接收用户Id
     * @param content       内容
     * @return AppResult
     */
    @ApiOperation("发送站内信")
    @PostMapping("/send")
    public AppResult send(HttpServletRequest request,
                          @ApiParam(value = "接收用户Id") @RequestParam(value = "receiveUserId") @NonNull Long receiveUserId,
                          @ApiParam(value = "站内信内容") @RequestParam(value = "content") @NonNull String content) {
        // 获取发送用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断是否被禁言
        if (user.getState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 不能给自己发送
        if (user.getId() == receiveUserId) {
            // 日志
            log.warn("不能给自己发送站内信. postUserId = " + user.getId() + ", receiveUserId = " + receiveUserId);
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_CREATE);
        }
        // 查询接收用户
        User receiveUser = userService.selectById(receiveUserId);
        // 目标用户不存在
        if (receiveUser == null || receiveUser.getDeleteState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString() + ", receiveUserId = " + receiveUserId);
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        // 构造对象
        Message message = new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(receiveUserId);
        message.setContent(content);
        // 调用业务层
        messageService.create(message);
        // 返回结果
        return AppResult.success();
    }

    /**
     * 读取站内信
     *
     * @param id 站内信Id
     * @return AppResult<Message>
     */
    @ApiOperation("读取站内信")
    @GetMapping("/read")
    public AppResult<Message> read(HttpServletRequest request,
                                   @ApiParam(value = "站内信Id") @RequestParam(value = "id") @NonNull Long id) {
        // 根据Id查询内容
        Message message = messageService.selectById(id);
        // 获取用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 接收方不是自己
        if (message != null && user.getId() != message.getReceiveUserId()) {
            // 打印日志
            log.warn("查询了不属于自己的站内信：userId = " + user.getId() + ", receiveUserId = " + message.getReceiveUserId());
            // 返回错误结果
            return AppResult.failed(ResultCode.FAILED);
        }
        // 更新为已读状态
        Message update = new Message();
        update.setId(message.getId());
        update.setState((byte) 1); // 1 为已读状态
        messageService.updateById(update);
        // 返回结果
        return AppResult.success(message);
    }

    /**
     * 获取未读消息个数
     *
     * @return AppResult<Integer>
     */
    @ApiOperation("获取未读消息个数")
    @GetMapping("/getUnreadCount")
    public AppResult<Integer> getUnreadCount(HttpServletRequest request) {
        // 获取发送用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断是否被禁言
        if (user.getState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 查询未读消息个数
        Integer result = messageService.selectUnreadCount(user.getId());
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 查询用户的所有站内信
     *
     * @return 站内信集合
     */
    @ApiOperation("查询用户的所有站内信")
    @GetMapping("/getAll")
    public AppResult<List<Message>> getAllByReceiveUserId(HttpServletRequest request) {
        // 获取当前登录用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 获取用户站内信
        List<Message> result = messageService.selectByReceiveUserId(user.getId());
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 回复站内信
     * @param repliedId 回复的原站内信Id
     * @param receiveUserId 接收者Id
     * @param content 内容
     * @return
     */
    @ApiOperation("回复站内信息")
    @PostMapping("/reply")
    public AppResult reply (HttpServletRequest request,
                            @ApiParam(value = "当前站内信Id") @RequestParam(value = "repliedId") @NonNull Long repliedId,
                            @ApiParam(value = "接收用户Id") @RequestParam(value = "receiveUserId") @NonNull Long receiveUserId,
                            @ApiParam(value = "站内信内容") @RequestParam(value = "content") @NonNull String content) {
        // 获取发送用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断是否被禁言
        if (user.getState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString() + ", userId = " + user.getId());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 不能给自己发送
        if (user.getId() == receiveUserId) {
            // 日志
            log.warn("不能给自己发送站内信. postUserId = " + user.getId() + ", receiveUserId = " + receiveUserId);
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_CREATE);
        }
        // 查询接收用户
        User receiveUser = userService.selectById(receiveUserId);
        // 目标用户不存在
        if (receiveUser == null || receiveUser.getDeleteState() != 0) {
            // 日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString() + ", receiveUserId = " + receiveUserId);
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        // 构造对象
        Message message = new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(receiveUserId);
        message.setContent(content);
        // 调用业务层
        messageService.reply(repliedId, message);
        // 返回结果
        return AppResult.success();
    }
}
