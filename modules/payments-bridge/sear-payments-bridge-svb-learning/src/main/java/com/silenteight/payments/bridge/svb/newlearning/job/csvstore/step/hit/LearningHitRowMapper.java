package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.hit;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
final class LearningHitRowMapper implements RowMapper<LearningHitEntity> {

  @Override
  public LearningHitEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    return LearningHitEntity
        .builder()
        .fkcoMessages(rs.getLong("fkco_messages"))
        .fkcoBHighlightHit(rs.getString("fkco_b_highlight_hit"))
        .fkcoVNameMatchedText(rs.getString("fkco_v_name_matched_text"))
        .fkcoVAddressMatchedText(rs.getString("fkco_v_address_matched_text"))
        .fkcoVCityMatchedText(rs.getString("fkco_v_city_matched_text"))
        .fkcoVStateMatchedText(rs.getString("fkco_v_state_matched_text"))
        .fkcoVCountryMatchedText(rs.getString("fkco_v_country_matched_text"))
        .fkcoVListMatchedName(rs.getString("fkco_v_list_matched_name"))
        .fkcoVFmlType(rs.getString("fkco_v_fml_type"))
        .fkcoIFmlPriority(rs.getString("fkco_i_fml_priority"))
        .fkcoIFmlConfidentiality(rs.getString("fkco_i_fml_confidentiality"))
        .fkcoVHitMatchLevel(rs.getString("fkco_v_hit_match_level"))
        .fkcoVHitType(rs.getString("fkco_v_hit_type"))
        .fkcoINonblocking(rs.getString("fkco_i_nonblocking"))
        .fkcoIBlocking(rs.getString("fkco_i_blocking"))
        .fkcoListedRecord(rs.getString("fkco_listed_record"))
        .fkcoFilteredDate(rs.getString("fkco_filtered_date"))
        .fkcoDFilteredDatetime1(rs.getString("fkco_d_filtered_datetime_1"))
        .fkcoVMatchedTag(rs.getString("fkco_v_matched_tag"))
        .fkcoVMatchedTagContent(rs.getString("fkco_v_matched_tag_content"))
        .fkcoISequence(rs.getString("fkco_i_sequence"))
        .build();
  }
}
