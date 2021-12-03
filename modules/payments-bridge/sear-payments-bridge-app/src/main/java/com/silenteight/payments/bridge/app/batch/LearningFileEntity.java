package com.silenteight.payments.bridge.app.batch;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import javax.persistence.*;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.BUCKET_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.FILE_NAME_PARAMETER;
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

  JobParameters toJobParameters() {
    return new JobParametersBuilder()
        .addString(FILE_NAME_PARAMETER, this.fileName)
        .addString(BUCKET_NAME_PARAMETER, this.bucketName)
        .toJobParameters();
  }
}
