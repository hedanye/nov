package com.minhao.nov.controller;

import com.github.pagehelper.PageInfo;
import com.minhao.nov.common.Const;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.pojo.Shipping;
import com.minhao.nov.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService shippingService;



    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    public ServerResponse add(HttpSession session, Shipping shipping){
        MmallUser user=(MmallUser)session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return shippingService.add(user.getId(),shipping);
        }
        return ServerResponse.createByError("未登录");

    }


    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    public ServerResponse delete(HttpSession session, Integer shippingId){
        MmallUser user=(MmallUser)session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return shippingService.delete(user.getId(),shippingId);
        }
        return ServerResponse.createByError("未登录");

    }


    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    public ServerResponse update(HttpSession session, Shipping shipping ){
        MmallUser user=(MmallUser)session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return shippingService.update(user.getId(),shipping);
        }
        return ServerResponse.createByError("未登录");

    }


    @RequestMapping(value = "select.do",method = RequestMethod.POST)
    public ServerResponse<Shipping> select(HttpSession session, Integer shippingId){
        MmallUser user=(MmallUser)session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return shippingService.select(user.getId(),shippingId);
        }
        return ServerResponse.createByError("未登录");

    }


    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pagenum",defaultValue = "1")int pagenum,
                                         @RequestParam(value = "pagesize",defaultValue = "10")int pagesize){
        MmallUser user=(MmallUser)session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return shippingService.list(user.getId(),pagenum,pagesize);
        }
        return ServerResponse.createByError("未登录");

    }














}
