package com.minhao.nov.service;

import com.github.pagehelper.PageInfo;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.Product;
import com.minhao.nov.vo.ProductDetailVo;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

public interface IProductService {


    ServerResponse save( Product product);

    ServerResponse<String> setStatus(Integer productId,Integer status);


    ServerResponse<ProductDetailVo> detail(Integer productId);

    ServerResponse<PageInfo> list(int pagenum, int pagesize);

    ServerResponse<PageInfo> search(String productname,Integer productId,int pagenum,int pagesize);

    ServerResponse<ProductDetailVo> qiantaidetail(Integer productId);

    ServerResponse<PageInfo> list(String keyword, Integer categoryId, int pagesize, int pagenum,String orderBy);
























}
