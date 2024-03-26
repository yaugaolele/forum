package com.knowledgeplanet.forum.controller;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.config.AppConfig;
import com.knowledgeplanet.forum.model.User;
import com.knowledgeplanet.forum.services.IUserService;
import com.knowledgeplanet.forum.utils.MD5Utils;
import com.knowledgeplanet.forum.utils.StringUtils;
import com.knowledgeplanet.forum.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


// 添加日志注解以便使用日志
@Slf4j
// API 描述
@Api(tags = "用户接口")
// 标识为了个Controller并且响应时返回JSON格式数据
@RestController
// 指定路径映射
@RequestMapping("/user")
public class UserController {

    // 注入业务层
    @Resource
    private IUserService userService;

    /**
     * 编写注册时:
     * 第一步. 先用非注解的方式实现功能并演示
     * 第二步， 优化为注解方式，参见register方法
     *
     * @param username 用户名
     * @param nickname 昵称
     * @param gender 性别
     * @param password 密码
     * @param passwordRepeat 重复密码
     * @return AppResult
     */
    // API 描述
    @ApiOperation("注册用户1")
    // 指定接口URL映射
    @PostMapping("/register1")
    public AppResult register1 (String username, String nickname, Byte gender, String password, String passwordRepeat)  {
        // 校验参数的有效性
        if (StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(passwordRepeat)
                || StringUtils.isEmpty(nickname)
                || gender == null) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        // 1.基本信息赋值
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        // 2.校验性别
        if (gender <0 || gender > 2) {
            // 不符合条件设为null
            user.setGender(null);
        }
        user.setGender(gender);
        // 3.处理密码
        // 3.1 校验密码和确认密码是否相等
        if (!password.equals(passwordRepeat)) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        // 3.2 加密密码
        String salt = UUIDUtils.UUID_32();
        String encryptPassword = MD5Utils.md5Salt(password, salt);
        // 3.3 设置密码
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        // 4. 调用Service
        userService.createNormalUser(user);
        // 5. 返回
        return AppResult.success();
    }


    /**
     * 用户注册
     *
     * 参数使用注解完成校验与API生成
     *
     * @param username 用户名
     * @param nickname 昵称
     * @param gender 性别
     * @param password 密码
     * @param passwordRepeat 重复密码
     * @return AppResult
     */
    // API 描述
    @ApiOperation("注册用户")
    @PostMapping("/register")
    public AppResult register (@ApiParam(value = "用户名") @RequestParam(value = "username") @NonNull String username,
                               @ApiParam(value = "昵称") @RequestParam(value = "nickname") @NonNull String nickname,
                               @ApiParam(value = "性别") @RequestParam(value = "gender", required = false) Byte gender,
                               @ApiParam(value = "密码") @RequestParam(value = "password") @NonNull String password,
                               @ApiParam(value = "确认密码") @RequestParam(value = "passwordRepeat") @NonNull String passwordRepeat) {
        // 1.基本信息赋值
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        // 2.校验性别
        if (gender != null && (gender < 0 || gender > 2)) {
            // 不符合条件设为null
            user.setGender(null);
        }
        user.setGender(gender);
        // 3.处理密码
        // 3.1 校验密码和确认密码是否相等
        if (!password.equals(passwordRepeat)) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        // 3.2 加密密码
        String salt = UUIDUtils.UUID_32();
        String encryptPassword = MD5Utils.md5Salt(password, salt);
        // 3.3 设置密码
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        // 4. 调用Service
        userService.createNormalUser(user);
        // 5. 返回
        return AppResult.success();
    }

    /**
     * 用户登录
     *
     * @param request
     * @param username 用户名
     * @param password 密码
     * @return AppResult
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public AppResult login (HttpServletRequest request,
                            @ApiParam(value = "用户名") @RequestParam(value = "username") @NonNull String username,
                            @ApiParam(value = "密码") @RequestParam(value = "password") @NonNull String password) {
        // 用户登录
        User user = userService.login(username, password);
        // 获取Session
        HttpSession session = request.getSession(true);
        // 把User设置到Session中
        session.setAttribute(AppConfig.USER_SESSION, user);
        // 打印日志
        log.info("登录成功: userId = " + user.getId());
        // 登录成功响应
        return AppResult.success(user);
    }

    @ApiOperation("用户退出")
    @GetMapping("/logout")
    public AppResult logout (HttpServletRequest request) {
        // 获取session对象
        HttpSession session = request.getSession();
        if (session != null) {
            // 注销session
            session.invalidate();
        }
        // 退出成功响应
        return AppResult.success();
    }

    /**
     * 修改个人信息
     * @param id 用户Id
     * @param username 新用户名
     * @param nickname 新昵称
     * @param gender 新性别
     * @return 更新后的用户信息
     */
    @ApiOperation("修改用户个人信息")
    @PostMapping("/modifyInfo")
    public AppResult<User> modifyInfo (HttpServletRequest request,
                                       @ApiParam(value = "用户Id") @RequestParam(value = "id") @NonNull Long id,
                                       @ApiParam(value = "用户名") @RequestParam(value = "username", required = false) String username,
                                       @ApiParam(value = "昵称") @RequestParam(value = "nickname", required = false) String nickname,
                                       @ApiParam(value = "性别") @RequestParam(value = "gender", required = false) Byte gender,
                                       @ApiParam(value = "邮箱") @RequestParam(value = "email", required = false) String email,
                                       @ApiParam(value = "电话") @RequestParam(value = "phoneNum", required = false) String phoneNum,
                                       @ApiParam(value = "个人介绍") @RequestParam(value = "remark", required = false) String remark) {
        // 1. 获取Session中的用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 2. 校验传入的用户Id是否为当前登录用户
        if (user == null || user.getId() != id) {
            // 记录日志
            log.info(ResultCode.FAILED_UNAUTHORIZED.toString() + "user id = " + id);
            // 校验不通过返回未授权
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        // 3. 校验性别
        if (gender != null && (gender < 0 || gender > 2)) {
            // 不符合条件设为null
            gender = null;
        }
        // 4. 校验参数
        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(nickname) && gender == null
                && StringUtils.isEmpty(remark) && StringUtils.isEmpty(email) && StringUtils.isEmpty(phoneNum)) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString() + "user id = " + id);
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        // 5. 调用Service处理
        user = userService.modifyInfo(user.getId(), username, nickname, gender, email, phoneNum, remark);
        // 6. 重新设置Session中的用户信息
        session.setAttribute(AppConfig.USER_SESSION, user);
        // 7. 记录日志
        log.info("用户信息修改成功: userId = " + user.getId());
        // 8. 返回成功
        return AppResult.success(user);
    }

    /**
     * 修改用户密码
     *
     * @param id 用户 id
     * @param oldPassword 密码
     * @param newPassword 新密码
     * @param passwordRepeat 重复密码
     * @return
     */
    @ApiOperation("修改用户密码")
    @PostMapping("/modifyPwd")
    public AppResult modifyPassword (HttpServletRequest request,
                                       @ApiParam(value = "用户Id") @RequestParam(value = "id") @NonNull Long id,
                                       @ApiParam(value = "原密码") @RequestParam(value = "oldPassword")  @NonNull String oldPassword,
                                       @ApiParam(value = "新密码") @RequestParam(value = "newPassword")  @NonNull String newPassword,
                                       @ApiParam(value = "重复新密码") @RequestParam(value = "passwordRepeat")  @NonNull String passwordRepeat) {
        // 1. 获取Session中的用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 2. 校验传入的用户Id是否为当前登录用户
        if (user == null || user.getId() != id) {
            // 记录日志
            log.info(ResultCode.FAILED_UNAUTHORIZED.toString() + "user id = " + id);
            // 校验不通过返回未授权
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        // 3. 校验新密码与重复密码是否相等
        if (!newPassword.equals(passwordRepeat)) {
            // 记录日志
            log.info(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString() + "user id = " + id);
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }

        // 4. 调用Service处理
        userService.modifyPassword(user.getId(), newPassword, oldPassword);
        // 7. 记录日志
        log.info("用户密码修改成功: userId = " + user.getId());
        // 8. 返回成功
        return AppResult.success();
    }

    /**
     * 获取用户详情
     *
     * @param id 用户 id
     * @return 用户详情
     */
    @ApiOperation("获取用户详情")
    @GetMapping("/info")
    public AppResult<User> getUserInfo (HttpServletRequest request,
                                     @ApiParam(value = "用户Id") @RequestParam(value = "id", required = false) Long id) {
        // 根据参数中的Id是否为空决定返回哪个用户
        // 1. id 为空，返回当前登录用户
        // 2. id 不为空，返回指定id的用户
        User user = null;
        if (id == null) {
            // 获取Session中的用户信息
            HttpSession session = request.getSession();
            user = (User) session.getAttribute(AppConfig.USER_SESSION);
        } else {
            // 根据用户Id从数据库中查询用户信息
            user = userService.selectById(id);
        }
        // 返回结果
        return AppResult.success(user);
    }
}
