package com.knowledgeplanet.forum.services;

import com.knowledgeplanet.forum.model.User;


public interface IUserService {
    /**
     * 创建普通用户
     * @param user 用户信息
     */
    void createNormalUser(User user);

    /**
     * 根据用户Id查询用户信息
     * @param Id
     * @return
     */
    User selectById(Long Id);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User selectByUserName(String username);

    /**
     * 处理用户登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    User login(String username, String password);

    /**
     * 修改用户信息
     * @param id 用户Id
     * @param username 新用户名
     * @param nickname 新昵称
     * @param gender 新性别
     * @return 更新后的用户信息
     */
    User modifyInfo(Long id, String username, String nickname, Byte gender, String email, String phoneNum, String remark);

    /**
     * 修改用户密码
     * @param id 用户Id
     * @param newPassword 新密码
     * @param oldPassword 老密码
     */
    void modifyPassword (Long id, String newPassword, String oldPassword);
}
