package com.nike.service;

public interface BaseService<T> {
    int insert(T entity);
    int insertSelective(T entity);
    int deletedByPrimaryKey(String id);
    T selectByPrimaryKey(String id);
    int updateByPrimaryKeySelective(T entity);
    int updateByPrimaryKey(T entity);
}
