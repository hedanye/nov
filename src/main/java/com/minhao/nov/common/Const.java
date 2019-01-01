package com.minhao.nov.common;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;


public class Const {


    public static final String TOKEN="token_";

    public static final String CURRENT_USER="currentUser";

    public static final String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";
    public static final String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";

    public static final String EMAIL="email";
    public static final String USERNAME= "username";

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("price_asc","price_desc");
    }







    public interface Cart{
        int CHECKED=1;
        int UN_CHECKED=0;
    }





}
