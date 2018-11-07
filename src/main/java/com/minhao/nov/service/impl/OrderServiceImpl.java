package com.minhao.nov.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.minhao.nov.common.*;
import com.minhao.nov.dao.*;
import com.minhao.nov.pojo.*;
import com.minhao.nov.service.IOrderService;
import com.minhao.nov.util.BigDecimalUtil;
import com.minhao.nov.util.DateTimeUtil;
import com.minhao.nov.vo.OrderItemVo;
import com.minhao.nov.vo.OrderProductVo;
import com.minhao.nov.vo.OrderVo;
import com.minhao.nov.vo.ShippingVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
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

    @Override
    public ServerResponse<String> cancel(Integer userId, long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order!=null){
            if (order.getStatus()!= OrderStatusEnum.NO_PAY.getCode()){
                return ServerResponse.createByError("已付款 无法取消");
            }
            Order order1=new Order();
            order1.setId(order.getId());
            order1.setStatus(OrderStatusEnum.CANCEL.getCode());

            int count=orderMapper.updateByPrimaryKeySelective(order1);
            if (count>0){
                return ServerResponse.createBySuccess();
            }else {
                return ServerResponse.createByError();
            }
        }
        return ServerResponse.createByError("找不到该用户的订单");


    }



    public ServerResponse getOrderCartProduct(Integer userId){
        OrderProductVo orderProductVo=new OrderProductVo();


        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
       ServerResponse serverResponse= this.getCartOrderItem(userId,cartList);
       if (!serverResponse.isSuccess()){
            return serverResponse;
       }
       List<OrderItem> orderItemList= (List<OrderItem>) serverResponse.getData();

       List<OrderItemVo> orderItemVoList=Lists.newArrayList();


       BigDecimal bigDecimal=new BigDecimal("0");
        for (OrderItem o :
                orderItemList) {
            bigDecimal = BigDecimalUtil.add(bigDecimal.doubleValue(), o.getTotalPrice().doubleValue());
            orderItemVoList.add(assembOrderItem(o));
        }

        orderProductVo.setProductTotalPrice(bigDecimal);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        return ServerResponse.createBySuccess(orderProductVo);







    }

    @Override
    public ServerResponse<OrderVo> detail(Integer userId, long orderNo) {

        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order!=null){
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
            OrderVo orderVo = this.assembOrderVo(order, orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return ServerResponse.createByError("没有找到订单");

    }


    public ServerResponse<PageInfo> getList(Integer userId,int pagenum,int pagesize){

        PageHelper.startPage(pagenum,pagesize);


       List<Order> orderList= orderMapper.selectByUserId(userId);
        List<OrderVo> orderVoList=this.assembOrderVo(orderList,userId);



        PageInfo pageInfo=new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);



    }




    //backed

    public ServerResponse<PageInfo> manageList(int pagenum,int pagesize){
        PageHelper.startPage(pagenum,pagesize);
        List<Order> orderList = orderMapper.selectAllOrder();
        List<OrderVo> orderVoList = this.assembOrderVo(orderList, null);

        PageInfo pageInfo=new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);



    }




    public ServerResponse<OrderVo> manageDetail(Integer userId,long orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order!=null){

            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
            OrderVo orderVo = this.assembOrderVo(order, orderItemList);
            return ServerResponse.createBySuccess(orderVo);


        }else {
            return ServerResponse.createByError("订单不存在");
        }


    }



    public ServerResponse<PageInfo> search(Integer userId,long orderNo,int pagenum,int pagesize){

        PageHelper.startPage(pagenum,pagesize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order!=null){

            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVo orderVo = this.assembOrderVo(order, orderItemList);

            PageInfo pageInfo=new PageInfo(Lists.newArrayList(order));
            pageInfo.setList(Lists.newArrayList(orderVo));
            return ServerResponse.createBySuccess(pageInfo);
        }else {
            return ServerResponse.createByError("订单不存在");
        }




    }



    public ServerResponse<String> send_goods(long orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order!=null){
            if (order.getStatus()==OrderStatusEnum.PAY.getCode()){
                order.setStatus(OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.createBySuccess("发货成功");
            }



        }
        return ServerResponse.createByError("找不到订单");


    }


























    private List<OrderVo> assembOrderVo(List<Order> orderList,Integer userId){
        List<OrderVo> orderVoList=Lists.newArrayList();
        for (Order o :
                orderList) {
            List<OrderItem> orderItemList=Lists.newArrayList();
            if (userId==null){
                orderItemList=orderItemMapper.getByOrderNo(o.getOrderNo());
            }else {
                orderItemList=orderItemMapper.getByOrderNoUserId(o.getOrderNo(),userId);
            }
            OrderVo orderVo=assembOrderVo(o,orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
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
