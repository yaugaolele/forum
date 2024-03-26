package com.knowledgeplanet.forum.dao;

import com.knowledgeplanet.forum.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    int insert(Message row);

    int insertSelective(Message row);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message row);

    int updateByPrimaryKey(Message row);

    /**
     * 按用户Id查询对应用户未读站内信数量
     * @param userId
     * @return
     */
    Integer selectUnreadCount (Long userId);

    /**
     * 根据接收者Id查询所有站内信
     * @param receiveUserId
     * @return
     */
    List<Message> selectByReceiveUserId (@Param("receiveUserId") Long receiveUserId);
}