package com.silenteight.connector.ftcc.callback.handler;

import lombok.*;

import com.silenteight.connector.ftcc.callback.handler.BatchCompletedEntity.BatchCompletedId;
import com.silenteight.sep.base.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table
@IdClass(BatchCompletedId.class)
class BatchCompletedEntity extends BaseEntity {

  @Id
  @NonNull
  @ToString.Include
  @Column(name = "batch_id")
  private UUID batchId;

  @Id
  @NonNull
  @Column(name = "analysis_id")
  private String analysisId;

  @Data
  static class BatchCompletedId implements Serializable {

    private static final long serialVersionUID = 9116742934258978862L;

    private UUID batchId;
    private String analysisId;
  }
}
