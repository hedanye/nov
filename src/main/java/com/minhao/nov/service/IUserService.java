package com.minhao.nov.service;

import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;

public interface IUserService {





    ServerResponse<MmallUser> login(String username, String password);

    ServerResponse<String> register(MmallUser user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, MmallUser user);

    ServerResponse<MmallUser> updateInformation(MmallUser user);

    ServerResponse<MmallUser> getInformation(Integer userId);

    ServerResponse checkAdminRole(MmallUser user);






}
