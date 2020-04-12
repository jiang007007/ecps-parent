package com.nike.service.impl;

import com.nike.dao.BaseMapper;
import com.nike.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T> {

    protected BaseMapper<T> baseMapper;


    @Override
    public int insert(T entity) {
        return 0;
    }

    @Override
    public int insertSelective(T entity) {
        return 0;
    }

    @Override
    public int deletedByPrimaryKey(String id) {
        return 0;
    }

    @Override
    public T selectByPrimaryKey(String id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(T entity) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(T entity) {
        return 0;
    }
}
