package com.silenteight.payments.bridge.svb.newlearning.step.composite;

import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import java.sql.ResultSet;
import java.sql.SQLException;

class HitCompositeRowMapper {

  static HitComposite mapRow(ResultSet resultSet) throws SQLException {
    return HitComposite.builder()
        .hitId(resultSet.getLong("learning_hit_id"))
        .fkcoVMatchedTag(resultSet.getString("fkco_v_matched_tag"))
        .fkcoISequence(resultSet.getString("fkco_i_sequence"))
        .fkcoVListFmmId(resultSet.getString("fkco_v_list_fmm_id"))
        .build();
  }
}
