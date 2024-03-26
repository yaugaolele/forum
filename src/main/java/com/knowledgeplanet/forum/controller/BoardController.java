package com.knowledgeplanet.forum.controller;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.model.Board;
import com.knowledgeplanet.forum.services.IBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@Api(tags = "版块接口")
@RestController
@RequestMapping("/board")
public class BoardController {

    // 注入Service
    @Resource
    private IBoardService boardService;
    // 从配置文件中获取主页中显示的版块个数，默认为9
    @Value("${bit-forum.index.board-num:9}")
    private Integer indexBoardNum;


    /***
     * 创建版块
     * @param name 版块名称
     * @param sort 排序
     * @return AppResult
     */
    // API 描述
    @ApiOperation("创建版块")
    // 指定接口URL映射
    @PostMapping("/create")
    public AppResult create (@ApiParam(value = "版块名称") @RequestParam(value = "name") @NonNull String name,
                             @ApiParam(value = "排序") @RequestParam(value = "sort") @NonNull Integer sort) {
        // 构建版块对象
        Board board = new Board();
        board.setName(name);
        board.setSort(sort);
        // 调用Service
        boardService.create(board);
        // 返回结果
        return AppResult.success();
    }

    // API 描述
    @ApiOperation("主页中显示的版块")
    // 指定接口URL映射
    @GetMapping("/topList")
    public AppResult<List<Board>> topList () {
        System.out.println("indexBoardNum = " + indexBoardNum);
        // 调用Service层获取版块信息
        List<Board> result = boardService.selectTopByNum(indexBoardNum);
        // 返回结果
        return AppResult.success(result);
    }

    // API 描述
    @ApiOperation("获取所有正常状态的版块")
    // 指定接口URL映射
    @GetMapping("/allNormal")
    public AppResult<List<Board>> allNormal () {
        // 调用Service层获取版块信息
        List<Board> result = boardService.selectAllNormal();
        // 返回结果
        return AppResult.success(result);
    }

    /**
     * 获取版块详情
     *
     * @param id 版块 id
     * @return 版块详情
     */
    @ApiOperation("获取版块详情")
    @GetMapping("/getById")
    public AppResult<Board> getBoardInfo (HttpServletRequest request,
                                        @ApiParam(value = "版块Id") @RequestParam(value = "id") @NonNull Long id) {
        // 查询
        Board result = boardService.selectById(id);
        // 返回结果
        return AppResult.success(result);
    }
}
