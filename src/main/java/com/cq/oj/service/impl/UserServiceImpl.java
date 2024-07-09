package com.cq.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cq.oj.Aop.SecurityContextHolder;
import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ServiceException;
import com.cq.oj.constant.CacheConstants;
import com.cq.oj.constant.CommonConstant;
import com.cq.oj.mapper.UserMapper;
import com.cq.oj.model.dto.user.UserAddRequest;
import com.cq.oj.model.dto.user.UserLoginRequest;
import com.cq.oj.model.dto.user.UserQueryRequest;
import com.cq.oj.model.dto.user.UserRegisterRequest;
import com.cq.oj.model.entity.User;
import com.cq.oj.model.vo.LoginUserVo;
import com.cq.oj.service.UserService;
import com.cq.oj.util.MD5Util;
import com.cq.oj.util.RedisService;
import com.cq.oj.util.sql.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
* @author 50404
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-07-05 16:14:14
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "chenQi";

    @Resource
    private  UserMapper userMapper;

    @Resource
    private RedisService redisService;

    @Override
    public int addUser(UserAddRequest userAddRequest) {
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        user.setUserPassword(MD5Util.StringInMd5(userAddRequest.getUserPassword()+SALT));
      return  userMapper.insert(user);
    }

    @Override
    public LoginUserVo login(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        checkUser(userAccount,userPassword);
        // 获取用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword,MD5Util.StringInMd5(userPassword+SALT)));
        if(null == user){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"用户不存在");
        }
        // 将用户存信息存入Redis
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        loginUserVo.setToken(getToken(userAccount));
        redisService.setCacheObject(CacheConstants.LOGIN_TOKEN_KEY+ loginUserVo.getToken() , loginUserVo, 30L, TimeUnit.MINUTES);
        SecurityContextHolder.set(CacheConstants.LOGIN_TOKEN_KEY, loginUserVo);
        return loginUserVo;
    }

    @Override
    public long Register(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        checkUser(userAccount,userPassword);
        if(!userPassword.equals(checkPassword)){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"两次密码不一致");
        }

        synchronized (userAccount.intern()){
            long userCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getUserAccount, userAccount));
            if(userCount > 0){
                throw new ServiceException(ErrorCode.PARAM_ERROR,"账号重复");
            }
            // 加密
            String encryptPassword = MD5Util.StringInMd5(userPassword + SALT);
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean result = this.save(user);
            if(!result){
                throw new ServiceException(ErrorCode.PARAM_ERROR,"注册失败,数据库错误");
            }
            return user.getId();
        }
    }

    public String getToken(String userName){
        return MD5Util.StringInMd5(userName+System.currentTimeMillis()+SALT);
    }

    public void checkUser(String userAccount, String userPassword){
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"账号或密码不能为空");
        }
        if(userAccount.length() < 4 || userAccount.length() > 12){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"账号长度不符合要求");
        }
        if(userPassword.length() < 4 || userPassword.length() > 12){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"密码长度不符合要求");
        }
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




