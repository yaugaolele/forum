package com.knowledgeplanet.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ApiModel("帖子信息")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    // 文章Id
    @ApiModelProperty("文章Id")
    private Long id;

    //版块Id
    @ApiModelProperty("版块Id")
    private Long boardId;

    // 用户Id
    @ApiModelProperty("用户Id")
    private Long userId;

    // 文章标题
    @ApiModelProperty("文章标题")
    private String title;

    // 访问数量
    @ApiModelProperty("访问数量")
    private Integer visitCount;

    // 回复数量
    @ApiModelProperty("回复数量")
    private Integer replyCount;

    // 点赞数量
    @ApiModelProperty("点赞数量")
    private Integer likeCount;

    // 文章状态
    @ApiModelProperty("文章状态")
    private Byte state;

    // 是否删除
    @ApiModelProperty("是否删除")
    @JsonIgnore
    private Byte deleteState;

    // 创建时间
    @ApiModelProperty("创建时间")
    private Date createTime;

    // 更新时间
    @ApiModelProperty("更新时间")
    private Date updateTime;

    // 帖子内容
    @ApiModelProperty("帖子内容")
    private String content;

    // 版块信息
    @ApiModelProperty("版块信息")
    private Board board;

    // 用户信息
    @ApiModelProperty("用户信息")
    private User user;

    // 用户是不是作者
    @ApiModelProperty("用户是不是作者")
    private boolean isOwn;
}