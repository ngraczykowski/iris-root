package com.silenteight.hsbc.datasource.category.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.category.ListCategoriesUseCase;
import com.silenteight.hsbc.datasource.category.dto.CategoryDto;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
class CategoryRestController {

  private final ListCategoriesUseCase listCategoriesUseCase;

  @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CategoryDto>> getCategories() {
    return ResponseEntity.ok(listCategoriesUseCase.getCategories());
  }
}
