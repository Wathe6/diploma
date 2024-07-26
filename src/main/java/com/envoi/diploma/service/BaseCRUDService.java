package com.envoi.diploma.service;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public abstract class BaseCRUDService<T, V> implements CRUDService<T, V>
{
    protected JpaRepository<T, V> repository;
    protected Class<T> entityClass;
    public BaseCRUDService(JpaRepository<T, V> repository) {
        this.repository = repository;

        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }
    @Override
    public T create(Map<String, Object> data) {
        try {
            Constructor<T> constructor = entityClass.getDeclaredConstructor(Map.class);
            return constructor.newInstance(data);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("НЕ УДАЛОСЬ КАСТАНУТЬ ЗАПРОС К КЛАССУ");
            return null;
        }
    }
    @Override
    public T save(T entity) {
        return repository.save(entity);
    }
    @Override
    public T getById(V id) {
        return repository.getById(id);
    }
    @Override
    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    @Override
    public void deleteAll(List<T> entities)
    {
        repository.deleteAll(entities);
    }
    @Override
    public void deleteById(V id) {
        repository.deleteById(id);
    }
}
