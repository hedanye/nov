package com.minhao.nov.controller;


import com.minhao.nov.common.Const;
import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.pojo.Shipping;
import com.minhao.nov.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    @RequestMapping("create.do")
    public ServerResponse create(HttpSession session, Integer shippingId){
        MmallUser user=(MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return orderService.create(user.getId(),shippingId);

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());


    }

















}
