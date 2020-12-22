package com.silenteight.serp.governance.model;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import javax.transaction.Transactional;

@RequiredArgsConstructor
class ModelActivator {

  private final ModelRepository models;

  @Transactional
  public void activateModel(ModelEntity model) {
    // TODO(bgulowaty): consult

    Collection<ModelEntity> allModels = models.findAll();
    allModels.forEach(ModelEntity::deactivate);

    model.activate();
  }
}
