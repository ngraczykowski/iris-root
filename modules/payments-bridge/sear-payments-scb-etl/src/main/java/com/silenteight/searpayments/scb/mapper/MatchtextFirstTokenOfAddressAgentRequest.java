package com.silenteight.searpayments.scb.mapper;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchtextFirstTokenOfAddressAgentRequest {

    List<String> matchingTexts;
    List<String> addresses;
}
