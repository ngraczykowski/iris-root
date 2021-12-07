package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.listrecord;

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
@Entity(name = "LearningListedRecord")
public class LearningListedRecordEntity {

  @Id
  @Column(name = "learning_listed_record_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fkco_id")
  private Long fkcoId;

  @Column(name = "fkco_v_list_fmm_id")
  private String fkcoVListFmmId;

  @Column(name = "fkco_v_list_official_ref")
  private String fkcoVListOfficialRef;

  @Column(name = "fkco_v_list_type")
  private String fkcoVListType;

  @Column(name = "fkco_v_list_origin")
  private String fkcoVListOrigin;

  @Column(name = "fkco_v_list_designation")
  private String fkcoVListDesignation;

  @Column(name = "fkco_v_list_pep")
  private String fkcoVListPep;

  @Column(name = "fkco_v_list_fep")
  private String fkcoVListFep;

  @Column(name = "fkco_v_list_name")
  private String fkcoVListName;

  @Column(name = "fkco_v_list_city")
  private String fkcoVListCity;

  @Column(name = "fkco_v_list_state")
  private String fkcoVListState;

  @Column(name = "fkco_v_list_country")
  private String fkcoVListCountry;

  @Column(name = "fkco_v_list_userdata1")
  private String fkcoVListUserdata1;

  @Column(name = "fkco_v_list_userdata2")
  private String fkcoVListUserdata2;

  @Column(name = "fkco_v_list_keyword")
  private String fkcoVListKeyword;

  @Column(name = "fkco_v_list_add_info")
  private String fkcoVListAddInfo;
}
