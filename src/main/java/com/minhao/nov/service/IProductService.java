package com.minhao.nov.service;

import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.Product;

import javax.servlet.http.HttpSession;

public interface IProductService {


    ServerResponse save( Product product);

    ServerResponse<String> setStatus(Integer productId,Integer status);




























}
