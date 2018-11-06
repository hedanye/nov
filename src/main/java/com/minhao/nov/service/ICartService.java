package com.minhao.nov.service;

import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.vo.CartVo;

public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);






















}
