package com.minhao.nov.controller.backend;


import com.github.pagehelper.PageInfo;
import com.minhao.nov.common.Const;
import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.service.IOrderService;
import com.minhao.nov.service.IUserService;
import com.minhao.nov.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

@RestController
@RequestMapping("/manage/order/")
public class OrderManageController {


    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;



    @RequestMapping("list.do")
    public ServerResponse<PageInfo> list(HttpSession session, @RequestParam(value = "pagenum",defaultValue = "1") int pagenum,
                                         @RequestParam(value = "pagesize",defaultValue = "10")int pagesize){


        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

            return orderService.manageList(pagenum,pagesize);

            }else {
                return ServerResponse.createByError("无权限");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");


    }



    @RequestMapping("detail.do")
    public ServerResponse<OrderVo> detail(HttpSession session, long orderNo){


        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

            return orderService.manageDetail(user.getId(),orderNo);
            }else {
                return ServerResponse.createByError("无权限");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");


    }




    @RequestMapping("search.do")
    public ServerResponse<PageInfo> search(HttpSession session, long orderNo,@RequestParam(value = "pagenum",defaultValue = "1") int pagenum,
                                          @RequestParam(value = "pagesize",defaultValue = "10")int pagesize){


        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

            return orderService.search(orderNo,pagenum,pagesize);

            }else {
                return ServerResponse.createByError("无权限");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");


    }




    @RequestMapping("send_goods.do")
    public ServerResponse<String> send_goods(HttpSession session, long orderNo){


        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

            return orderService.send_goods(orderNo);

            }else {
                return ServerResponse.createByError("无权限");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");


    }





















}
