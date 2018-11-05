package com.minhao.nov.controller.backend;


import com.minhao.nov.common.Const;
import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.service.ICategoryService;
import com.minhao.nov.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;


    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    public ServerResponse add(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue ="0") Integer parentId){
        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            return categoryService.addCategory(categoryName,parentId);

        }else {
            return ServerResponse.createByError("请登录管理员");

        }

    }


    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    public ServerResponse update(HttpSession session,String categoryName, Integer categoryId){
        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            return categoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createByError("请登录管理员");

        }

    }


    @RequestMapping(value = "getChildrenParallel.do",method = RequestMethod.POST)
    public ServerResponse getChildrenParallel(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            return categoryService.getChildrenParallelCategory(categoryId);

        }else {
            return ServerResponse.createByError("请登录管理员");

        }

    }



    @RequestMapping(value = "getChildren.do",method = RequestMethod.POST)
    public ServerResponse getChildren(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        MmallUser user= (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCode(ResponseCode.NEED_LOGIN.getCode(),"未登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            return categoryService.selectCategoryAndChildrenById(categoryId);

        }else {
            return ServerResponse.createByError("请登录管理员");

        }

    }



























}
