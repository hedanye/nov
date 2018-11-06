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






















}
