package com.minhao.nov.service;

import com.github.pagehelper.PageInfo;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.Shipping;



public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);


    ServerResponse<PageInfo> list(Integer userId, int pagenum, int pagesize);















}
