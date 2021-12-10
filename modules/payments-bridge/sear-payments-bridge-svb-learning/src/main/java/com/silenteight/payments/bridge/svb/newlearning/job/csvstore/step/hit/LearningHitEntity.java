package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.hit;

import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Data
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "LearningHit")
public class LearningHitEntity {

  @Id
  @Column(name = "learning_hit_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fkco_messages")
  private Long fkcoMessages;

  @Column(name = "fkco_b_highlight_hit")
  private String fkcoBHighlightHit;

  @Column(name = "fkco_v_name_matched_text")
  private String fkcoVNameMatchedText;

  @Column(name = "fkco_v_address_matched_text")
  private String fkcoVAddressMatchedText;

  @Column(name = "fkco_v_city_matched_text")
  private String fkcoVCityMatchedText;

  @Column(name = "fkco_v_state_matched_text")
  private String fkcoVStateMatchedText;

  @Column(name = "fkco_v_country_matched_text")
  private String fkcoVCountryMatchedText;

  @Column(name = "fkco_v_list_matched_name")
  private String fkcoVListMatchedName;

  @Column(name = "fkco_v_fml_type")
  private String fkcoVFmlType;

  @Column(name = "fkco_i_fml_priority")
  private String fkcoIFmlPriority;

  @Column(name = "fkco_i_fml_confidentiality")
  private String fkcoIFmlConfidentiality;

  @Column(name = "fkco_v_hit_match_level")
  private String fkcoVHitMatchLevel;

  @Column(name = "fkco_v_hit_type")
  private String fkcoVHitType;

  @Column(name = "fkco_i_nonblocking")
  private String fkcoINonblocking;

  @Column(name = "fkco_i_blocking")
  private String fkcoIBlocking;

  @Column(name = "fkco_listed_record")
  private String fkcoListedRecord;

  @Column(name = "fkco_filtered_date")
  private String fkcoFilteredDate;

  @Column(name = "fkco_d_filtered_datetime_1")
  private String fkcoDFilteredDatetime1;

  @Column(name = "fkco_v_matched_tag")
  private String fkcoVMatchedTag;

  @Column(name = "fkco_v_matched_tag_content")
  private String fkcoVMatchedTagContent;

  @Column(name = "fkco_i_sequence")
  private String fkcoISequence;
}
