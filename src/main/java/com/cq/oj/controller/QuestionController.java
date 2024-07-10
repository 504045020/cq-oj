package com.cq.oj.controller;

import com.cq.oj.annotation.AuthCheck;
import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ResultUtil;
import com.cq.oj.common.ServiceException;
import com.cq.oj.constant.AuthConstant;
import com.cq.oj.model.dto.question.QuestionAddRequest;
import com.cq.oj.model.dto.question.QuestionQueryRequest;
import com.cq.oj.model.dto.question.QuestionUpdateRequest;
import com.cq.oj.model.dto.user.*;
import com.cq.oj.model.entity.Question;
import com.cq.oj.model.entity.User;
import com.cq.oj.page.TableDataInfo;
import com.cq.oj.service.QuestionService;
import com.cq.oj.service.UserService;
import com.cq.oj.util.SecurityUtils;
import com.cq.oj.util.sql.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "题目")
@RequestMapping("question")
public class QuestionController extends BaseController {

    @Resource
    public QuestionService questionService;

    @ApiOperation("创建题目")
    @PostMapping("/add")
    @AuthCheck(mustRole = AuthConstant.ADMIN_ROLE)
    public ResultUtil addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        if(null == questionAddRequest){
            throw  new ServiceException(ErrorCode.PARAM_ERROR);
        }
        questionService.addQuestion(questionAddRequest);
        return ResultUtil.success();
    }

    @ApiOperation("删除题目")
    @DeleteMapping("/{id}")
    @AuthCheck(mustRole = AuthConstant.ADMIN_ROLE)
    public ResultUtil delete(@PathVariable("id") Long id){
        if(id == null || id < 0){
            throw new ServiceException(ErrorCode.PARAM_ERROR);
        }
        return ResultUtil.success(questionService.removeById(id));
    }


    @ApiOperation("更新题目信息")
    @PostMapping("/update")
    public ResultUtil questionUpdateRequest(@RequestBody QuestionUpdateRequest questionUpdateRequest){
        if(null == questionUpdateRequest || questionUpdateRequest.getId() < 0){
            throw  new ServiceException(ErrorCode.PARAM_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest,question);
        questionService.updateById(question);
        return ResultUtil.success();
    }


    @ApiOperation("分页获取题目")
    @PostMapping("list")
    public TableDataInfo listQuestion(@RequestBody QuestionQueryRequest questionQueryRequest){
        startPage();
        return getDataTable(questionService.list(questionService.getQueryWrapper(questionQueryRequest)));
    }
}
