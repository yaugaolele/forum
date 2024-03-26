package com.knowledgeplanet.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ApiModel("用户信息")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 用户Id
    @ApiModelProperty("用户Id")
    private Long id;

    // 用户名
    @ApiModelProperty("用户名")
    private String username;

    // 用户密码
    @ApiModelProperty("用户密码")
    @JsonIgnore
    private String password;

    // 昵称
    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    // 必须进行JSON序列化
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String phoneNum;

    @ApiModelProperty("电子邮箱")
    // 必须进行JSON序列化
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String email;

    // 性别
    @ApiModelProperty("性别")
    private Byte gender;

    // 盐
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String salt;

    // 头像地址
    @ApiModelProperty("头像地址")
    // 必须进行JSON序列化
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String avatarUrl;

    // 文章数量
    @ApiModelProperty(value = "文章数量", hidden = true)
    private Integer articleCount;

    // 是否管理员
    @ApiModelProperty("是否管理员:0否 1是")
    private Byte isAdmin;

    // 备注，自我介绍
    @ApiModelProperty("自我介绍")
    private String remark;

    // 状态
    @ApiModelProperty("状态：0正常 1禁用")
    private Byte state;

    // 是否删除
    @ApiModelProperty(value = "是否删除 0否 1是", hidden = true)
    @JsonIgnore
    private Byte deleteState;

    // 创建时间
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    // 修改时间
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date updateTime;

}