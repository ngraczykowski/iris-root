package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.dto.validator.CompleteAlertDefinition;
import com.silenteight.payments.bridge.dto.validator.MinimalAlertDefinition;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertMessageDto implements Serializable {

  private static final long serialVersionUID = 6631585786553870920L;
  @JsonProperty("Unit")
  @NotNull(groups = MinimalAlertDefinition.class)
  String unit; // "MTS-SCB"
  @JsonProperty("BusinessUnit")
  String businessUnit; //"ROOT"
  @JsonProperty("MessageID")
  @NotNull(groups = MinimalAlertDefinition.class)
  String messageId; // "2020082100212372"
  @JsonProperty("SystemID")
  @NotNull(groups = MinimalAlertDefinition.class)
  String systemId; // "USMTS20200909074058-91828-188490"
  @JsonProperty("Bypass")
  String bypass; // "0"
  @JsonProperty("Command")
  String command; // "1"
  @JsonProperty("BusinessType")
  String businessType; // "FTR"
  @JsonProperty("MessageType")
  String messageType; // "31"
  @JsonProperty("IOIndicator")
  String ioIndicator; // "I"
  @JsonProperty("SenderReference")
  String senderReference; // "2020082100212372"
  @JsonProperty("Currency")
  String currency; // "USD"
  @JsonProperty("Amount")
  String amount; // "12225.00"
  @JsonProperty("ApplicationCode")
  @NotNull(groups = CompleteAlertDefinition.class)
  String applicationCode; // "MTS"
  @JsonProperty("ToApplication")
  String toApplication; // ""
  @JsonProperty("MessageFormat")
  String messageFormat; // "ALL"
  @JsonProperty("SenderCode")
  String senderCode; // "####" - masked?
  @JsonProperty("ReceiverCode")
  String receiverCode; // "#############" - masked?
  @JsonProperty("ApplicationPriority")
  String applicationPriority; // "0"
  @JsonProperty("CutoffTime")
  String cutoffTime; // ""
  @JsonProperty("NormalizedAmount")
  String normalizedAmount; // ""
  @JsonProperty("MessageData")
  @NotNull(groups = CompleteAlertDefinition.class)
  String messageData;
  // "[FIRCOSOFT     X] FMT V1.0 GEN CoreEngine 5.6.9.2.p0\n[UNIT          X]
  // MTS-SCB\n[BUSINESS      X] FTR\n[APPLI         X] MTS\n[UUMID         X]
  // MTS I3582426950840312020082100212372\n[TYPE          X] MTS31 INBOUND\n[SENDER         ]
  // ####\n[RECEIVER       ] #############\n[AMOUNT         ] USD 12225.00\n[VALUEDATE     X]
  // 2020\/08\/21\n[REFERENCE     X] 2020082100212372\n[SERVICE       X] ?\n[USERREF       X]
  // ?\n[MTS_UID       X] 2020082100212372\n[MTS_IDB       X] SCB\n[MTS_SRC       X]
  // CHP\n[MTS_TPI       X] 31\n[MTS_BNI        ] RETURN REASON PER CCBNY INTERNAL\nPOLICY.
  // WE DONT PROCESS PYMT REF.\nOGB EBILAEADXXX. NOT AN OFAC ISSUE.\n[MTS_OPD        ]
  // PCBCUS33\n[MTS_OPI        ] CHINA CONSTRUCTION BANK\nNEW YORK BRANCH\nFLOOR 33\nNEW YORK,
  // NY,US\nUS\n[MTS_OPR        ] 2020082100095798\n[MTS_OBD        ] 0000000001\n[MTS_OBI        ]
  // CHINA CONSTRUCTION BANK\nNEW YORK BRANCH\nTRANSIT ACCOUNT\nNY\n[MTS_SBR        ]
  // 200821USI0012001\n[MTS_DPD        ] ####\n[MTS_DPI        ] CHINA CONSTRUCTION BANK\nNEW YORK
  // BRANCH\nFLOOR 33\nNEW YORK,NY,US\nUS\n[MTS_CPD        ] #############\n[MTS_CPI        ]
  // CHIPS FED RETURN OF FUNDS\nC\/O STANDARD CHARTERED BANK\n1095 AVENUE OF THE AMERICAS\nNEW YORK,
  // NEW YORK USA\nUS\n[MTS_CAV        ] LTR\n[MTS_BPI        ] RTN YR PYMT SSN #######\nDD
  // 20200821 USD 12225.00 ORG DR\nZZZZZZZZZ CENTER LLC, ZZZ SHANGHAI\nZZZZZZ INTERNATIONAL TRADING
  // CO.\n[MTS_DHT   01   ] ENQ\n[MTS_DHT   02   ] ENQ\n[XSEP          X]
  // ===================================\n[XTYPE         X] CHP04 OUTBOUND\n[XSENDER        ]
  // ####\n[XRECEIVER      ] ####\n[XAMOUNT        ] USD 12225.00\n[XVALUEDATE    X]
  // 2020\/08\/21\n[XREFERENCE    X] 200821USI0012001\n[XSERVICE      X] ?\n[XUSERREF      X]
  // 2020082100095798\n[CHP_31         ]
  // 042020082114681011614340408423000064022984\n[CHP_221        ] 0256B\n[CHP_260        ]
  // 000001222500\n[CHP_270        ] 000064\n[CHP_320        ] 200821USI0012001\n[CHP_321        ]
  // 2020082100095798\n[CHP_410        ] C200111\n[CHP_417        ]
  // C200111\nD3582426950840\n\n\nSTANDARD CHARTERED BANK\nSAME DAY RETURN OF
  // FUNDS\n[CHP_422        ] RTN YR PYMT SSN #######\nDD 20200821 USD 12225.00 ORG DR\nZZZZZZZZZ
  // CENTER LLC, ZZZ SHANGHAI\nZZZZZZ INTERNATIONAL TRADING CO.\n[CHP_502        ]
  // BPCBCUS33XXX\nCHINA CONSTRUCTION BANK NEW YORK BR\n1095 AVE OF AMERICAS\nNEW YORK,NY
  // 10036\nUNITED STATES OF AMERICA\n[CHP_512        ] D0000000001\nCHINA CONSTRUCTION BANK\nNEW
  // YORK BRANCH\nTRANSIT ACCOUNT\nNY\n[CHP_640        ] RETURN REASON PER CCBNY\nINTERNAL POLICY.
  // WE DONT PROCESS\nPYMT REF. OGB EBILAEADXXX.\nNOT AN OFAC ISSUE.\n"
  @JsonProperty("LastComment")
  String lastComment; // "Regression test for 5.3.20.2"
  @JsonProperty("LastOperator")
  String lastOperator; // "1516837"
  @JsonProperty("ValueDate")
  String valueDate; // "2020\/08\/21"
  @JsonProperty("FilteredDate")
  String filteredDate; // "2020\/09\/09 07:40:59"
  @JsonProperty("RelatedReference")
  String relatedReference; // ""
  @JsonProperty("CreatedDate")
  String createdDate; // "2020\/09\/09 07:40:58"
  @JsonProperty("Priority")
  String priority; // "0"
  @JsonProperty("Confidentiality")
  String confidentiality; // "0"
  @JsonProperty("Blocking")
  String blocking; // "2"
  @JsonProperty("NonBlocking")
  String nonBlocking; // "0"
  @JsonProperty("CopyService")
  String copyService; // ""
  @JsonProperty("Hits")
  @Size(min = 1)
  List<RequestHitDto> hits;
  @JsonProperty("Actions")
  List<RequestActionDto> actions;
  @JsonProperty("CurrentStatus")
  @NotNull(groups = MinimalAlertDefinition.class)
  @Valid
  StatusDto currentStatus;
  @JsonProperty("NextStatuses")
  @Size(min = 1)
  @NotNull(groups = MinimalAlertDefinition.class)
  @Valid
  List<RequestStatusDto> nextStatuses;
}
