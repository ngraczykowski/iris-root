package com.silenteight.payments.bridge.agents.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.agents.specific-terms2")
class SpecificTerms2Properties {

  private String bucket = "";
  private String regularTermsKey = "category/regularTerms.csv";
  private String specificTermsKey = "category/specificTerms.csv";

  public static final List<String> REGULAR_TERMS = List.of(
      "([12]/|\n|\\s)(D|S|W|Y)(/|\\s|-)?O([12]/|\n|\\s|\\b)",
      "(?i)([12]/|\n|\\s|,)P.?O.?([12]/|\n|\\s)?BOX([12]/|\n|\\s)",
      "([12]/|\n|\\s|,)P.?([12]/|\n|\\s)?B.([12]/|\n|\\s)?[0-9]{3,9}([12]/|\n|\\s|,)",
      "([12]/|\n|\\s|,)P.([12]/|\n|\\s)?B.([12]/|\n|\\s|,)",
      "([12]/|\n|\\s|,)PO([12]/|\n|\\s)?[0-9]{3,9}([12]/|\n|\\s|,)",
      "([12]/|\n|\\s|,)(PO|BOX)([12]/|\n|\\s)?[0-9]{1}",
      "([12]/|\n|\\s)C(/|\\s)O([12]/|\n|\\s|-)",
      "([12]/|\n|\\s)FORMALLY([12]/|\n|\\s)KNOWN([12]/|\n|\\s)AS([12]/|\n|\\s)",
      "([12]/|\n|\\s)ON([12]/|\n|\\s)BEHALF([12]/|\n|\\s)OF([12]/|\n|\\s)",
      "([12]/|\n|\\s)FOR([12]/|\n|\\s)FURTHER([12]/|\n|\\s)CREDIT([12]/|\n|\\s)",
      "([12]/|\n|\\s)ATT(ENTIO)?N([12]/|\n|\\s)?([12]/|\n|\\s|:)",
      "OWNED([12]/|\n|\\s)BY([12]/|\n|\\s)",
      "OFFICE([12]/|\n|\\s)FROM([12]/|\n|\\s)",
      "(MEMBER|OFFICE)([12]/|\n|\\s)OF([12]/|\n|\\s)",
      "([12]/|\n|\\s|\\b)(TRUST|ESTATE|FORMER?LY|LOUNGE)([12]/|\n|\\s|\\b)",
      "([12]/|\n|\\s)(T/AS?|O/B|EN/OF)([12]/|\n|\\s)"
  );

  public static final List<String> SPECIFIC_TERMS = List.of(
      ".?TREASURY.?OF.?RUSSIA",
      "ADVANCED.?MICRO-FABRICATION.?EQUIPMENT.?INC.?CHINA.?TELECOM",
      "AKBBBY2X",
      "Al.?Amin.?Medical",
      "BANK.?OF.?CHINA.?BELINVEST",
      "BANK.?OF.?RUSSIA",
      "BELARUS.?BANK",
      "BELINVEST",
      "CAFCPEPL",
      "CARACAS",
      "CHINA.?TELECOM",
      "CLOUDMINDS",
      "CNOVRUMM",
      "CPOBMMMY",
      "CUBA",
      "GAZPROM",
      "GAZPRUM",
      "GAZPRUMM",
      "HANGZHOU.?HIKVISION",
      "HIKVISIONAL.?AMIN.?TRUST",
      "HUAWEI",
      "IRAN",
      "ITFKSAJE",
      "JOINT.?STOCK",
      "KBZBMMMY",
      "LUKOIL",
      "Longtail",
      "MNBLGB2L",
      "MUGAAZ22",
      "MYANMAR",
      "Matilock",
      "RCBLCY2I",
      "RIETUMU.?BANKA",
      "RKBZCHZZ",
      "ROSNEFT",
      "RRDBRUM1",
      "RRDBRUMM",
      "RTMBLV2X",
      "RUDVRUMM",
      "RUSSIAN.?BANK",
      "RUSSIAN.?EXPORT",
      "RUSSIAN.?REGIONAL.?DEVELOPMENT.?BANK",
      "Rossiya",
      "SBERBANK",
      "SEMICONDUCTOR.?MANUFACTURING",
      "SMICPERVOMAYSKAYA",
      "Sensebrain",
      "VENEZUELA",
      "VTB",
      "Venezuela",
      "YOMAMMMY");

  private List<String> specificTerms = SPECIFIC_TERMS;
  private List<String> regularTerms = REGULAR_TERMS;

}
