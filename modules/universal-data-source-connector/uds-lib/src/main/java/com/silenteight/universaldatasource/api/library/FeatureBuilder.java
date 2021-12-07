package com.silenteight.universaldatasource.api.library;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput.Builder;
import com.silenteight.datasource.api.allowlist.v1.AllowListFeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.datasource.api.country.v1.CountryFeatureInput;
import com.silenteight.datasource.api.date.v1.DateFeatureInput;
import com.silenteight.datasource.api.date.v1.DateFeatureInput.EntityType;
import com.silenteight.datasource.api.date.v1.DateFeatureInput.SeverityMode;
import com.silenteight.datasource.api.document.v1.DocumentFeatureInput;
import com.silenteight.datasource.api.event.v1.EventFeatureInput;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
import com.silenteight.datasource.api.gender.v1.GenderFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.ModelType;
import com.silenteight.datasource.api.ispep.v2.AlertedPartyItem;
import com.silenteight.datasource.api.ispep.v2.IsPepFeatureInput;
import com.silenteight.datasource.api.ispep.v2.WatchlistItem;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.datasource.api.nationalid.v1.NationalIdFeatureInput;
import com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput;
import com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput.WatchlistType;
import com.silenteight.universaldatasource.api.library.allowlist.v1.AllowListFeatureInputOut;
import com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1.BankIdentificationCodesFeatureInputOut;
import com.silenteight.universaldatasource.api.library.commentinput.v2.StructMapperUtil;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;
import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut;
import com.silenteight.universaldatasource.api.library.document.v1.DocumentFeatureInputOut;
import com.silenteight.universaldatasource.api.library.event.v1.EventFeatureOut;
import com.silenteight.universaldatasource.api.library.freetext.v1.FreeTextFeatureInputOut;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;
import com.silenteight.universaldatasource.api.library.historicaldecisions.v1.HistoricalDecisionsFeatureInputOut;
import com.silenteight.universaldatasource.api.library.ispep.v2.IsPepFeatureInputOut;
import com.silenteight.universaldatasource.api.library.ispep.v2.WatchlistItemOut;
import com.silenteight.universaldatasource.api.library.location.v1.LocationFeatureInputOut;
import com.silenteight.universaldatasource.api.library.name.v1.NameFeatureInputOut;
import com.silenteight.universaldatasource.api.library.nationalid.v1.NationalIdFeatureInputOut;
import com.silenteight.universaldatasource.api.library.transaction.v1.TransactionFeatureInputOut;

import com.google.protobuf.Any;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureBuilder implements FeatureBuilderProvider {

  public static final FeatureBuilder INSTANCE = new FeatureBuilder();

  @Override
  public void build(AllowListFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            AllowListFeatureInput.newBuilder()
                .addAllCharacteristicsValues(input.getCharacteristicsValues())
                .addAllAllowListName(input.getAllowListName())
                .build()
        ));
  }

  @Override
  public void build(BankIdentificationCodesFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            BankIdentificationCodesFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .setAlertedPartyMatchingField(input.getAlertedPartyMatchingField())
                .setWatchlistMatchingText(input.getWatchListMatchingText())
                .setWatchlistType(input.getWatchlistType())
                .addAllWatchlistSearchCodes(input.getWatchlistSearchCodes())
                .addAllWatchlistBicCodes(input.getWatchlistBicCodes())
                .build()
        ));
  }

  @Override
  public void build(CountryFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            CountryFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .addAllAlertedPartyCountries(input.getAlertedPartyCountries())
                .addAllWatchlistCountries(input.getWatchlistCountries())
                .build()
        ));
  }

  @Override
  public void build(DateFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            DateFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .addAllAlertedPartyDates(input.getAlertedPartyDates())
                .addAllWatchlistDates(input.getWatchlistDates())
                .setAlertedPartyType(EntityType.valueOf(input.getAlertedPartyType().name()))
                .setMode(SeverityMode.valueOf(input.getMode().name()))
                .build()
        ));
  }

  @Override
  public void build(DocumentFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            DocumentFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .addAllAlertedPartyDocuments(input.getAlertedPartyDocuments())
                .addAllWatchlistDocuments(input.getWatchlistDocuments())
                .build()
        ));
  }

  @Override
  public void build(EventFeatureOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            EventFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .addAllAlertedPartyDates(input.getAlertedPartyDates())
                .addAllWatchlistEvents(input.getWatchlistEvents())
                .build()
        ));
  }

  @Override
  public void build(FreeTextFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            FreeTextFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .setMatchedName(input.getMatchedName())
                .setMatchedNameSynonym(input.getMatchedNameSynonym())
                .setMatchedType(input.getMatchedType())
                .addAllMatchingTexts(input.getMatchingTexts())
                .setFreetext(input.getFreeText())
                .build()
        ));
  }

  @Override
  public void build(GenderFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            GenderFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .addAllAlertedPartyGenders(input.getAlertedPartyGenders())
                .addAllWatchlistGenders(input.getWatchlistGenders())
                .build()
        ));
  }

  @Override
  public void build(HistoricalDecisionsFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            HistoricalDecisionsFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .setTruePositiveCount(input.getTruePositiveCount())
                .setModelType(ModelType.valueOf(input.getModelType().name()))
                .setReason(StructMapperUtil.toStruct(input.getReason()))
                .build()
        ));
  }

  @Override
  public void build(IsPepFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            IsPepFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .setWatchlistItem(getWatchlistItem(input))
                .setAlertedPartyItem(getAlertedPartyItem(input))
                .build()
        ));
  }

  private static WatchlistItem getWatchlistItem(IsPepFeatureInputOut input) {
    WatchlistItemOut watchlistItem = input.getWatchlistItem();
    return WatchlistItem.newBuilder()
        .setId(watchlistItem.getId())
        .setType(watchlistItem.getType())
        .addAllCountries(watchlistItem.getCountries())
        .setFurtherInformation(watchlistItem.getFurtherInformation())
        .addAllLinkedPepsUids(watchlistItem.getLinkedPepsUids())
        .build();
  }

  private static AlertedPartyItem getAlertedPartyItem(IsPepFeatureInputOut input) {
    return AlertedPartyItem.newBuilder()
        .setCountry(input.getAlertedPartyItem().getCountry())
        .build();
  }

  @Override
  public void build(LocationFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            LocationFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .setAlertedPartyLocation(input.getAlertedPartyLocation())
                .setWatchlistLocation(input.getWatchlistLocation())
                .build()
        ));
  }

  @Override
  public void build(NameFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            NameFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .setAlertedPartyType(
                    NameFeatureInput.EntityType.valueOf(input.getAlertedPartyType().name()))
                .addAllAlertedPartyNames(getAlertedPartyNames(input))
                .addAllWatchlistNames(getWatchlistNames(input))
                .addAllMatchingTexts(input.getMatchingTexts())
                .build()
        ));
  }

  @Override
  public void build(NationalIdFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            NationalIdFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .addAllAlertedPartyDocumentNumbers(input.getAlertedPartyDocumentNumbers())
                .addAllWatchlistDocumentNumbers(input.getWatchlistDocumentNumbers())
                .addAllAlertedPartyCountries(input.getAlertedPartyCountries())
                .addAllWatchlistCountries(input.getWatchlistCountries())
                .build()
        ));
  }

  @Override
  public void build(TransactionFeatureInputOut input, Builder builder) {
    builder
        .setFeature(input.getFeature())
        .setAgentFeatureInput(Any.pack(
            TransactionFeatureInput.newBuilder()
                .setFeature(input.getFeature())
                .addAllTransactionMessages(input.getTransactionMessages())
                .addAllWatchlistNames(getWatchlistNames(input))
                .setWatchlistType(WatchlistType.valueOf(input.getWatchlistType().name()))
                .addAllMatchingTexts(input.getMatchingTexts())
                .build()
        ));
  }

  private static List<com.silenteight.datasource.api.transaction.v1.WatchlistName> getWatchlistNames(
      TransactionFeatureInputOut input) {
    return input.getWatchlistNames().stream()
        .map(watchlistName -> com.silenteight.datasource.api.transaction.v1.WatchlistName
            .newBuilder()
            .setName(watchlistName.getName())
            .setType(com.silenteight.datasource.api.transaction.v1.WatchlistName.NameType.valueOf(
                watchlistName.getType().name()))
            .build())
        .collect(Collectors.toList());
  }

  private static List<WatchlistName> getWatchlistNames(NameFeatureInputOut input) {
    return input.getWatchlistNames().stream()
        .map(watchlistName -> WatchlistName.newBuilder()
            .setName(watchlistName.getName())
            .setType(NameType.valueOf(watchlistName.getType().name()))
            .build())
        .collect(Collectors.toList());
  }

  private static List<AlertedPartyName> getAlertedPartyNames(NameFeatureInputOut input) {
    return input.getAlertedPartyNames().stream()
        .map(alertedPartyName -> AlertedPartyName.newBuilder()
            .setName(alertedPartyName.getName())
            .build())
        .collect(Collectors.toList());
  }
}
