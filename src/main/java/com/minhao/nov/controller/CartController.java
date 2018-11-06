package com.minhao.nov.controller;

import com.minhao.nov.common.Const;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.service.ICartService;
import com.minhao.nov.service.IUserService;
import com.minhao.nov.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {


    @Autowired
    private ICartService cartService;





    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return cartService.add(user.getId(),productId,count);
        }
        return ServerResponse.createByError("未登录");

    }


    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> update(HttpSession session, Integer productId, Integer count){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
        return cartService.update(user.getId(),productId,count);


        }
        return ServerResponse.createByError("未登录");

    }


    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> delete(HttpSession session, String productIds){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){

            return cartService.delete(user.getId(),productIds);

        }
        return ServerResponse.createByError("未登录");

    }



    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> list(HttpSession session){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return cartService.list(user.getId());

        }
        return ServerResponse.createByError("未登录");

    }



    //全选
    @RequestMapping(value = "select_all.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> select_all(HttpSession session){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            cartService.select_unselect(user.getId(),null,Const.Cart.CHECKED);


        }
        return ServerResponse.createByError("未登录");

    }
    //全反选
    @RequestMapping(value = "select_unall.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> select_unall(HttpSession session){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
                cartService.select_unselect(user.getId(),null,Const.Cart.UN_CHECKED);

        }
        return ServerResponse.createByError("未登录");

    }

    //单独选
    @RequestMapping(value = "select_one.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> select_one(HttpSession session,Integer productId){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            cartService.select_unselect(user.getId(),productId,Const.Cart.CHECKED);


        }
        return ServerResponse.createByError("未登录");

    }
    //单独反选
    @RequestMapping(value = "select_unone.do",method = RequestMethod.POST)
    public ServerResponse<CartVo> select_unone(HttpSession session,Integer productId){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            cartService.select_unselect(user.getId(),productId,Const.Cart.UN_CHECKED);


        }
        return ServerResponse.createByError("未登录");

    }

    //查看登录用户的购物车数量
    @RequestMapping(value = "getCartCount.do",method = RequestMethod.POST)
    public ServerResponse<Integer> getCartCount(HttpSession session){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return cartService.getCartCount(user.getId());

        }
        return ServerResponse.createBySuccess(0);
    }












}
