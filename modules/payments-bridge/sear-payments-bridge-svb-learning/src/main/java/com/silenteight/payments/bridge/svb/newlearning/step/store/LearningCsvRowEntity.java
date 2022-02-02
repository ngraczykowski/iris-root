
package com.silenteight.payments.bridge.svb.newlearning.step.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.silenteight.sep.base.common.entity.BaseEntity;

import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "LearningCsvRow")
public class LearningCsvRowEntity extends BaseEntity {

  @Id
  @Column(name = "learning_csv_row_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "job_id")
  private Long jobId;

  @Column(name = "file_name")
  private String fileName;

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

  @Column(name = "fkco_v_action_comment")
  private String fkcoVActionComment;

  @Column(name = "fkco_action_date")
  private String fkcoActionDate;

  @Column(name = "fkco_d_filtered_datetime")
  private String fkcoDFilteredDatetime;

  @Column(name = "fkco_d_action_datetime")
  private String fkcoDActionDatetime;

  @Column(name = "fkco_operator")
  private String fkcoOperator;

  @Column(name = "fkco_status")
  private String fkcoStatus;

  @Column(name = "fkco_i_total_action")
  private String fkcoITotalAction;

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
  private String fkcovhittype;

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

  @Column(name = "fkco_v_status_name")
  private String fkcoVStatusName;

  @Column(name = "fkco_v_status_behavior")
  private String fkcoVStatusBehavior;

  @Column(name = "fkco_i_blockinghits")
  private String fkcoIBlockinghits;
}
