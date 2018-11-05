package com.minhao.nov.service.impl;

import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.dao.CategoryMapper;
import com.minhao.nov.dao.ProductMapper;
import com.minhao.nov.pojo.Category;
import com.minhao.nov.pojo.Product;
import com.minhao.nov.service.ICategoryService;
import com.minhao.nov.service.IProductService;
import com.minhao.nov.util.DateTimeUtil;
import com.minhao.nov.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;


@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;


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

    @Override
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        if (productId==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product!=null){
            ProductDetailVo productDetailVo=assembDetail(product);
            return ServerResponse.createBySuccess(productDetailVo);
        }
        return ServerResponse.createByError("找不到产品");



    }



    private ProductDetailVo assembDetail(Product product){
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setName(product.getName());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setDetail(product.getDetail());

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null){
            productDetailVo.setCategoryId(category.getParentId());
        }else {
            productDetailVo.setCategoryId(0);
        }



        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }






}
