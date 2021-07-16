package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

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
      var existingCategory = findByName(name, existingCategories);

      if (existingCategory.isPresent()) {
        updateEntity(existingCategory.get(), categoryModel);
      } else {
        saveCategory(categoryModel, name);
      }
    });
  }

  private void saveCategory(CategoryModel categoryModel, String name) {
    var entity = new CategoryEntity(categoryModel);

    categoryRepository.save(entity);
    log.info("Category name: {} has been added into database.", name);
  }

  private void updateEntity(CategoryEntity entity, CategoryModel categoryModel) {
    entity.getAllowedValues().clear();
    entity.getAllowedValues().addAll(categoryModel.getAllowedValues());
    entity.setDisplayName(categoryModel.getDisplayName());
    entity.setMultiValue(categoryModel.isMultiValue());
    entity.setType(categoryModel.getType());

    categoryRepository.save(entity);
    log.debug("Category name: {} has been updated.", entity.getName());
  }

  private Optional<CategoryEntity> findByName(String name,  Collection<CategoryEntity> categories) {
    return categories.stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst();
  }
}
