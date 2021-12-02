package com.silenteight.payments.bridge.svb.newlearning.batch.step.message;

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
@Entity(name = "LearningAlertedMessage")
public class LearningAlertedMessageEntity {

  @Id
  @Column(name = "learning_alerted_message_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fkco_messages")
  private Long fkcoMessages;

  @Column(name = "fkco_d_filtered_datetime")
  private OffsetDateTime fkcoDfilteredDateTime;

  @Column(name = "fkco_i_blockinghits")
  private String fkcoIBlockinghits;
}
