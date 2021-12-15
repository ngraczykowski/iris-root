package com.silenteight.payments.bridge.svb.newlearning.step.alert;

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
@Entity(name = "LearningAlert")
public class LearningAlertEntity {

  @Id
  @Column(name = "learning_alert_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fkco_id")
  private Long fkcoId;

  @Column(name = "fkco_v_system_id")
  private String fkcoVSystemId;

  @Column(name = "fkco_v_format")
  private String fkcoVFormat;

  @Column(name = "fkco_v_type")
  private String fkcoVType;

  @Column(name = "fkco_v_transaction_ref")
  private String fkcoVTransactionRef;

  @Column(name = "fkco_v_related_ref")
  private String fkcoVRelatedRef;

  @Column(name = "fkco_v_sens")
  private String fkcoVSens;

  @Column(name = "fkco_v_business_unit")
  private String fkcoVBusinessUnit;

  @Column(name = "fkco_v_application")
  private String fkcoVApplication;

  @Column(name = "fkco_v_currency")
  private String fkcoVCurrency;

  @Column(name = "fkco_f_amount")
  private String fkcoFAmount;

  @Column(name = "fkco_v_content")
  private String fkcoVContent;

  @Column(name = "fkco_b_highlight_all")
  private String fkcoBHighlightAll;

  @Column(name = "fkco_v_value_date")
  private String fkcoVValueDate;

  @Column(name = "fkco_unit")
  private String fkcoUnit;

  @Column(name = "fkco_i_msg_fml_priority")
  private String fkcoIMsgFmlPriority;

  @Column(name = "fkco_i_msg_fml_confidentiality")
  private String fkcoIMsgFmlConfidentiality;

  @Column(name = "fkco_d_app_deadline")
  private String fkcoDAppDeadline;

  @Column(name = "fkco_i_app_priority")
  private String fkcoIAppPriority;

  @Column(name = "fkco_i_normamount")
  private String fkcoINormamount;

  @Column(name = "fkco_v_messageid")
  private String fkcoVMessageid;

  @Column(name = "fkco_v_copy_service")
  private String fkcoVCopyService;

  @Column(name = "fkco_d_filtered_datetime")
  private OffsetDateTime fkcoDFilteredDateTime;

  @Column(name = "fkco_i_blockinghits")
  private String fkcoIBlockinghits;
}
