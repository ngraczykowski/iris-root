package com.silenteight.serp.governance.model.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;

import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Slf4j
public class CategoryRegistry {

  @NonNull
  private final String source;

  @NonNull
  private final ObjectMapper objectMapper;

  @NonNull
  private final ResourceLoader resourceLoader;

  private final AtomicReference<List<CategoryDto>> categoriesRef = new AtomicReference<>();

  public List<CategoryDto> getAllCategories() {
    return Optional.ofNullable(categoriesRef.get())
        .orElseThrow(NonResolvableCategoriesException::new);
  }

  @PostConstruct
  void init() {
    try (InputStream inputStream = resourceLoader.getResource(source).getInputStream()) {
      categoriesRef.set(parseCategories(inputStream));
      log.debug("Categories loaded from file: "
          + " source=" + source
          + " entriesCount=" + categoriesRef.get().size());
    } catch (Exception e) {
      log.error("Loading categories failed, could not load file: " + source, e);
    }
  }

  private List<CategoryDto> parseCategories(InputStream inputStream) throws IOException {
    TypeReference<List<CategoryJson>> categoriesWrapperType
        = new TypeReference<>() {};
    List<CategoryJson> categoriesWrapper =
        objectMapper.readValue(inputStream, categoriesWrapperType);

    return categoriesWrapper.stream()
        .map(CategoryJson::toDto)
        .collect(toUnmodifiableList());
  }
}
