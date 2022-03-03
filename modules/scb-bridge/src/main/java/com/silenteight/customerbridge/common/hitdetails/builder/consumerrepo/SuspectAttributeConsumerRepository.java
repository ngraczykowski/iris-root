package com.silenteight.customerbridge.common.hitdetails.builder.consumerrepo;

import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.customerbridge.common.hitdetails.model.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SuspectAttributeConsumerRepository
    implements ConsumerRepository<Suspect, String> {

  private final MappingConsumerRepository<Suspect, String> mappingRepository =
      new MappingConsumerRepository<>();

  public SuspectAttributeConsumerRepository() {
    mappingRepository.register("OFAC ID", Suspect::setOfacId);
    mappingRepository.register("MATCH", Float::parseFloat, Suspect::setMatch);
    mappingRepository.register("TAG", Tag::parse, Suspect::setTag);
    mappingRepository.register("MATCHINGTEXT", Suspect::setMatchingText);
    mappingRepository.register("BATCH", Suspect::setBatchId);
    mappingRepository.register("NAME", Suspect::setName);
    mappingRepository.register("ADDRESS", Suspect::setAddress);
    mappingRepository.register("CITY", Suspect::setCity);
    mappingRepository.register("COUNTRY", Suspect::setCountry);
    mappingRepository.register("STATE", Suspect::setState);
    mappingRepository.register("ORIGIN", Suspect::setOrigin);
    mappingRepository.register("DESIGNATION", Suspect::setDesignation);
    mappingRepository.register("TYPE", Suspect::setType);
    mappingRepository.register("USER DATA 1", Suspect::setUserData1);
    mappingRepository.register("USER DATA 2", Suspect::setUserData2);
    mappingRepository.register("OFFICIAL REF", Suspect::setOfficialRef);
    mappingRepository.register("PASSPORT", Suspect::setPassport);
    mappingRepository.register("NATID", Suspect::setNationalId);
    mappingRepository.register("PLACE OF BIRTH", Suspect::setBirthPlace);
    mappingRepository.register("DATE OF BIRTH", Suspect::setBirthDate);
    mappingRepository.register("NATIONALITY", Suspect::setNationality);
    mappingRepository.register("ADDITIONAL INFOS", (suspect, additionalInfo) -> {
      suspect.setAdditionalInfo(additionalInfo);
      suspect.setNotes(parseNotes(additionalInfo));
    });
    mappingRepository.register("FML TYPE", Integer::parseInt, Suspect::setFmlType);
    mappingRepository.register("FML PRIORITY", Integer::parseInt, Suspect::setFmlPriority);
    mappingRepository
        .register("FML CONFIDENTIALITY", Integer::parseInt, Suspect::setFmlConfidentiality);
    mappingRepository.register("FML INFO", Suspect::setFmlInfo);
    mappingRepository.register("TYS", Integer::parseInt, Suspect::setTys);
    mappingRepository.register("ISN", Integer::parseInt, Suspect::setIsn);
    mappingRepository.register("PEP-FEP", s -> s.split(" "), (s, a) -> {
      s.setPep(Integer.valueOf(a[0]));
      s.setFep(Integer.valueOf(a[1]));
    });
    mappingRepository.register(
        "SEARCH CODES",
        s -> s.split(" "),
        (s, a) -> forEach(a, s.getSearchCodes()::add));
    mappingRepository.register(
        "BIC CODES",
        s -> s.split(" "),
        (s, a) -> forEach(a, s.getBicCodes()::add));

    mappingRepository.register("index", Integer::parseInt, Suspect::setIndex);
  }

  private static void forEach(String[] texts, Consumer<String> consumer) {
    for (String text : texts) {
      consumer.accept(text);
    }
  }

  private static Map<String, String> parseNotes(String additionalInfo) {
    var notes = new HashMap<String, String>();
    if (additionalInfo == null)
      return notes;

    var info = additionalInfo.split("\\/");
    for (String note : info) {
      var colonIndex = note.indexOf(':');
      if (colonIndex != -1) {
        var key = note.substring(0, colonIndex).trim();
        var value = note.substring(colonIndex + 1).trim();

        if (isNotBlank(key) && isNotBlank(value))
          notes.put(key.toLowerCase(), value);
      }
    }
    return notes;
  }

  @Override
  public Optional<BiConsumer<Suspect, String>> find(String key) {
    return mappingRepository.find(key);
  }
}
