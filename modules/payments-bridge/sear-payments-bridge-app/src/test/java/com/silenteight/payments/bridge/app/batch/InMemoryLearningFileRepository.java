package com.silenteight.payments.bridge.app.batch;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class InMemoryLearningFileRepository implements LearningFileRepository {

  private static final ArrayList<LearningFileEntity> LEARNING_FILE_ENTITIES = new ArrayList<>();

  @Override
  @NonNull
  public LearningFileEntity save(LearningFileEntity learningFileEntity) {
    LEARNING_FILE_ENTITIES.add(learningFileEntity);
    return learningFileEntity;
  }

  @Override
  @NonNull
  public List<LearningFileEntity> findAllByFileNameAndBucketName(
      String fileName, String bucketName) {
    return LEARNING_FILE_ENTITIES
        .stream()
        .filter(lf -> fileName.equals(lf.getFileName()) && bucketName.equals(lf.getBucketName()))
        .collect(Collectors.toList());
  }

}
