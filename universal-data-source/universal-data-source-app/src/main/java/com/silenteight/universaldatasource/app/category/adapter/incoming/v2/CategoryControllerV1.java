package com.silenteight.universaldatasource.app.category.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.category.model.CategoryDto;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CategoryControllerV1 {

  private final ListAvailableCategoriesUseCase listAvailableCategoriesUseCase;
  private final CategoryMapper categoryMapper;

  @GetMapping("/api/v1/categories")
  public ResponseEntity<List<CategoryDto>> getAvailableCategories() {
    var categories = listAvailableCategoriesUseCase.getAvailableCategories().getCategoriesList();

    var filteredCategories = categories.stream()
        .filter(category -> !category.getDisplayName().equals("Specific Terms"))
        .filter(category -> !category.getDisplayName().equals("Specific Terms 3"))
        .filter(category -> !category.getDisplayName().equals("Historical Risk Assessment"))
        .collect(Collectors.toList());

    return ResponseEntity.ok(categoryMapper.mapCategories(filteredCategories));
  }
}
