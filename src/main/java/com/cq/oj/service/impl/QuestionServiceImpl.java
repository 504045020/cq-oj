package com.cq.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cq.oj.mapper.QuestionMapper;
import com.cq.oj.model.entity.Question;
import com.cq.oj.service.QuestionService;
import org.springframework.stereotype.Service;

/**
* @author 50404
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-07-05 16:36:40
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




