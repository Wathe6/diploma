package com.envoi.diploma.service;

import java.util.List;
import java.util.Map;

public interface CRUDService<T, V>
{
    T create(Map<String, Object> data);
    T save(T entity);
    T getById(V id);
    List<T> findAll();
    List<T> saveAll(List<T> entities);
    void deleteAll(List<T> entities);
    void deleteById(V id);
}