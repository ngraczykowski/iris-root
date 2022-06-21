package com.silenteight.serp.governance.model.transfer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.serp.governance.model.common.ChecksumCalculator;
import com.silenteight.serp.governance.model.common.exception.InvalidChecksumException;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelDto;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ValidationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferredModelChecksumCalculator {

  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;

  public static String calculate(TransferredModelDto dto) {
    String model = JSON_CONVERTER.serializeToString(dto);
    return ChecksumCalculator.calculate(model);
  }

  public static void assertTheSame(@NonNull String checksum, @NonNull TransferredModelDto dto) {
    if(StringUtils.isBlank(checksum)) {
      throw new ValidationException();
    }
    if (!checksum.equals(calculate(dto)))
      throw new InvalidChecksumException("Checksum is different from expected.");
  }
}
