package com.minhao.nov.service;

import com.github.pagehelper.PageInfo;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.vo.OrderVo;


public interface IOrderService {


    ServerResponse create(Integer userId, Integer shippingId);


    ServerResponse<String> cancel(Integer userId, long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> detail(Integer userId, long orderNo);

    ServerResponse<PageInfo> getList(Integer userId,int pagenum,int pagesize);




    ServerResponse<PageInfo> manageList(int pagenum,int pagesize);

    ServerResponse<OrderVo> manageDetail(Integer userId,long orderNo);


    ServerResponse<PageInfo> search(long orderNo,int pagenum,int pagesize);

    ServerResponse<String> send_goods(long orderNo);















}
