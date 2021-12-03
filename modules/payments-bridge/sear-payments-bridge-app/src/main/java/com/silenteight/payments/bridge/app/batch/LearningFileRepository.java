package com.silenteight.payments.bridge.app.batch;

import org.springframework.data.repository.Repository;

import java.util.List;

interface LearningFileRepository extends Repository<LearningFileEntity, Long> {

  LearningFileEntity save(LearningFileEntity learningFileEntity);

  List<LearningFileEntity> findAllByFileNameAndBucketName(String fileName, String bucketName);

}
