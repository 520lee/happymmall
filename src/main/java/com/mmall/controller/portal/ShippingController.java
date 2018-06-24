package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpServletRequest httpServletRequest, Shipping shipping){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iShippingService.add(user.getId(),shipping);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse delete(HttpServletRequest httpServletRequest, Integer shippingId){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iShippingService.delete(user.getId(),shippingId);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpServletRequest httpServletRequest, Shipping shipping){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iShippingService.update(user.getId(),shipping);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpServletRequest httpServletRequest, Integer shippingId){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iShippingService.select(user.getId(),shippingId);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iShippingService.list(user.getId(),pageNum,pageSize);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }
}
