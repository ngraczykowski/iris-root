package com.silenteight.payments.bridge.svb.newlearning.step.composite;

import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import java.sql.ResultSet;
import java.sql.SQLException;

class HitCompositeRowMapper {

  static HitComposite mapRow(ResultSet rs) throws SQLException {
    return HitComposite.builder()
        .hitId(rs.getLong("learning_hit_id"))
        .fkcoVMatchedTag(rs.getString("fkco_v_matched_tag"))
        .fkcoISequence(rs.getString("fkco_i_sequence"))
        .fkcoVListFmmId(rs.getString("fkco_v_list_fmm_id"))
        .fkcoVListCity(rs.getString("fkco_v_list_city"))
        .fkcoVListState(rs.getString("fkco_v_list_state"))
        .fkcoVListCountry(rs.getString("fkco_v_list_country"))
        .fkcoVMatchedTagContent(rs.getString("fkco_v_matched_tag_content"))
        .fkcoVCityMatchedText(rs.getString("fkco_v_city_matched_text"))
        .fkcoVStateMatchedText(rs.getString("fkco_v_state_matched_text"))
        .fkcoVCountryMatchedText(rs.getString("fkco_v_country_matched_text"))
        .fkcoVAddressMatchedText(rs.getString("fkco_v_address_matched_text"))
        .fkcoVListMatchedName(rs.getString("fkco_v_list_matched_name"))
        .fkcoVHitType(rs.getString("fkco_v_hit_type"))
        .fkcoVListType(rs.getString("fkco_v_list_type"))
        .build();
  }
}
