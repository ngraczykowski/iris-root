package com.silenteight.scb.feeding.infrastructure.category;

import com.silenteight.universaldatasource.api.library.category.v2.CategoryShared;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@ConfigurationProperties("silenteight.scb-bridge")
record CategoriesProperties(List<CategoryShared> categories) {}
