package com.silenteight.customerbridge.gnsrt.generator;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.customerbridge.common.DateConverter;
import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.gnsparty.GnsParty;
import com.silenteight.customerbridge.common.hitdetails.lexer.Lexer;
import com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus;
import com.silenteight.customerbridge.gnsrt.model.request.*;

import java.time.Instant;
import java.util.List;

import static com.google.common.base.Strings.nullToEmpty;

@Builder
class RecordToGnsRequestMapper {

  private final DateConverter hktDateConverter = new DateConverter("Asia/Hong_Kong");
  @NonNull
  AlertRecord record;
  @NonNull
  GnsParty alertedParty;
  @NonNull
  List<AlertDto> alerts;

  GnsRtRecommendationRequest toGnsRtRequest() {
    GnsRtRecommendationRequest request = new GnsRtRecommendationRequest();
    request.setScreenCustomerNameRes(screenCustomerNameRes());
    return request;
  }

  private GnsRtScreenCustomerNameRes screenCustomerNameRes() {
    GnsRtScreenCustomerNameRes customerNameRes = new GnsRtScreenCustomerNameRes();
    customerNameRes.setScreenCustomerNameResPayload(screenCustomerNameResPayload());
    customerNameRes.setHeader(customerNameResHeader());
    return customerNameRes;
  }

  private GnsRtScreenCustomerNameResHeader customerNameResHeader() {
    GnsRtScreenCustomerNameResHeader header = new GnsRtScreenCustomerNameResHeader();
    GnsRtOriginationDetails gnsRtOriginationDetails = new GnsRtOriginationDetails();
    gnsRtOriginationDetails.setTrackingId(record.getBatchId());
    header.setOriginationDetails(gnsRtOriginationDetails);
    return header;
  }

  private GnsRtScreenCustomerNameResPayload screenCustomerNameResPayload() {
    GnsRtScreenCustomerNameResPayload customerNamePayload = new GnsRtScreenCustomerNameResPayload();
    customerNamePayload.setScreenCustomerNameResInfo(customerInfo());
    return customerNamePayload;
  }

  private GnsRtScreenCustomerNameResInfo customerInfo() {
    GnsRtScreenCustomerNameResInfo customerInfo = new GnsRtScreenCustomerNameResInfo();
    customerInfo.setHeader(screenCustomerNameResInfoHeader());
    customerInfo.setScreenableData(screenableData());
    customerInfo.setImmediateResponseData(immediateResponseData());
    return customerInfo;
  }

  private GnsRtScreenCustomerNameResInfoHeader screenCustomerNameResInfoHeader() {
    GnsRtScreenCustomerNameResInfoHeader header = new GnsRtScreenCustomerNameResInfoHeader();
    header.setCountryCode(record.getDbCountry());
    header.setClientType(record.getTypeOfRec());
    header.setUserBankID(record.getDbAccount());
    header.setSourceSystem("GNSRT");
    header.setWatchlistTypeAll("Y");
    return header;
  }

  private ImmediateResponseData immediateResponseData() {
    ImmediateResponseData immediateResponseData = new ImmediateResponseData();
    immediateResponseData.setImmediateResponseTimestamp(getImmediateResponseTimestamp());
    immediateResponseData.setOverAllStatus(GnsRtAlertStatus.POTENTIAL_MATCH);
    alerts.forEach(dto -> immediateResponseData.getAlerts().add(gnsRtAlert(dto)));
    return immediateResponseData;
  }

  private Instant getImmediateResponseTimestamp() {
    try {
      return hktDateConverter.convert(record.getFilteredString())
          .orElseThrow(() ->
              new IllegalStateException(
                  "Missing filtered date, cannot create valid gns-rt request."));
    } catch (RuntimeException ex) {
      throw new IllegalStateException(
          "Invalid format of filtered date, cannot create valid gns-rt request.", ex);
    }
  }

  private static GnsRtAlert gnsRtAlert(AlertDto alertDto) {
    GnsRtAlert gnsRtAlert = new GnsRtAlert();
    gnsRtAlert.setAlertId(alertDto.getSystemId());
    gnsRtAlert.setAlertStatus(GnsRtAlertStatus.POTENTIAL_MATCH);
    gnsRtAlert.setWatchlistType(determineWatchlistType(nullToEmpty(alertDto.getUnit())));

    HitDetailsCollector detailsCollector = new HitDetailsCollector();
    Lexer lexer = new Lexer(detailsCollector);
    lexer.lex(alertDto.getDetails());

    gnsRtAlert.getHitList().addAll(detailsCollector.asGnsRtHits(alertDto.getDetails()));
    return gnsRtAlert;
  }

  private static String determineWatchlistType(String unit) {
    if (unit.contains("DENY"))
      return "Sanctions";
    else if (unit.contains("DUDL"))
      return "DD";
    else if (unit.contains("PEPL"))
      return "PEP";
    else
      return "AM";
  }

  private ScreenableData screenableData() {
    ScreenableData screenableData = new ScreenableData();
    screenableData.setAmlCountry(record.getDbCountry());
    screenableData.setClientType(record.getTypeOfRec());
    screenableData.setSourceSystemIdentifier(alertedParty.getSourceSystemIdentifier());
    screenableData.setCustomerIdentificationNo(alertedParty.getCustomerIdentificationNo());

    alertedParty.getName().ifPresent(screenableData::setFullLegalName);

    alertedParty
        .mapString("dateOfBirthOrRegis", screenableData::setDateOfBirthOrRegistration)
        .mapString("countryOfBirthOrRegist", screenableData::setCountryOfBirthOrRegistration)
        .mapString("dateTimeStamp", screenableData::setDateTimeStamp)
        .mapString("fullLegalName", screenableData::setFullLegalName)
        .mapString("fullTradingName", screenableData::setFullTradingName)
        .mapString("legalParentGroupName", screenableData::setLegalParentOrGroupName)
        .mapString("executiveManagementNames", screenableData::setExecutiveManagementNames)
        .mapString("supplimentaryCardName", screenableData::setSupplementaryCardName)
        .mapString("supplierType", screenableData::setSupplierType)
        .mapString("nameOfAuthority", screenableData::setNameOfAuthority)
        .mapString("nameOfStockExchange", screenableData::setNameOfStockExchange)
        .mapString("estCountryOfHeadOffice", screenableData::setEstablishmentCountryOfHO)
        .mapString("registeredAddOfParentComp", screenableData::setRegisteredAddressOfParentCompany)
        .mapString("businessNature", screenableData::setBusinessNature)
        .mapString("partyType", screenableData::setPartyType)
        .mapString("city", screenableData::setAddressCity)
        .mapString("countryOfEmployment", screenableData::setCountryOfEmployment)
        .mapString("alternateName1", screenableData::setAlternateName1)
        .mapString("alternateName2", screenableData::setAlternateName2)
        .mapString("alternateName3", screenableData::setAlternateName3)
        .mapString("alternateNameRest", screenableData::setAlternateNameRest)
        .mapString("nationalityAll", screenableData::setNationalityAll)
        .mapString("gender", screenableData::setGender)
        .mapString("clientLegalEntityType", screenableData::setClientLegalEntityType)
        .mapString("supplementaryInformation1", screenableData::setSupplementaryInformation1)
        .mapString("supplementaryInformation2", screenableData::setSupplementaryInformation2)
        .mapString("supplementaryInformation3", screenableData::setSupplementaryInformation3)
        .mapString("supplementaryInformation4", screenableData::setSupplementaryInformation4)
        .mapString("supplementaryInformation5", screenableData::setSupplementaryInformation5)
        .mapString("supplementaryInformation6", screenableData::setSupplementaryInformation6)
        .mapString("supplementaryInformation7", screenableData::setSupplementaryInformation7)
        .mapString("supplementaryInformation8", screenableData::setSupplementaryInformation8)
        .mapString("supplementaryInformation9", screenableData::setSupplementaryInformation9)
        .mapString("supplementaryInformation10", screenableData::setSupplementaryInformation10)
        .mapString("identificationType1", screenableData::setIdentificationType1)
        .mapString("identificationNumber1", screenableData::setIdentificationNumber1)
        .mapString("identificationType2", screenableData::setIdentificationType2)
        .mapString("identificationNumber2", screenableData::setIdentificationNumber2)
        .mapString("identificationType3", screenableData::setIdentificationType3)
        .mapString("identificationNumber3", screenableData::setIdentificationNumber3)
        .mapString("identificationType4", screenableData::setIdentificationType4)
        .mapString("identificationNumber4", screenableData::setIdentificationNumber4)
        .mapString("identificationTypeRest", screenableData::setIdentificationTypeRest)
        .mapString("identificationNumberRest", screenableData::setIdentificationNumberRest)
        .mapString("clientSegment", screenableData::setClientSegment)
        .mapString("clientSubSegment", screenableData::setClientSubSegment)
        .mapString("classOfBeneficiary", screenableData::setClassesOfBeneficiary)
        .mapString("customerStatus", screenableData::setCustomerStatus)
        .mapString("staffAccountIdentifier", screenableData::setStaffAccountIdentifier)
        .mapString("pepStatus", screenableData::setPepstatus)
        .mapString("relatedPartyType", screenableData::setRelatedPartyType)
        .mapString("casaFlag", screenableData::setCasaFlag)
        .mapString("changeIndicator", screenableData::setChangeOrPriorityIndicator)
        .mapString("alternateId1Description", screenableData::setAlternateID1Description)
        .mapString("alternateId1", screenableData::setAlternateID1)
        .mapString("alternateId2Description", screenableData::setAlternateID2Description)
        .mapString("alternateId2", screenableData::setAlternateID2)
        .mapString("cddRiskRating", screenableData::setCddriskRating)
        .mapString("rmCodeCddOwner", screenableData::setRmcodeOfCddOwner)
        .mapString("rmLocationCddOwner", screenableData::setRmlocationOfCddOwner)
        .mapString("ownershipStatusOfClient", screenableData::setOwnershipStatusOfClient)
        .mapString("linkedCustomerIdentNo", screenableData::setLinkedCustomerIdentificationNo)
        .mapString("lastCddApprovedDate", screenableData::setLastCddApprovedDate)
        .mapString("otherAddress", screenableData::setOtherAddress)
        .mapString("otherAddressCountry", screenableData::setOtherAddressCountry)
        .mapString("registeredAddOfHeadOffice", screenableData::setRegisteredAddressOfHeadOffice)
        .mapString("operatingOrOfficialAddress", screenableData::setOperatingOrOfficialAddress)
        .mapString("operatingOrOffAddrCntry", screenableData::setOperatingOrOfficialAddressCountry)
        .mapString("mailingOrCommunicationAddr", screenableData::setMailingOrCommunicationAddress)
        .mapString("registOrResidentialAddress", screenableData::setRegisteredOrResidentialAddress)
        .mapString(
            "estCountryOfParentCompany", screenableData::setEstablishmentCountryOfParentCompany)
        .mapString(
            "regAddCntryParCompany", screenableData::setRegisteredAddressCountryOfParentCompany)
        .mapString(
            "accountRelaProfClsdDate", screenableData::setAcctOrRelationshipOrProfileClosedDate)
        .mapString(
            "natureRelaHomeStateAuth",
            screenableData::setNatureOfRelationshipWithHomeStateAuthority)
        .mapString(
            "relationshPrimaryCardholder", screenableData::setRelationshipToThePrimaryCardholder)
        .mapString(
            "registeredAddCntryOfHo", screenableData::setRegisteredAddressCountryOfHeadOffice)
        .mapString(
            "mailingOrCommunAddrCntry", screenableData::setMailingOrCommunicationAddressCountry)
        .mapString(
            "registOrResidenAddCntry", screenableData::setRegisteredOrResidentialAddressCountry);
    return screenableData;
  }
}
