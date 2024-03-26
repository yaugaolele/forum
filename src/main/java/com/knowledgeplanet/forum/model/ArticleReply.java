package com.knowledgeplanet.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ApiModel("帖子回复信息")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleReply {

    // 回复Id
    @ApiModelProperty("回复Id")
    private Long id;

    // 帖子Id
    @ApiModelProperty("帖子Id")
    private Long articleId;

    // 楼主用户Id
    @ApiModelProperty("楼主用户Id")
    private Long postUserId;

    // 关联回复Id, 支持楼中楼
    @ApiModelProperty("关联回复Id, 支持楼中楼")
    private Long replyId;

    // 楼主下的回复用户Id
    @ApiModelProperty("楼主下的回复用户Id")
    private Long replyUserId;

    // 回复内容
    @ApiModelProperty("回复内容")
    private String content;

    // 点赞数量
    @ApiModelProperty("点赞数量")
    private Integer likeCount;

    // 回复状态
    @ApiModelProperty("回复状态")
    private Byte state;

    // 是否删除
    @ApiModelProperty("是否删除")
    @JsonIgnore
    private Byte deleteState;

    // 创建时间
    @ApiModelProperty("创建时间")
    private Date createTime;

    // 创建时间
    @ApiModelProperty("更新时间")
    private Date updateTime;

    // 用户
    @ApiModelProperty("用户")
    private User user;
}