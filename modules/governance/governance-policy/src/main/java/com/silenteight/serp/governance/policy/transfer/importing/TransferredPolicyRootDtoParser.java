package com.silenteight.serp.governance.policy.transfer.importing;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.serp.governance.policy.transfer.dto.TransferredPolicyRootDto;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

class TransferredPolicyRootDtoParser {

  TransferredPolicyRootDto parse(InputStream inputStream) {
    try {
      String json = IOUtils.toString(inputStream, UTF_8);
      if (json.isEmpty())
        throw new PolicyImportException("Empty json Step Policy configuration file");

      return parse(json);
    } catch (IOException e) {
      throw new PolicyImportException(e);
    }
  }

  private TransferredPolicyRootDto parse(String json) {
    try {
      return JsonConversionHelper
          .INSTANCE
          .objectMapper()
          .readValue(json, TransferredPolicyRootDto.class);
    } catch (Exception e) {
      throw new PolicyImportException(e);
    }
  }
}
