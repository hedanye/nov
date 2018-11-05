package com.minhao.nov.service;

import com.minhao.nov.common.ServerResponse;
import com.minhao.nov.pojo.Category;

import java.util.List;

public interface ICategoryService {



    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);


























}
