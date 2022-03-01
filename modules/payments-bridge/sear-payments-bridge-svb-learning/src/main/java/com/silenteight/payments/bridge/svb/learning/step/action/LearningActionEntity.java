package com.silenteight.payments.bridge.svb.learning.step.action;

import lombok.*;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Data
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "LearningAction")
public class LearningActionEntity {

  @Id
  @Column(name = "learning_action_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fkco_messages")
  private Long fkcoMessages;

  @Column(name = "fkco_v_action_comment")
  private String fkcoVActionComment;

  @Column(name = "fkco_action_date")
  private String fkcoActionDate;

  @Column(name = "fkco_d_action_datetime")
  private OffsetDateTime fkcoDActionDatetime;

  @Column(name = "fkco_operator")
  private String fkcoOperator;

  @Column(name = "fkco_status")
  private String fkcoStatus;

  @Column(name = "fkco_i_total_action")
  private String fkcoITotalAction;

  @Column(name = "fkco_v_status_name")
  private String fkcoVStatusName;

  @Column(name = "fkco_v_status_behavior")
  private String fkcoVStatusBehavior;
}
