package com.minhao.nov.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.minhao.nov.common.Const;
import com.minhao.nov.common.ProductStatusEnum;
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
import com.minhao.nov.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService categoryService;


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






    public ServerResponse<PageInfo> list(int pagenum,int pagesize){
        PageHelper.startPage(pagenum,pagesize);

        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVos= Lists.newArrayList();
        for (Product p :
                productList) {
            ProductListVo productListVo=assembList(p);
            productListVos.add(productListVo);

        }


        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);




    }




    private ProductListVo assembList(Product product){
        ProductListVo productListVo=new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setCategoryId(product.getCategoryId());
        return productListVo;

    }



    public ServerResponse<PageInfo> search(String productname,Integer productId,int pagenum,int pagesize){
        if (StringUtils.isBlank(productname)||productId==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        PageHelper.startPage(pagenum,pagesize);

        if (StringUtils.isNotBlank(productname)){
            productname=new StringBuilder().append("%").append(productname).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productname, productId);
        List<ProductListVo> productListVos= Lists.newArrayList();
        for (Product p :
                productList) {
            ProductListVo productListVo=assembList(p);
            productListVos.add(productListVo);

        }


        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);


    }




    //前台
    public ServerResponse<ProductDetailVo> qiantaidetail(Integer productId) {
        if (productId==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product.getStatus().intValue()!= ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByError("产品已下架");
        }
        if (product!=null){
            ProductDetailVo productDetailVo=assembDetail(product);
            return ServerResponse.createBySuccess(productDetailVo);
        }
        return ServerResponse.createByError("找不到产品");



    }

    @Override
    public ServerResponse<PageInfo> list(String keyword, Integer categoryId, int pagesize, int pagenum,String orderBy) {
        if (StringUtils.isBlank(keyword)||categoryId==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }


        List<Integer> categoryIdList=new ArrayList<>();

        if (categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category==null&&StringUtils.isBlank(keyword)){
                PageHelper.startPage(pagenum,pagesize);
                List<ProductListVo> productListVos = Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVos);
                return ServerResponse.createBySuccess(pageInfo);

            }

           categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }

        if (StringUtils.isNotBlank(keyword)){
            keyword=new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pagenum,pagesize);
        if (StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] s = orderBy.split("_");
                PageHelper.orderBy(s[0]+" "+s[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword, categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVos= Lists.newArrayList();
        for (Product p :
                productList) {
            ProductListVo productListVo=assembList(p);
            productListVos.add(productListVo);

        }


        PageInfo pageInfo=new PageInfo(productList);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);


    }


}
