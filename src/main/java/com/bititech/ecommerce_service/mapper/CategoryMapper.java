package com.bititech.ecommerce_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bititech.ecommerce_service.domain.Category;
import com.bititech.ecommerce_service.dto.category.CategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "parentId", source = "parent.id")
  CategoryDto toDto(Category entity);

  // This method will be automatically implemented by MapStruct
  // to iterate over the list and apply the toDto method.
  List<CategoryDto> toDtoList(List<Category> entities);
}
