package com.minhao.nov.service;

import com.minhao.nov.common.ServerResponse;


public interface IOrderService {


    ServerResponse create(Integer userId, Integer shippingId);


    ServerResponse<String> cancel(Integer userId, long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);





















}
