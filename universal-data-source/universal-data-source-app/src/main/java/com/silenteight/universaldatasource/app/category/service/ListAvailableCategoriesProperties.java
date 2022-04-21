package com.silenteight.universaldatasource.app.category.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "uds.category")
class ListAvailableCategoriesProperties {

  private List<String> available = new ArrayList<>();
}
