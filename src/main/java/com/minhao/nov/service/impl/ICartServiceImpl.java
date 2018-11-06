package com.minhao.nov.service.impl;

import com.google.common.collect.Lists;
import com.minhao.nov.common.Const;
import com.minhao.nov.common.ResponseCode;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.dao.CartMapper;
import com.minhao.nov.dao.ProductMapper;
import com.minhao.nov.pojo.Cart;
import com.minhao.nov.pojo.Product;
import com.minhao.nov.service.ICartService;
import com.minhao.nov.service.ICategoryService;
import com.minhao.nov.util.BigDecimalUtil;
import com.minhao.nov.vo.CartProductVo;
import com.minhao.nov.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;


@Service("iCategoryService")
public class ICartServiceImpl implements ICartService {



    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;


    @Override
    public ServerResponse<CartVo> add(Integer userId,Integer productId, Integer count) {
        if (productId==null || count==null){
            return ServerResponse.createByErrorCode(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if (cart==null){
            Cart cart1=new Cart();
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.Cart.CHECKED);
            cart1.setUserId(userId);
            cartMapper.insert(cart1);
        }else {
            count=cart.getQuantity()+count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);

        }
        CartVo cartVo=getLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }



    private CartVo getLimit(Integer userId){
        CartVo cartVo=new CartVo();
        List<CartProductVo> cartProductVoList= Lists.newArrayList();
        List<Cart> cartList=cartMapper.selectCartByUserId(userId);

        BigDecimal bigDecimal=new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)){
            for (Cart c : cartList) {
                CartProductVo cartProductVo=new CartProductVo();
                cartProductVo.setId(c.getId());
                cartProductVo.setUserId(c.getUserId());
                cartProductVo.setProductId(c.getProductId());

                Product product=productMapper.selectByPrimaryKey(c.getProductId());
                if (product!=null){
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductSubtitle(product.getSubtitle());

                    //判断库存
                    int limit=0;
                    if (product.getStock()>=c.getQuantity()){
                        //库存充足
                        limit=c.getQuantity();
                        cartProductVo.setLimitQuantity(Const.LIMIT_NUM_SUCCESS);
                    }else {
                        limit=product.getStock();
                        cartProductVo.setLimitQuantity(Const.LIMIT_NUM_FAIL);
                        Cart cart=new Cart();
                        cart.setId(c.getId());
                        cart.setQuantity(limit);
                        cartMapper.updateByPrimaryKeySelective(cart);
                    }
                    cartProductVo.setQuantity(limit);
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(c.getChecked());

                }
                if (c.getChecked()==Const.Cart.CHECKED){
                    bigDecimal=BigDecimalUtil.add(bigDecimal.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);


            }
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setCartTotalPrice(bigDecimal);
            cartVo.setAllChecked(this.getAllCheck(userId));

        }


        return cartVo;





    }


    private boolean getAllCheck(Integer userId){
        if (userId==null){
            return false;
        }
       return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
    }















}
