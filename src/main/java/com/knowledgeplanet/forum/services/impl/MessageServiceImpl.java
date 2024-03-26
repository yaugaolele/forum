package com.knowledgeplanet.forum.services.impl;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.dao.MessageMapper;
import com.knowledgeplanet.forum.exception.ApplicationException;
import com.knowledgeplanet.forum.model.Message;
import com.knowledgeplanet.forum.services.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


// 日志注解
@Slf4j
// 加入到Spring容器
@Service
public class MessageServiceImpl implements IMessageService {
    // 注入DAO
    @Resource
    private MessageMapper messageMapper;
    @Override
    public void create(Message message) {
        // 非空校验
        if (message == null || message.getReceiveUserId() == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }

        // 填充默认值
        message.setState((byte) 0); // 未读状态
        message.setDeleteState((byte) 0); // 是否删除
        Date date = new Date();
        message.setCreateTime(date);
        message.setUpdateTime(date);
        // 定入数据库
        int row = messageMapper.insertSelective(message);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.FAILED_CREATE.toString() + "messageId = " + message.getId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 记录日志, 记录一下返回Id
        log.info("发送站内信成功：postUserId = " + message.getPostUserId() + ", receiveUserId = " + message.getReceiveUserId());

    }

    @Override
    public Message selectById(Long id) {
        // 非空校验
        if (id == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询结果
        Message result = messageMapper.selectByPrimaryKey(id);
        // 返回结果
        return result;
    }

    @Override
    public Integer selectUnreadCount(Long userId) {
        // 非空校验
        if (userId == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询结果
        Integer result = messageMapper.selectUnreadCount(userId);
        // 返回结果
        return result;
    }

    @Override
    public List<Message> selectPage(Long receiveUserId, Integer page, Integer num, String order, boolean isAsc) {
        return null;
    }

    @Override
    public List<Message> selectByReceiveUserId(Long receiveUserId) {
        // 非空校验
        if (receiveUserId == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 调用DAO
        List<Message> result = messageMapper.selectByReceiveUserId(receiveUserId);
        // 返回结果
        return result;
    }

    @Override
    public void updateById(Message message) {
        // 非空校验
        if (message == null || message.getId() == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新
        int row = messageMapper.updateByPrimaryKeySelective(message);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.FAILED.toString() + "messageId = " + message.getId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void reply(Long repliedId, Message message) {
        // 非空校验
        if (repliedId == null || message == null || message.getReceiveUserId() == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询原记录
        Message replied = messageMapper.selectByPrimaryKey(repliedId);
        if (replied == null || replied.getDeleteState() == 1) {
            // 记录日志
            log.info(ResultCode.FAILED_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NOT_EXISTS));
        }
        // 更新状态
        Message updateMessage = new Message();
        updateMessage.setId(repliedId);
        updateMessage.setUpdateTime(new Date());
        updateMessage.setState((byte) 2); // 2 表示已回复
        messageMapper.updateByPrimaryKeySelective(updateMessage);

        // 写入新记录
        create(message);
    }
}
