package com.silenteight.payments.bridge.svb.newlearning.step.alert;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.silenteight.payments.common.app.OffsetTimeConverter.getOffsetDateTime;

@RequiredArgsConstructor
final class LearningAlertRowMapper implements RowMapper<LearningAlertEntity> {

  private final String timeZone;

  @Override
  public LearningAlertEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    return LearningAlertEntity
        .builder()
        .fileName(rs.getString("file_name"))
        .fkcoId(rs.getLong("fkco_id"))
        .fkcoVSystemId(rs.getString("fkco_v_system_id"))
        .fkcoVFormat(rs.getString("fkco_v_format"))
        .fkcoVType(rs.getString("fkco_v_type"))
        .fkcoVTransactionRef(rs.getString("fkco_v_transaction_ref"))
        .fkcoVRelatedRef(rs.getString("fkco_v_related_ref"))
        .fkcoVSens(rs.getString("fkco_v_sens"))
        .fkcoVBusinessUnit(rs.getString("fkco_v_business_unit"))
        .fkcoVApplication(rs.getString("fkco_v_application"))
        .fkcoVCurrency(rs.getString("fkco_v_currency"))
        .fkcoFAmount(rs.getString("fkco_f_amount"))
        .fkcoVContent(rs.getString("fkco_v_content"))
        .fkcoBHighlightAll(rs.getString("fkco_b_highlight_all"))
        .fkcoVValueDate(rs.getString("fkco_v_value_date"))
        .fkcoUnit(rs.getString("fkco_unit"))
        .fkcoIMsgFmlPriority(rs.getString("fkco_i_msg_fml_priority"))
        .fkcoIMsgFmlConfidentiality(rs.getString("fkco_i_msg_fml_confidentiality"))
        .fkcoDAppDeadline(rs.getString("fkco_d_app_deadline"))
        .fkcoIAppPriority(rs.getString("fkco_i_app_priority"))
        .fkcoINormamount(rs.getString("fkco_i_normamount"))
        .fkcoVMessageid(rs.getString("fkco_v_messageid"))
        .fkcoVCopyService(rs.getString("fkco_v_copy_service"))
        .fkcoDFilteredDateTime(
            getOffsetDateTime(timeZone, rs.getString("fkco_d_filtered_datetime")))
        .fkcoIBlockinghits(rs.getString("fkco_i_blockinghits"))
        .build();
  }
}
