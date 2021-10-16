package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.service.MessageFieldExtractor.MessageNameAddressResult;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.createOneElementList;
import static com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils.processMessage;
import static java.util.Arrays.copyOfRange;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MessageNameAddressResultExtractorHelper {

  /**
   * Simple concatenation based on the hypothesis that words are truncated if hit 35 char-length
   * Based on Swift definition, only applicable for format 50K, 59
   */
  static MessageNameAddressResult extractNameAddressSwift103field50KAnd59(
      String[] nameAddresses) {

    int length = nameAddresses.length;
    int maxLineLength = 35;
    StringBuilder fullNameAddress = new StringBuilder(nameAddresses[0]);

    for (int idx = 0; idx < length; idx++) {
      if (nameAddresses[idx].length() == maxLineLength && idx < length - 1) {
        fullNameAddress.append(nameAddresses[idx + 1]);
      } else if (idx < length - 1) {
        fullNameAddress.append(' ');
        fullNameAddress.append(nameAddresses[idx + 1]);
      }
    }

    String fullNameAddressStr = fullNameAddress.toString();
    String ctryTown =
        nameAddresses[length - 1]; // last line is always "" due to "/n" as the last char

    Pair<String, String> corpnameAddressPair =
        CorpNameRegexExtractionHelper.extractAddrCorpName(fullNameAddressStr);
    String name = corpnameAddressPair.getLeft();
    String address = corpnameAddressPair.getRight();

    if (address == null) {
      address =
          NameAddressRegexExtractionHelper.extractNameAddress(fullNameAddressStr).getAddress();
    }
    if (address == null) {
      if (nameAddresses.length >= 3) {
        address = String.join(" ", copyOfRange(nameAddresses, 2, length)); // lines 3 and 4
      }
    }
    if (name == null) {
      name = nameAddresses[0];
    }
    return MessageNameAddressResult.of(
        createOneElementList(fullNameAddress.toString()),
        createOneElementList(name),
        createOneElementList(address), createOneElementList(ctryTown));
  }

  static MessageNameAddressResult extractNameAddressDtpMessages(
      String fieldValue, String nextFieldValue) {
    String processedMessage = processMessage(fieldValue);
    String[] nameAddressSplitByComma = processedMessage.split(",");
    String fullNameAddressStr = String.join(" ", nameAddressSplitByComma);

    Pair<String, String> corpnameAddressPair =
        CorpNameRegexExtractionHelper.extractAddrCorpName(fullNameAddressStr);
    String name = corpnameAddressPair.getLeft();
    String address = corpnameAddressPair.getRight();
    if (name == null) {
      name = nameAddressSplitByComma[0];
    }
    if (address == null) {
      address = "";
    }

    String[] nextFieldSeparated = nextFieldValue.split("\\n");
    String ctryTown = nextFieldSeparated[nextFieldSeparated.length - 1];
    return MessageNameAddressResult.of(
        createOneElementList(fullNameAddressStr),
        createOneElementList(name),
        createOneElementList(address), createOneElementList(ctryTown));
  }

  /**
   * Simple concatenation based on the hypothesis that words are truncated if hit 35 char-length
   * Based on Swift definition, only applicable for format 50F 59F 1/, 2/, 3/ refer to name,
   * address, country/town respectively
   */
  static MessageNameAddressResult extractNameAddressSwift103Field50FAnd59F(
      String[] nameAddresses) {

    int maxLineLength = 35;
    String unqualifiedText = "";

    Map<String, NameAddressEntry> entityMap = new HashMap<>();
    entityMap.put("1/", new NameAddressEntry());
    entityMap.put("2/", new NameAddressEntry());
    entityMap.put("3/", new NameAddressEntry());

    for (String item : nameAddresses) {
      String entityTypeId = item.substring(0, 2);
      if (!entityMap.containsKey(entityTypeId)) {
        unqualifiedText += item;
        continue;
      }

      NameAddressEntry nameAddressEntry = entityMap.get(entityTypeId);
      nameAddressEntry.tmpNameAddresses.add(item);
      entityMap.put(entityTypeId, nameAddressEntry);
    }

    for (Entry<String, NameAddressEntry> entry : entityMap.entrySet()) {
      NameAddressEntry value = entry.getValue();
      if (value.tmpNameAddresses.isEmpty())
        continue;

      for (int idx = 0; idx < value.tmpNameAddresses.size(); idx++) {
        if (idx == 0) {
          value.value += value.tmpNameAddresses.get(idx).substring(2);
        } else {
          if (value.tmpNameAddresses.get(idx - 1).length() == maxLineLength) {
            value.value += value.tmpNameAddresses.get(idx).substring(2);
          } else {
            value.value += ' ' + value.tmpNameAddresses.get(idx).substring(2);
          }
        }
      }
      entityMap.put(entry.getKey(), value);
    }
    String name = entityMap.get("1/").value;
    String address = entityMap.get("2/").value;
    String ctryTown = entityMap.get("3/").value;
    String nameAddress;
    if (unqualifiedText.isEmpty())
      nameAddress = String.format("%s %s %s", name, address, ctryTown);
    else
      nameAddress = String.format("%s %s %s %s", unqualifiedText.trim(), name, address, ctryTown);

    return MessageNameAddressResult.of(
        createOneElementList(nameAddress), createOneElementList(name),
        createOneElementList(address), createOneElementList(ctryTown));
  }

  static MessageNameAddressResult extractNameAddressFieldLastLineName(
      String[] nameAddresses) {
    String[] nameAddressWithAdjustedOrder = new String[nameAddresses.length];
    nameAddressWithAdjustedOrder[0] = nameAddresses[nameAddresses.length - 1];
    for (int i = 1; i < nameAddresses.length; i++) {
      nameAddressWithAdjustedOrder[i] = nameAddresses[i - 1];
    }
    return extractNameAddressSwift103field50KAnd59(nameAddressWithAdjustedOrder);
  }

  static MessageNameAddressResult extractNameAddressFieldLastLineEmail(
      String[] nameAddresses) {

    List<String> emailValues = new ArrayList<>();
    var checkEmail = false;

    for (String nameAddress : nameAddresses) {
      if (EmailRegexHelper.checkEmail(nameAddress)) {
        checkEmail = true;
        emailValues.add(nameAddress);
        nameAddresses = ArrayUtils.removeElement(nameAddresses, nameAddress);
      }
    }

    if (checkEmail) {
      return getMessageNameAddressResultForEmail(nameAddresses, emailValues);
    }
    return extractNameAddressSwift103field50KAnd59(nameAddresses);
  }

  private static MessageNameAddressResult getMessageNameAddressResultForEmail(
      String[] nameAddresses, List<String> emailValues) {

    MessageNameAddressResult messageNameAddressResult =
        extractNameAddressSwift103field50KAnd59(nameAddresses);
    addUserNamesFromEmails(messageNameAddressResult, emailValues);
    return messageNameAddressResult;
  }

  private static void addUserNamesFromEmails(
      MessageNameAddressResult messageNameAddressResult,
      List<String> emailValues) {

    List<String> names = messageNameAddressResult.getNames();
    ArrayList<String> newNames = new ArrayList<>(names);
    newNames.addAll(extractUserNamesFromEmails(emailValues));
    messageNameAddressResult.setNames(newNames);
  }

  private static List<String> extractUserNamesFromEmails(List<String> emailValues) {
    return emailValues.stream()
        .map(email -> StringUtils.split(email, ' '))
        .filter(splittedValues -> splittedValues.length > 1)
        .map(splittedValues -> splittedValues[0])
        .collect(Collectors.toList());
  }

  @Getter
  private static class NameAddressEntry {

    private final List<String> tmpNameAddresses;
    private String value;

    NameAddressEntry() {
      tmpNameAddresses = new ArrayList<>();
      value = "";
    }
  }
}
