package com.silenteight.universaldatasource.app.category;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;
import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;
import com.silenteight.universaldatasource.app.category.adapter.incoming.v2.CategoryControllerV1;
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
class CategoryControllerV1Test {

  private static List<Category> categories;
  private static List<CategoryDto> categoriesDto;

  @InjectMocks
  private CategoryControllerV1 categoryControllerV1;

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
    categoryControllerV1 = new CategoryControllerV1(listAvailableCategoriesUseCase, categoryMapper);
  }

  @Test
  void throwNullPointerExceptionWhenMessageDataIsNull() {
    ResponseEntity<List<CategoryDto>> availableCategories =
        categoryControllerV1.getAvailableCategories();
    assertEquals(availableCategories.getBody(), categoriesDto);
    assertEquals(2, availableCategories.getBody().size());
    assertEquals(HttpStatus.OK, availableCategories.getStatusCode());
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
