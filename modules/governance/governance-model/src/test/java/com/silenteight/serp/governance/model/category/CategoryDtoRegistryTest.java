package com.silenteight.serp.governance.model.category;

import com.silenteight.serp.governance.model.TestResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static com.silenteight.serp.governance.model.category.CategoryFixture.*;
import static org.assertj.core.api.Assertions.*;

class CategoryDtoRegistryTest {

  private static final String SAMPLE_INPUT = "["
      + "  {"
      + "    \"name\": \"" + APTYPE_CATEGORY_NAME + "\","
      + "    \"type\": \"" + APTYPE_CATEGORY_TYPE.toString() + "\","
      + "    \"values\": ["
      + "      \"" + APTYPE_INDIVIDUAL_VALUE + "\","
      + "      \"" + APTYPE_COMPANY_VALUE + "\""
      + "    ]"
      + "  }"
      + "]";

  private static final String SOURCE = "source/to/file";

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldLoadFeaturesFromFile() {
    CategoryRegistry underTest =
        new CategoryRegistry(SOURCE, objectMapper, new TestResourceLoader(SAMPLE_INPUT));

    underTest.init();
    Collection<CategoryDto> allCategories = underTest.getAllCategories();

    assertThat(allCategories).containsExactlyInAnyOrder(APTYPE_CATEGORY);
  }

  @Test
  void shouldThrowExceptionIfConfigNotPresent() {
    CategoryRegistry underTest =
        new CategoryRegistry(SOURCE, objectMapper, new TestResourceLoader(null));

    underTest.init();
    assertThatThrownBy(underTest::getAllCategories)
        .isInstanceOf(NonResolvableCategoriesException.class);
  }
}
