package com.knowledgeplanet.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ApiModel("版块信息")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    // 版块Id
    @ApiModelProperty("版块Id")
    private Long id;

    // 版块名称
    @ApiModelProperty("版块名称")
    private String name;

    // 帖子数量
    @ApiModelProperty("帖子数量")
    private Integer articleCount;

    // 排序
    @ApiModelProperty("排序")
    private Integer sort;

    // 版块状态
    @ApiModelProperty("版块状态")
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
}