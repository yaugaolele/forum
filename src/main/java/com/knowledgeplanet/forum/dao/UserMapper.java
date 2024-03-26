package com.knowledgeplanet.forum.dao;

import com.knowledgeplanet.forum.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    /**
     * 接用户名查询用户信息
     *
     * @param username 用户名
     * @return 对应的用户信息
     */
    User selectByUserName(String username);

    /**
     * 帖子数加1
     * @param id
     * @return
     */
    int updateArticleCountById(Long id);
}