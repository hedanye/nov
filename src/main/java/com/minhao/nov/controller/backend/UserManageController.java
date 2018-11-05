package com.minhao.nov.controller.backend;


import com.minhao.nov.common.Const;
import com.minhao.nov.common.Role;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public ServerResponse<MmallUser> login(String username, String password, HttpSession session){
        ServerResponse<MmallUser> serverResponse = userService.login(username, password);
        if (serverResponse.isSuccess()){
            MmallUser user = serverResponse.getData();
            if (user.getRole().intValue()== Role.ROLE_ADMIN.getCode()){
                session.setAttribute(Const.CURRENT_USER,user);
                return serverResponse;
            }else {
                return ServerResponse.createByError("不是管理员");
            }
        }
        return serverResponse;


    }











}
