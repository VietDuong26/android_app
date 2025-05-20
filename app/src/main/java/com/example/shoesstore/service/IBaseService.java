package com.example.shoesstore.service;

import java.util.List;

public interface IBaseService<M,D,K>{
   long add(M m);
   long update(M m,K k);
   long delete(K k);
   List<D> getAll();
   D findById(K k);
}
