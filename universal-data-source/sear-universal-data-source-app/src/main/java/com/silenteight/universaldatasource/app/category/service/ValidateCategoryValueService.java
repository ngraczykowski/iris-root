package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.ValidateCategoryValueUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class ValidateCategoryValueService implements ValidateCategoryValueUseCase {

  private final ListAvailableCategoriesUseCase listAvailableCategories;

  @Override
  public void isValid(
      List<CreateCategoryValuesRequest> categoryValue) {

    List<Category> categoriesList = getCategoryList();

    for (CreateCategoryValuesRequest categoryValuesRequest : categoryValue) {

      Category category = getCategory(categoriesList, categoryValuesRequest.getCategory());
      List<CategoryValue> categoryValuesList = categoryValuesRequest.getCategoryValuesList();

      categoryValuesList.forEach(c -> validateCategoryValue(category, c));
    }
  }

  private List<Category> getCategoryList() {
    var availableCategories = listAvailableCategories.getAvailableCategories();
    return availableCategories.getCategoriesList();
  }

  private Category getCategory(List<Category> categoriesList, String category) {
    return categoriesList.stream()
        .filter(c -> c.getName().equals(category))
        .findFirst()
        .orElseThrow(() ->
            new CategoryValueException("Invalid category given for CategoryValues: " + category));
  }

  private void validateCategoryValue(Category category, CategoryValue categoryValue) {
    if (!category.getAllowedValuesList().contains(categoryValue.getSingleValue())) {
      throw new CategoryValueException(
          "Invalid value of CategoryValue: " + categoryValue.getSingleValue());
    }
  }

  private static class CategoryValueException extends RuntimeException {

    private static final long serialVersionUID = -422566344416976769L;

    CategoryValueException(String message) {
      super(message);
    }
  }
}



