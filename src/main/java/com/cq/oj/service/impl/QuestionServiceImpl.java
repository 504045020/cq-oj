package com.cq.oj.service.impl;
import java.util.List;

import com.cq.oj.common.ThrowUtils;
import com.cq.oj.model.dto.question.JudgeConfig;
import com.cq.oj.model.dto.question.JudgeCase;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ServiceException;
import com.cq.oj.mapper.QuestionMapper;
import com.cq.oj.model.dto.question.QuestionAddRequest;
import com.cq.oj.model.dto.question.QuestionQueryRequest;
import com.cq.oj.model.dto.user.UserQueryRequest;
import com.cq.oj.model.entity.Question;
import com.cq.oj.model.entity.User;
import com.cq.oj.service.QuestionService;
import com.cq.oj.util.SecurityUtils;
import com.google.gson.Gson;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 50404
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-07-05 16:36:40
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private QuestionMapper questionMapper;

    private final static Gson GSON = new Gson();

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
     if(null == questionQueryRequest){
        throw new ServiceException(ErrorCode.PARAM_ERROR);
     }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();

        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtil.isBlank(title),"title",title);
        wrapper.like(StringUtil.isBlank(content),"content",content);
        return wrapper;
    }

    @Override
    public void addQuestion(QuestionAddRequest questionAddRequest) {

        List<String> tags = questionAddRequest.getTags();
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest,question);
        if(null != judgeCase){
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        if(null != judgeCase){
            question.setTags(GSON.toJson(tags));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        this.validQuestion(question, true);
        question.setId(SecurityUtils.getUser().getId());
        question.setFavourNum(0);
        question.setThumbNum(0);
        int insert = questionMapper.insert(question);
        ThrowUtils.throwIf(insert <= 0, ErrorCode.OPERATION_ERROR);
    }

    /**
     * 校验题目是否合法
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new ServiceException(ErrorCode.PARAM_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(org.apache.commons.lang3.StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAM_ERROR);
        }
        // 有参数则校验
        if (org.apache.commons.lang3.StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "标题过长");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "内容过长");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "答案过长");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "判题配置过长");
        }
    }


}




