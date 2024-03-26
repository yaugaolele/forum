package com.knowledgeplanet.forum.services.impl;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.dao.UserMapper;
import com.knowledgeplanet.forum.exception.ApplicationException;
import com.knowledgeplanet.forum.model.User;
import com.knowledgeplanet.forum.services.IUserService;
import com.knowledgeplanet.forum.utils.MD5Utils;
import com.knowledgeplanet.forum.utils.StringUtils;
import com.knowledgeplanet.forum.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


// 日志注解
@Slf4j
// 加入到Spring容器
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void createNormalUser(User user) {
        // 非空校验
        if (user == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 查询当前用户是否存在
        User existUser = selectByUserName(user.getUsername());
        if (existUser != null) {
            // 记录日志
            log.info(ResultCode.FAILED_USER_EXISTS.toString() + "username = " + user.getUsername());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
        }
        // 除了注册时传入的有效值，其他全部使用默认
        if (user.getGender() == null) {
            // 性别赋默认值
            user.setGender((byte) 2);
        }
        // 填充默认值
        user.setIsAdmin((byte) 0);
        user.setAvatarUrl(null);
        user.setArticleCount(0);
        user.setState((byte) 0);
        user.setDeleteState((byte) 0);
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        int row = userMapper.insertSelective(user);
        if(row != 1) {
            // 记录日志
            log.info(ResultCode.FAILED_CREATE.toString() + "username = " + user.getUsername());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 记录日志
        log.info("新增用户成功：userId = " + user.getId());
    }

    @Override
    public User selectById(Long Id) {
        return userMapper.selectByPrimaryKey(Id);
    }

    @Override
    public User selectByUserName(String username) {
        // 调用Mapper中的方法，根据用户名查询用户信息
        return userMapper.selectByUserName(username);
    }

    @Override
    public User login(String username, String password) {
        // 根据用户名获取用户信息
        User user = selectByUserName(username);
        // 用户不存在抛出异常
        if (user == null) {
            // 记录日志
            log.info(ResultCode.FAILED_USER_NOT_EXISTS.toString() + "username = " + username);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 校验密码是否正确
        String encryptPassword = MD5Utils.md5Salt(password, user.getSalt());
        if (!encryptPassword.equalsIgnoreCase(user.getPassword())) {
            // 记录日志
            log.info(ResultCode.FAILED_LOGIN.toString() + "username = " + username + ", password = " + password);
            // 密码不正确抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 校验通过，返回用户信息
        return user;
    }

    @Override
    public User modifyInfo(Long id, String username, String nickname, Byte gender, String email, String phoneNum,
                           String remark) {
        // 1. 根据用户Id查询用户信息
        User user = userMapper.selectByPrimaryKey(id);
        // 2. 校验用户Id是否有存在
        if (user == null) {
            // 记录日志
            log.info(ResultCode.FAILED_UNAUTHORIZED.toString() + "user id = " + id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UNAUTHORIZED));
        }
        // 3. 校验数据是否全部为空
        if (user.getUsername().equals(username)
                && user.getNickname().equals(nickname)
                && user.getGender() == gender) {
            // 与原数据相同直接返回
            return user;
        }
        // 4. 用户名不为空时，根据新用户名查询用户是否存在
        if (!StringUtils.isEmpty(username)) {
            User existsUser = selectByUserName(username);
            if (existsUser != null) {
                // 记录日志
                log.info(ResultCode.FAILED_USER_EXISTS.toString() + "username = " + username);
                // 存在则抛出用户已存在的异常
                throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
            }
        }

        // 5. 校验完成，开始构建更新对象
        User modifyUser = new User();
        // 设备用户Id
        modifyUser.setId(user.getId());
        // 用户名不为空时，需要更新
        if (!StringUtils.isEmpty(username)) {
            modifyUser.setUsername(username);
        }
        // 昵称不为空时，需要更新
        if (!StringUtils.isEmpty(nickname)) {
            modifyUser.setNickname(nickname);
        }
        // 性别不为空时，需要更新
        if (gender != null && (gender >= 0 || gender <= 2)) {
            modifyUser.setGender(gender);
        }
        // 邮箱不为空时，需要更新
        if (!StringUtils.isEmpty(email)) {
            modifyUser.setEmail(email);
        }
        // 电话不为空时，需要更新
        if (!StringUtils.isEmpty(phoneNum)) {
            modifyUser.setPhoneNum(phoneNum);
        }
        // 个人介绍不为空时，需要更新
        if (!StringUtils.isEmpty(remark)) {
            modifyUser.setRemark(remark);
        }
        // 更新时间
        modifyUser.setUpdateTime(new Date());
        // 6. 更新用户信息，这个方法只更新不为空的字段
        userMapper.updateByPrimaryKeySelective(modifyUser);
        // 7.获取用户更新后的数据
        user = userMapper.selectByPrimaryKey(user.getId());
        // 8.记录日志
        log.info("用户信息修改成功：" + user.getUsername());
        // 9.返回修改后的用户信息
        return user;
    }

    @Override
    public void modifyPassword(Long id, String newPassword, String oldPassword) {
        // 1. 根据用户Id查询用户信息
        User user = userMapper.selectByPrimaryKey(id);
        // 2. 校验用户Id是否有存在
        if (user == null) {
            // 记录日志
            log.info(ResultCode.FAILED_UNAUTHORIZED.toString() + "user id = " + id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UNAUTHORIZED));
        }

        // 3. 校验原密码是否正确
        String oldEncryptPassword = MD5Utils.md5Salt(oldPassword, user.getSalt());
        if (!oldEncryptPassword.equalsIgnoreCase(user.getPassword())) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString() + "username = " + user.getUsername() + ", password = " + oldPassword);
            // 密码不正确抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 4. 重新生成扰动字符串
        String salt = UUIDUtils.UUID_32();
        // 5. 计算新密码
        String encryptPassword = MD5Utils.md5Salt(newPassword, salt);
        // 6. 创建用于更新的User对象
        User modifyUser = new User();
        // 设置用户Id
        modifyUser.setId(user.getId());
        // 设置新密码
        modifyUser.setPassword(encryptPassword);
        // 设置扰动字符串
        modifyUser.setSalt(salt);
        // 设置更新时间
        modifyUser.setUpdateTime(new Date());

        // 7. 更新操作
        int row = userMapper.updateByPrimaryKeySelective(modifyUser);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.FAILED.toString() + "修改密码失败. userId = " + user.getId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
        // 打印日志
        log.info("用户密码修改成功. userId = " + user.getId());
    }
}
