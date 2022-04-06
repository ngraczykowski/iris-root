package com.silenteight.serp.governance.model.category;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.model.domain.DomainConstants.SOLVING_MODEL_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = SOLVING_MODEL_ENDPOINT_TAG)
class CategoryRestController {

  @NonNull
  private final CategoryRegistry registry;

  @GetMapping("/v1/categories")
  @PreAuthorize("isAuthorized('LIST_CATEGORIES')")
  public ResponseEntity<CategoriesListDto> list() {
    log.info(("Listing categories."));
    CategoriesListDto result = new CategoriesListDto(registry.getAllCategories());
    return ok(result);
  }
}
