package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;
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

  private static Category getCategory(List<Category> categoriesList, String category) {
    return categoriesList.stream()
        .filter(c -> c.getName().equals(category))
        .findFirst()
        .orElseThrow(() ->
            new CategoryException("Invalid category given for CategoryValues: " + category));
  }

  private static void validateCategoryValue(Category category, CategoryValue categoryValue) {
    if (!isCategoryValueAllowed(category, categoryValue) &&
        !isCategoryAnyString(category.getType())) {
      throw new CategoryValueException(
          "Invalid value of CategoryValue: " + categoryValue.getSingleValue());
    }
  }

  private static boolean isCategoryValueAllowed(Category category, CategoryValue categoryValue) {
    return category.getAllowedValuesList().contains(categoryValue.getSingleValue());
  }

  private static boolean isCategoryAnyString(CategoryType categoryType) {
    return CategoryType.ANY_STRING.equals(categoryType);
  }
}



