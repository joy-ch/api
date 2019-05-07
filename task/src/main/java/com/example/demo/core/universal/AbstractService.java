 /*
  * Copyright 2019 tuhu.cn All right reserved. This software is the
  * confidential and proprietary information of tuhu.cn ("Confidential
  * Information"). You shall not disclose such Confidential Information and shall
  * use it only in accordance with the terms of the license agreement you entered
  * into with Tuhu.cn
  */
 package com.example.demo.core.universal;

 import org.apache.ibatis.exceptions.TooManyResultsException;
 import org.hibernate.service.spi.ServiceException;
 import org.springframework.beans.factory.annotation.Autowired;
 import tk.mybatis.mapper.entity.Condition;

 import java.lang.reflect.Field;
 import java.lang.reflect.ParameterizedType;
 import java.util.List;

 /**
  * @Description: 基于通用MyBatis Mapper插件的Service接口的实现
  * @author chendesheng chendesheng@tuhu.cn
  * @since 2019/5/7 14:25
  */
 public class AbstractService<T> implements Service<T> {
     @Autowired
     protected Mapper<T> mapper;
    
     private Class<T> modelClass; // 当前泛型真实类型的Class
    
     @SuppressWarnings("unchecked")
     public AbstractService() {
         ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
         modelClass = (Class<T>) pt.getActualTypeArguments()[0];
     }
    
     @Override
     public Integer insert(T model) {
         return mapper.insertSelective(model);
     }
    
     @Override
     public Integer deleteById(String id) {
         return mapper.deleteByPrimaryKey(id);
     }
    
     @Override
     public Integer deleteByIds(String ids) {
         return mapper.deleteByIds(ids);
     }
    
     @Override
     public Integer update(T model) {
         return mapper.updateByPrimaryKeySelective(model);
     }
    
     @Override
     public T selectById(String id) {
         return mapper.selectByPrimaryKey(id);
     }
    
     @Override
     public T selectBy(String fieldName, Object value) throws TooManyResultsException {
         try {
             T model = modelClass.newInstance();
             Field field = modelClass.getDeclaredField(fieldName);
             field.setAccessible(true);
             field.set(model, value);
             return mapper.selectOne(model);
         } catch (ReflectiveOperationException e) {
             throw new ServiceException(e.getMessage(), e);
         }
     }
    
     @Override
     public List<T> selectListBy(String fieldName, Object value)  {
         try {
             T model = modelClass.newInstance();
             Field field = modelClass.getDeclaredField(fieldName);
             field.setAccessible(true);
             field.set(model, value);
             return mapper.select(model);
         } catch (ReflectiveOperationException e) {
             throw new ServiceException(e.getMessage(), e);
         }
     }
    
     @Override
     public List<T> selectListByIds(String ids) {
         return mapper.selectByIds(ids);
     }
    
     @Override
     public List<T> selectByCondition(Condition condition) {
         return mapper.selectByCondition(condition);
     }
    
     @Override
     public List<T> selectAll() {
         return mapper.selectAll();
     }
    
     @Override
     public List<T> select(T record){
         return mapper.select(record);
     }
    
     @Override
     public T selectOne(T recoed){
         return mapper.selectOne(recoed);
     }
     
 }
