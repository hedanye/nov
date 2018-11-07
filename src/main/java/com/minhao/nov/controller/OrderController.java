package com.minhao.nov.controller;


import com.github.pagehelper.PageInfo;
import com.minhao.nov.common.Const;
import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.pojo.Shipping;
import com.minhao.nov.service.IOrderService;
import com.minhao.nov.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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



    @RequestMapping("cancel.do")
    public ServerResponse<String> cancel(HttpSession session, long orderNo){
        MmallUser user=(MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return orderService.cancel(user.getId(),orderNo);

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());

    }



    @RequestMapping("get_order_cart_product.do")
    public ServerResponse<String> getOrderCartProduct(HttpSession session){
        MmallUser user=(MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return orderService.getOrderCartProduct(user.getId());
        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());

    }


    @RequestMapping("detail.do")
    public ServerResponse<OrderVo> detail(HttpSession session, long orderNo){
        MmallUser user=(MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return orderService.detail(user.getId(),orderNo);

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());

    }


    @RequestMapping("list.do")
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pagenum",defaultValue = "1") int pagenum,
                                         @RequestParam(value = "pagesize",defaultValue = "10")int pagesize){
        MmallUser user=(MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return orderService.getList(user.getId(),pagenum,pagesize);
        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());

    }









}
