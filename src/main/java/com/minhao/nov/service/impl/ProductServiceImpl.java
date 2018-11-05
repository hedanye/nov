package com.minhao.nov.service.impl;

import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.dao.ProductMapper;
import com.minhao.nov.pojo.Product;
import com.minhao.nov.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;


    @Override
    public ServerResponse save(Product product) {
        if (product!=null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] split = product.getSubImages().split(",");
                if (split.length>0){
                    product.setMainImage(split[0]);
                }
            }

            if (product.getId()!=null){
               int count= productMapper.updateByPrimaryKeySelective(product);
               if (count>0){
                   return ServerResponse.createBySuccess("更新成功");
               }else {
                   return ServerResponse.createByError("更新失败");
               }
            }else {
                int count=productMapper.insert(product);
                if (count>0){
                    return ServerResponse.createBySuccess("新增成功");
                }else {
                    return ServerResponse.createByError("新增失败");
                }
            }



        }
        return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());


    }

    @Override
    public ServerResponse<String> setStatus(Integer productId,Integer status) {
        if (productId==null || status==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }

        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
        int count=productMapper.updateByPrimaryKeySelective(product);
        if (count>0){
            return ServerResponse.createBySuccess("修改成功");
        }
        return ServerResponse.createByError();





    }


}
