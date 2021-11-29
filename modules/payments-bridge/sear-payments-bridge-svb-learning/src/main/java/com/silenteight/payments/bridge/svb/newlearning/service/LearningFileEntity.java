package com.silenteight.payments.bridge.svb.newlearning.service;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Data
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "LearningFile")
class LearningFileEntity extends BaseEntity {


  @Id
  @Column(name = "learning_file_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "bucket_name")
  private String bucketName;

  @Column(name = "status")
  private String status;
}
