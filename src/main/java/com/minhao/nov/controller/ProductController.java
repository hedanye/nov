package com.minhao.nov.controller;

import com.minhao.nov.common.Const;
import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.MmallUser;
import com.minhao.nov.service.IProductService;
import com.minhao.nov.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/product/")
public class ProductController {


    @Autowired
    private IProductService productService;

    @RequestMapping(value = "detail.do",method = RequestMethod.POST)
    public ServerResponse<ProductDetailVo> detail(Integer productId){
            return productService.qiantaidetail(productId);

    }



    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    public ServerResponse list(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "categoryId", required = false)Integer categoryId,
                                 @RequestParam(value = "pagesize",defaultValue = "10") int pagesize,
                                 @RequestParam(value = "pagenum",defaultValue = "1") int pagenum,
                               @RequestParam(value = "orderBy",defaultValue = "")String orderBy){


            return productService.list(keyword,categoryId,pagenum,pagesize,orderBy);

        }












}
