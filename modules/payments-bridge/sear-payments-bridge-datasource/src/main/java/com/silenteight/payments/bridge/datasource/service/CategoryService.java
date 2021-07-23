package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.category.CategoryFacade;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class CategoryService {

  private final CategoryFacade facade;
}
