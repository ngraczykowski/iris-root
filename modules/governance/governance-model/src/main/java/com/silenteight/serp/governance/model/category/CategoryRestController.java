package com.silenteight.serp.governance.model.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CategoryRestController {

  @NonNull
  private final CategoryRegistry registry;

  @GetMapping("/v1/categories")
  @PreAuthorize("isAuthorized('LIST_CATEGORIES')")
  public ResponseEntity<CategoriesListDto> list() {
    CategoriesListDto result = new CategoriesListDto(registry.getAllCategories());
    return ok(result);
  }
}
