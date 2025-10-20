package com.bititech.ecommerce_service.util;

import org.springframework.data.domain.*;

public final class Pages {
  private Pages(){}
  public static Pageable page(int page, int size) {
    return PageRequest.of(Math.max(0,page), Math.min(Math.max(1,size), 200), Sort.by("id").descending());
  }
}
