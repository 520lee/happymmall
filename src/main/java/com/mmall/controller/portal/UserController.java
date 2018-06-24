package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        //service - mybatis - dao
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()){
            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String key = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(key)){
            RedisShardedPoolUtil.del(key);
            CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type){
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest){
        String key = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(key)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(key), User.class);
            if (user != null){
                return ServerResponse.createBySuccess(user);
            }
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest httpServletRequest, String passwordOld, String passwordNew){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iUserService.resetPassword(passwordOld, passwordNew, user);
            }
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpServletRequest httpServletRequest, User user){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User currentUser = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (currentUser != null){
                user.setId(currentUser.getId());
                ServerResponse<User> response = iUserService.updateInformation(user);
                if (response.isSuccess()){
                    response.getData().setUsername(currentUser.getUsername());
                    RedisShardedPoolUtil.set(login_token, JsonUtil.obj2String(response.getData()));
                }
                return response;
            }
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpServletRequest httpServletRequest){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = (JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class));
            if (user != null){
                return iUserService.getinformation(user.getId());
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，强制登录status=10");
        }
}
