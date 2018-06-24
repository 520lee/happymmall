package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.list(user.getId());
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest httpServletRequest, Integer count, Integer productId){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.add(user.getId(),productId,count);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest, Integer count, Integer productId){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.update(user.getId(),productId,count);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest, String productIds){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.deleteProduct(user.getId(),productIds);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
    }

    //全选
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
    }
    //全反选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest) {
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }
    //单独选
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest, Integer productId) {
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }
    //单独反选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest, Integer productId) {
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    //查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest){
        String login_token = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(login_token)){
            User user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(login_token), User.class);
            if (user != null){
                return iCartService.getCartProductCount(user.getId());
            }
        }
        return ServerResponse.createBySuccess(0);
    }
}
