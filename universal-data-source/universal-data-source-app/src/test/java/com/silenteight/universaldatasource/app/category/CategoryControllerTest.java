package com.silenteight.universaldatasource.app.category;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;
import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;
import com.silenteight.universaldatasource.app.category.adapter.incoming.v2.CategoryController;
import com.silenteight.universaldatasource.app.category.model.CategoryDto;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

  private static List<Category> categories;
  private static List<CategoryDto> categoriesDto;

  @InjectMocks
  private CategoryController categoryController;

  @Mock
  private ListAvailableCategoriesUseCase listAvailableCategoriesUseCase;

  @Mock
  private CategoryMapper categoryMapper;

  @BeforeAll
  static void beforeAll() {
    categories = generateCategories();
    categoriesDto = generateCategoriesDto();
  }

  @BeforeEach
  void beforeEach() {
    when(listAvailableCategoriesUseCase.getAvailableCategories()).thenReturn(
        ListCategoriesResponse.newBuilder().addAllCategories(categories).build());
    when(categoryMapper.mapCategories(categories)).thenReturn(categoriesDto);
    categoryController = new CategoryController(listAvailableCategoriesUseCase, categoryMapper);
  }

  @Test
  void throwNullPointerExceptionWhenMessageDataIsNull() {
    ResponseEntity<List<CategoryDto>> availableCategories =
        categoryController.getAvailableCategories();
    assertEquals(availableCategories.getBody(), categoriesDto);
    assertEquals(availableCategories.getBody().size(), 2);
    assertEquals(availableCategories.getStatusCode(), HttpStatus.OK);
  }

  private static List<Category> generateCategories() {
    return List.of(
        Category.newBuilder()
            .setName("categories/customerType")
            .setDisplayName("Customer Type")
            .setType(
                CategoryType.ENUMERATED)
            .addAllAllowedValues(List.of("I", "C"))
            .setMultiValue(false)
            .build(),
        Category.newBuilder()
            .setName("categories/hitType")
            .setDisplayName("Risk Type")
            .setType(CategoryType.ENUMERATED)
            .addAllAllowedValues(List.of("AML", "OTHER", "SAN", "PEP", "EXITS", "SSC"))
            .setMultiValue(false)
            .build()
    );
  }

  private static List<CategoryDto> generateCategoriesDto() {
    return List.of(
        CategoryDto.builder()
            .name("categories/customerType")
            .displayName("Customer Type")
            .categoryType(
                CategoryType.ENUMERATED.toString())
            .allowedValues(List.of("I", "C"))
            .multiValue(false)
            .build(),
        CategoryDto.builder()
            .name("categories/hitType")
            .displayName("Risk Type")
            .categoryType(CategoryType.ENUMERATED.toString())
            .allowedValues(List.of("AML", "OTHER", "SAN", "PEP", "EXITS", "SSC"))
            .multiValue(false)
            .build()
    );
  }

}
