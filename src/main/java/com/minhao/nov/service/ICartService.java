package com.minhao.nov.service;

import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.vo.CartVo;

public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);


    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> delete(Integer userId,String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> select_unselect(Integer userId,Integer productId,Integer checked);

    ServerResponse<Integer> getCartCount(Integer userId);




















}
