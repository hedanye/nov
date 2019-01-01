package com.minhao.nov.controller;

import com.minhao.nov.common.Const;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.service.IUserService;

import com.minhao.nov.util.CookieUtil;
import com.minhao.nov.util.JsonUtil;
import com.minhao.nov.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */
@RestController
@RequestMapping("/user/")
public class UserController {


    @Autowired
    private IUserService userService;



    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public ServerResponse<MmallUser> login(HttpSession session, String username, String password,HttpServletResponse httpServletResponse){

        ServerResponse<MmallUser> response = userService.login(username, password);
        if (response.isSuccess()){
            //session.setAttribute(Const.CURRENT_USER,response.getData());
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), 60 * 30);
        }

        return response;
    }

    @RequestMapping(value = "loginOut.do",method = RequestMethod.GET)
    public ServerResponse loginOut(HttpServletResponse httpServletResponse,HttpServletRequest request){
        //session.removeAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readToken(request);
        CookieUtil.delToken(request,httpServletResponse);
        RedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }


    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    public ServerResponse<String> register(MmallUser user){
        return userService.register(user);
    }


    @RequestMapping(value = "checkValid.do",method = RequestMethod.POST)
    public ServerResponse<String> checkValid(String str,String type){
        return userService.checkValid(str,type);

    }



    @RequestMapping(value = "getUserInfo.do",method = RequestMethod.POST)
    public ServerResponse getUserInfo(HttpServletRequest request){
        //MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        String loginToken=CookieUtil.readToken(request);
        if (StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByError("未登录");
        }
        String userJsonStr=RedisPoolUtil.get(loginToken);
        MmallUser user=JsonUtil.String2Obj(userJsonStr,MmallUser.class);
        if (user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByError("未登录");
    }




    @RequestMapping(value = "getQuestion.do",method = RequestMethod.POST)
    public ServerResponse<String> getQuestion(String username){
        return userService.selectQuestion(username);
    }



    @RequestMapping(value = "checkAnswer.do",method = RequestMethod.POST)
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        return userService.checkAnswer(username,question,answer);
    }



    @RequestMapping(value = "forgetrestPassword.do",method = RequestMethod.POST)
    public ServerResponse<String> forgetrestPassword(String username,String passwordNew,String uuid){
        return userService.forgetResetPassword(username,passwordNew,uuid);
    }


    @RequestMapping(value = "restpassword.do",method = RequestMethod.POST)
    public ServerResponse<String> restpassword(HttpSession session,String passwordOld,String passwordNew){
        MmallUser user = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByError("未登录");
        }
        return userService.resetPassword(passwordOld,passwordNew,user);

    }

    @RequestMapping(value = "updateInfo.do",method = RequestMethod.POST)
    public ServerResponse<MmallUser> updateInfo(HttpSession session,MmallUser user){
        MmallUser mmallUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if (mmallUser==null){
            return ServerResponse.createByError("未登录");
        }
        user.setId(mmallUser.getId());
        user.setUsername(mmallUser.getUsername());

        ServerResponse<MmallUser> serverResponse=userService.updateInformation(user);
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }





























}
