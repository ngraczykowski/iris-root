package com.silenteight.payments.bridge.svb.etl.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.etl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.etl.response.SourceSystem;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@Slf4j
public abstract class AbstractMessageStructure {

  private static final String C_CREDITOR = "C_CREDITOR";
  private final String type;
  private final String apTag;
  private final String messageData;

  public abstract boolean checkMessageWithoutAccountNum();

  public abstract boolean checkMessageFormatF();

  public abstract boolean checkMessageFormatUnstructured();

  public abstract boolean checkMessageFormatUnstructuredLastlineName();

  public abstract boolean checkMessageFormatUnstructuredLastlineEmail();

  public MessageFieldStructure getMessageFieldStructure() {
    if (checkMessageFormatF())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_F;
    else if (checkMessageFormatUnstructured())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED;
    else if (checkMessageFormatUnstructuredLastlineName())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_LASTLINE_NAME;
    else if (checkMessageFormatUnstructuredLastlineEmail())
      return MessageFieldStructure.NAMEADDRESS_FORMAT_LASTLINE_EMAIL;
    else
      return MessageFieldStructure.UNSTRUCTURED;
  }

  public static SourceSystem extractSourceSystem(String sourceSystemId) {
    log.debug("invoked extractSourceSystem: {}", sourceSystemId);

    if (sourceSystemId != null) {
      if (sourceSystemId.startsWith("MTS"))
        return SourceSystem.MTS;
      if (sourceSystemId.startsWith("NBP"))
        return SourceSystem.NBP;
      if (sourceSystemId.startsWith("STAR") || sourceSystemId.startsWith("AMH"))
        return SourceSystem.SCSTAR;
      if (sourceSystemId.startsWith("STS"))
        return SourceSystem.STS;
      if (sourceSystemId.startsWith("DTP"))
        return SourceSystem.DTP;
    }
    return SourceSystem.OTHER;
  }

  public static class MessageStructureDefault extends AbstractMessageStructure {

    public MessageStructureDefault(String type, String apTag, String messageData) {
      super(type, apTag, messageData);
    }

    @Override
    public boolean checkMessageWithoutAccountNum() {
      return false;
    }

    @Override
    public boolean checkMessageFormatF() {
      return false;
    }

    @Override
    public boolean checkMessageFormatUnstructured() {
      return false;
    }

    @Override
    public boolean checkMessageFormatUnstructuredLastlineName() {
      return false;
    }

    @Override
    public boolean checkMessageFormatUnstructuredLastlineEmail() {
      return false;
    }
  }

  public static class MessageStructureNbp extends MessageStructureDefault {

    public MessageStructureNbp(String type, String apTag, String messageData) {
      super(type, apTag, messageData);
    }

    @Override
    public boolean checkMessageWithoutAccountNum() {
      return asList(C_CREDITOR, "UC_CRED").contains(getApTag());
    }

    @Override
    public boolean checkMessageFormatUnstructured() {
      return asList(C_CREDITOR, "UC_CRED").contains(getApTag());
    }
  }

  public static class MessageStructureSts extends MessageStructureDefault {

    public static final String S_CREDIT = "S_CREDIT";
    public static final String S_DEBTOR = "S_DEBTOR";
    public static final String S_UDEBTOR = "S_UDEBTOR";
    public static final String PD_DEBTOR = "PD_DEBTOR";
    public static final String UD_UDEBTOR = "UD_UDEBTOR";

    public MessageStructureSts(String type, String apTag, String messageData) {
      super(type, apTag, messageData);
    }

    @Override
    public boolean checkMessageWithoutAccountNum() {
      return asList(C_CREDITOR, S_CREDIT, S_DEBTOR, S_UDEBTOR, PD_DEBTOR, UD_UDEBTOR).contains(
          getApTag());
    }

    @Override
    public boolean checkMessageFormatUnstructured() {
      return asList(C_CREDITOR, S_CREDIT, S_DEBTOR, UD_UDEBTOR).contains(getApTag());
    }

    @Override
    public boolean checkMessageFormatUnstructuredLastlineName() {
      return Objects.equals(S_UDEBTOR, getApTag());
    }

    @Override
    public boolean checkMessageFormatUnstructuredLastlineEmail() {
      return Objects.equals(PD_DEBTOR, getApTag());
    }
  }

  public static class MessageStructureDtp extends MessageStructureDefault {

    public static final List<String> DTP_PAIRS_SCOPE =
        List.of("C_AGENTCO", "C_CARRIER", "C_OTHBANK", "C_APLICANT", "C_SHIPPING", "C_BENFICRY",
            "C_DRAWEE", "C_DRAWER");

    @Getter
    private final String matchtext;

    @Getter
    private final List<String> mainTagFieldValues;

    @Getter
    private final List<String> nextTagFieldValues;

    public MessageStructureDtp(
        String type, String apTag, String messageData, String matchtext,
        List<String> mainTagFieldValues,
        List<String> nextTagFieldValues) {
      super(type, apTag, messageData);
      this.matchtext = matchtext;
      this.mainTagFieldValues = mainTagFieldValues;
      this.nextTagFieldValues = nextTagFieldValues;
    }

    @Override
    public boolean checkMessageWithoutAccountNum() {
      return DTP_PAIRS_SCOPE.contains(getApTag());
    }

    @Override
    public boolean checkMessageFormatUnstructured() {
      return DTP_PAIRS_SCOPE.contains(getApTag());
    }
  }

  public static class MessageStructureScstar extends MessageStructureDefault {

    public MessageStructureScstar(String type, String apTag, String messageData) {
      super(type, apTag, messageData);
    }

    @Override
    public boolean checkMessageWithoutAccountNum() {
      return "700".equals(getType()) && "50".equals(getApTag());
    }

    @Override
    public boolean checkMessageFormatF() {
      return "103".equals(getType()) && asList("50F", "59F").contains(getApTag());
    }

    @Override
    public boolean checkMessageFormatUnstructured() {
      return ("103".equals(getType()) && asList("50K", "59", "57D").contains(getApTag()))
          || ("700".equals(getType()) && "50".equals(getApTag()));
    }
  }

  public static class MessageStructureMts extends MessageStructureDefault {

    private static final String CHP_502 = "CHP_502";
    private static final String MTS_BBI = "MTS_BBI";
    private static final String MTS_BPI = "MTS_BPI";
    private static final String MTS_OBI = "MTS_OBI";
    private static final String MTS_OPI = "MTS_OPI";
    private static final List<String> MTS_APTAG_SCOPE =
        List.of(CHP_502, MTS_BBI, MTS_BPI, MTS_OBI, MTS_OPI, "SWF_4_59", "SWF_4_59F", "SWF_4_50K",
            "SWF_4_50F");
    @Getter
    private final String apMatchingField;

    public MessageStructureMts(
        String type, String apTag, String messageData, String apMatchingField) {
      super(type, apTag, messageData);
      this.apMatchingField = apMatchingField;
    }

    @Override
    public boolean checkMessageWithoutAccountNum() {
      return asList(MTS_BBI, MTS_BPI, MTS_OBI, MTS_OPI).contains(getApTag());
    }

    @Override
    public boolean checkMessageFormatF() {
      boolean formatFString = extractRegexFormatF(getApMatchingField());
      return asList("SWF_4_50F", "SWF_4_59F").contains(getApTag())
          || asList(CHP_502, MTS_BPI, MTS_OPI).contains(getApTag()) && formatFString;
    }

    private static boolean extractRegexFormatF(String field) {
      String pattern = "^\\s*(\\d/[\\S\\s]+?(\n\\d)[\\S\\s]+?)";
      return field != null && field.matches(pattern);
    }

    @Override
    public boolean checkMessageFormatUnstructured() {
      return MTS_APTAG_SCOPE.contains(getApTag()) && !checkMessageFormatF();
    }
  }
}
