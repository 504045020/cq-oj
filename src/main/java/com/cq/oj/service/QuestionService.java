package com.cq.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cq.oj.model.dto.question.QuestionAddRequest;
import com.cq.oj.model.dto.question.QuestionQueryRequest;
import com.cq.oj.model.dto.user.UserQueryRequest;
import com.cq.oj.model.entity.Question;
import com.cq.oj.model.entity.User;

/**
* @author 50404
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-07-05 16:36:40
*/
public interface QuestionService extends IService<Question> {

    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    void addQuestion(QuestionAddRequest questionAddRequest);

    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);
}
