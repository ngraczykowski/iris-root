package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
class CategoryUpdater {

  private final CategoryRepository categoryRepository;

  @EventListener(ApplicationStartedEvent.class)
  @Transactional
  public void onApplicationStart() {
    log.info("Updating categories started");

    updateCategories();

    log.info("Updating categories finished");
  }

  private void updateCategories() {
    var categories = CategoryModelHolder.getCategories();
    var existingCategories = categoryRepository.findAll();

    categories.forEach(categoryModel -> {
      var name = categoryModel.getName();

      if (categoryNameDoesNotExist(name, existingCategories)) {
        saveCategory(categoryModel);
      } else {
        log.debug("Category name: {} already exist in database.", name);
      }
    });
  }

  private void saveCategory(CategoryModel categoryModel) {
    var entity = new CategoryEntity(categoryModel);

    categoryRepository.save(entity);
    log.info("Category name: {} has been added into database.", categoryModel.getName());
  }

  private boolean categoryNameDoesNotExist(String name, Collection<CategoryEntity> categories) {
    return categories.stream()
        .noneMatch(t -> t.getName().equalsIgnoreCase(name));
  }
}
