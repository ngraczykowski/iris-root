package com.silenteight.payments.bridge.svb.newlearning.batch.step.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "LearningActionStatus")
public class LearningActionStatusEntity {

  @Id
  @Column(name = "learning_action_status_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fkco_id")
  private Long fkcoId;

  @Column(name = "fkco_v_status_name")
  private String fkcoVStatusName;

  @Column(name = "fkco_v_status_behavior")
  private String fkcoVStatusBehavior;
}
