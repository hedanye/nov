package com.minhao.nov.controller.backend;


import com.github.pagehelper.PageInfo;
import com.minhao.nov.common.Const;
import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.pojo.Product;
import com.minhao.nov.service.IProductService;
import com.minhao.nov.service.IUserService;
import com.minhao.nov.vo.ProductDetailVo;
import com.minhao.nov.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService userService;



    @Autowired
    private IProductService productService;



    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    public ServerResponse save(HttpSession session, Product product){
       MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
       if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

                return productService.save(product);


            }else {
                return ServerResponse.createByError("无权限");
            }

       }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");

    }


    @RequestMapping(value = "setStatus.do",method = RequestMethod.POST)
    public ServerResponse<String> setStatus(HttpSession session,Integer productId,Integer status){
        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

                return productService.setStatus(productId,status);


            }else {
                return ServerResponse.createByError("无权限");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");

    }




    @RequestMapping(value = "detail.do",method = RequestMethod.POST)
    public ServerResponse<ProductDetailVo> detail(HttpSession session, Integer productId){
        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

                return productService.detail(productId);

            }else {
                return ServerResponse.createByError("无权限");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");

    }



    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    public ServerResponse list(HttpSession session, @RequestParam(value = "pagesize",defaultValue = "10") int pagesize, @RequestParam(value = "pagenum",defaultValue = "1") int pagenum){
        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            if (userService.checkAdminRole(user).isSuccess()){

                return productService.list(pagenum,pagesize);

            }else {
                return ServerResponse.createByError("无权限");
            }

        }
        return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");

    }

















}
