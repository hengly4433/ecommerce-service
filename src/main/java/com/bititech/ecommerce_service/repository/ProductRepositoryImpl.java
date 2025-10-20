package com.bititech.ecommerce_service.repository;

import jakarta.persistence.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import com.bititech.ecommerce_service.domain.Product;

import java.util.*;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Page<Product> search(String q, Long categoryId, Pageable pageable) {
    String where = " where 1=1 ";
    Map<String,Object> params = new HashMap<>();
    if (q != null && !q.isBlank()) {
      where += " and (lower(p.name) like :q or lower(p.sku) like :q) ";
      params.put("q", "%"+q.toLowerCase()+"%");
    }
    if (categoryId != null) {
      where += " and p.category.id = :cid ";
      params.put("cid", categoryId);
    }
    String base = " from Product p " + where;
    var dataQ = em.createQuery("select p" + base + " order by p.id desc", Product.class);
    var countQ = em.createQuery("select count(p)" + base, Long.class);
    params.forEach((k,v)->{ dataQ.setParameter(k, v); countQ.setParameter(k, v); });

    dataQ.setFirstResult((int) pageable.getOffset());
    dataQ.setMaxResults(pageable.getPageSize());
    List<Product> content = dataQ.getResultList();
    long total = countQ.getSingleResult();
    return new PageImpl<>(content, pageable, total);
  }
}
