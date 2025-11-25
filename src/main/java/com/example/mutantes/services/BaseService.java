package com.example.mutantes.services;

import java.util.List;

public interface BaseService<E> {

  List<E> findAll();

  E findById(Long id);

  E update(Long id, E entity);

  void delete(Long id);
}
