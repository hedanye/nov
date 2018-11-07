package com.minhao.nov.service.impl;

import com.google.common.collect.Lists;
import com.minhao.nov.common.OrderStatusEnum;
import com.minhao.nov.common.PaymentTypeEnum;
import com.minhao.nov.common.ProductStatusEnum;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.dao.*;
import com.minhao.nov.pojo.*;
import com.minhao.nov.service.IOrderService;
import com.minhao.nov.util.BigDecimalUtil;
import com.minhao.nov.util.DateTimeUtil;
import com.minhao.nov.vo.OrderItemVo;
import com.minhao.nov.vo.OrderVo;
import com.minhao.nov.vo.ShippingVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;





    @Override
    public ServerResponse create(Integer userId, Integer shippingId) {
        //从购物车中获取数据
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);


        //计算订单的总价
        ServerResponse serverResponse = this.getCartOrderItem(userId, cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }

        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        BigDecimal bigDecimal=this.price(orderItemList);


        //生成订单
        Order order=this.getOrder(userId,shippingId,bigDecimal);
        if (order==null){
            return ServerResponse.createByError("生成订单错误");
        }
        if (CollectionUtils.isEmpty(orderItemList)){
            return ServerResponse.createByError("🛒为空");
        }

        for (OrderItem o :
                orderItemList) {
            o.setOrderNo(order.getOrderNo());
        }


        //mybatis批量插入
        orderItemMapper.batchInsert(orderItemList);


        //生成成功，减少产品的库存
        this.stock(orderItemList);

        //清空购物车
        this.cleanCart(cartList);


        //返回给前端数据
        OrderVo orderVo=this.assembOrderVo(order,orderItemList);
        return ServerResponse.createBySuccess(orderVo);



    }

    private OrderVo assembOrderVo(Order order,List<OrderItem> orderItemList){
            OrderVo orderVo=new OrderVo();
            List<OrderItemVo> orderItemVoList=Lists.newArrayList();
            orderVo.setOrderNo(order.getOrderNo());
            orderVo.setPayment(order.getPayment());
            orderVo.setPaymentType(order.getPaymentType());
            orderVo.setPaymentTypeDesc(PaymentTypeEnum.codeOf(order.getPaymentType()).getMsg());
            orderVo.setPostage(order.getPostage());
            orderVo.setStatus(order.getStatus());
            orderVo.setShippingId(order.getShippingId());
            Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
            if (shipping!=null){
                orderVo.setReceiverName(shipping.getReceiverName());
                orderVo.setShippingVo(this.assembShipping(shipping));

            }
            orderVo.setStatusDesc(OrderStatusEnum.codeOf(order.getStatus()).getMsg());

            orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));
            orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
            orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
            orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
            orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));


        for (OrderItem o :
                orderItemList) {
                orderItemVoList.add(this.assembOrderItem(o));
    }
        orderVo.setOrderItemVoList(orderItemVoList);

        return orderVo;

    }


    private OrderItemVo assembOrderItem(OrderItem orderItem){
        OrderItemVo orderItemVo=new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        return orderItemVo;
    }







    private ShippingVo assembShipping(Shipping shipping){
        ShippingVo shippingVo=new ShippingVo();
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        return shippingVo;
    }






    private void stock(List<OrderItem> orderItemList){
        for (OrderItem o :
                orderItemList) {
            Product product = productMapper.selectByPrimaryKey(o.getProductId());
            product.setStock(product.getStock()-o.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }

    }


    private void cleanCart(List<Cart> cartList){
        for (Cart c :
                cartList) {
            cartMapper.deleteByPrimaryKey(c.getId());
        }
    }




    private Order getOrder(Integer userId,Integer shippingId,BigDecimal bigDecimal){
        long orderNo=this.getOrderNo();
        Order order=new Order();
        order.setOrderNo(orderNo);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(PaymentTypeEnum.ALIPAY.getCode());
        order.setShippingId(shippingId);
        order.setUserId(userId);
        order.setPayment(bigDecimal);

        int count=orderMapper.insert(order);
        if (count>0){
            return order;
        }
        return null;

    }

    private long getOrderNo(){
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis+new Random().nextInt(100);

    }





    private BigDecimal price(List<OrderItem> orderItemList){
        BigDecimal bigDecimal=new BigDecimal("0");
        for (OrderItem o :
                orderItemList) {
            bigDecimal=BigDecimalUtil.add(bigDecimal.doubleValue(),o.getTotalPrice().doubleValue());

        }
        return bigDecimal;

    }








    //子订单的明细
    private ServerResponse<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList){
        List<OrderItem> orderItemList= Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByError("购物车为空");
        }
        for (Cart c :
                cartList) {
            OrderItem orderItem=new OrderItem();
           Product product= productMapper.selectByPrimaryKey(userId);
            if (product.getStatus().intValue()!= ProductStatusEnum.ON_SALE.getCode()){
                return ServerResponse.createByError("产品不在售卖");
            }

            //校验库存
            if (c.getQuantity()>=product.getStock()){
                return ServerResponse.createByError("库存不足");
            }

            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setQuantity(c.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),c.getQuantity()));

            orderItemList.add(orderItem);


        }
        
        
        return ServerResponse.createBySuccess(orderItemList);
        
        
        
    }














}
