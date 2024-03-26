package com.knowledgeplanet.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ApiModel("站内信信息")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    // 消息Id
    @ApiModelProperty("消息Id")
    private Long id;

    // 消息Id
    @ApiModelProperty("发送用户Id")
    private Long postUserId;

    // 消息Id
    @ApiModelProperty("接收用户Id")
    private Long receiveUserId;

    // 消息Id
    @ApiModelProperty("消息内容")
    private String content;

    // 消息Id
    @ApiModelProperty("消息状态")
    private Byte state;

    // 消息Id
    @ApiModelProperty("是否删除")
    @JsonIgnore
    private Byte deleteState;

    // 消息Id
    @ApiModelProperty("创建时间")
    private Date createTime;

    // 消息Id
    @ApiModelProperty("更新时间")
    private Date updateTime;

    // 发送者
    @ApiModelProperty("发送者")
    private User postUser;
}