package com.minhao.nov.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.dao.ShippingMapper;
import com.minhao.nov.pojo.Shipping;
import com.minhao.nov.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;


    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {

        shipping.setUserId(userId);
        int count=shippingMapper.insert(shipping);

        if (count>0) {
            Map map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新增成功",map);

        }else {
            return ServerResponse.createByError("新增失败");
        }

    }





    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
            int count=shippingMapper.deleteByShippingIdUserId(userId,shippingId);
            if (count>0){
                return ServerResponse.createBySuccess("删除成功");
            }else {
                return ServerResponse.createByError("删除失败");
            }
    }




    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count=shippingMapper.updateByShipping(shipping);
        if (count>0){
            return ServerResponse.createBySuccess("更新成功");
        }else {
            return ServerResponse.createByError("更新失败");
        }

    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping!=null){
            return ServerResponse.createBySuccess(shipping);
        }else {
            return ServerResponse.createByError("无法查询到该地址");
        }


    }

    public ServerResponse<PageInfo> list(Integer userId,int pagenum,int pagesize){
        PageHelper.startPage(pagenum,pagesize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);



    }












}
