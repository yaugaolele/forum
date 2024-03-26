package com.knowledgeplanet.forum.services;


import com.knowledgeplanet.forum.model.Message;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IMessageService {

    /**
     * 发送站内信息
     * @param message 站内信
     */
    void create (Message message);

    /**
     * 读取站内信
     *
     * @param id 站内信Id
     * @return
     */
    Message selectById(Long id);

    /**
     * 获取所有未读站内信息
     *
     * @param userId 用户Id
     * @return
     */
    Integer selectUnreadCount (Long userId);

    /**
     * 获取所有站内信息
     *
     * @param receiveUserId 用户Id
     * @return
     */
    List<Message> selectPage (Long receiveUserId, Integer page, Integer num, String order, boolean isAsc);

    /**
     * 根据接收者Id查询所有站内信
     * @param receiveUserId
     * @return
     */
    List<Message> selectByReceiveUserId (Long receiveUserId);

    /**
     * 根据Id更新
     * @param message
     * @return
     */
    void updateById(Message message);

    /**
     * 回复站内信
     * @param repliedId 被回复的站内信Id
     * @param message 回复内容
     */
    @Transactional
    void reply (Long repliedId, Message message);
}
