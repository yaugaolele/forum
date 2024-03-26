package com.knowledgeplanet.forum.services.impl;

import com.knowledgeplanet.forum.common.AppResult;
import com.knowledgeplanet.forum.common.ResultCode;
import com.knowledgeplanet.forum.dao.BoardMapper;
import com.knowledgeplanet.forum.exception.ApplicationException;
import com.knowledgeplanet.forum.model.Board;
import com.knowledgeplanet.forum.services.IBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


// 日志注解
@Slf4j
// 加入到Spring容器
@Service
public class BoardServiceImpl implements IBoardService {

    @Resource
    private BoardMapper boardMapper;
    @Override
    public void create(Board board) {
        // 非空校验
        if (board == null) {
            // 记录日志
            log.info(ResultCode.ERROR_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 设备初值
        board.setArticleCount(0); // 帖子数量
        board.setState((byte) 0); // 状态
        board.setDeleteState((byte) 0); // 是否删除
        Date date = new Date();
        board.setCreateTime(date); // 创建时间
        board.setUpdateTime(date); // 更新时间
        int row = boardMapper.insertSelective(board);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.FAILED_CREATE.toString() + "版块名 = " + board.getName());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        log.info("新增版块成功. board = " + board.toString());

    }

    @Override
    public List<Board> selectTopByNum(Integer num) {
        // 参数校验
        if (num <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO层查询
        List<Board> result = boardMapper.selectTopByNum(num);
        // 返回结果
        return result;
    }

    @Override
    public List<Board> selectAllNormal() {
        // 调用DAO层查询
        List<Board> result = boardMapper.selectAllNormal();
        // 返回结果
        return result;
    }

    @Override
    public Board selectById(Long id) {
        // 参数校验
        if (id == null || id <= 0) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        Board result = boardMapper.selectByPrimaryKey(id);
        return result;
    }


}
