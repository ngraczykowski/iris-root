import logging
from abc import ABC, abstractmethod
from copy import deepcopy

from silenteight.datasource.api.allowlist.v1.allow_list_pb2 import AllowListFeatureInput
from silenteight.datasource.api.bankidentificationcodes.v1.bank_identification_codes_pb2 import (
    BankIdentificationCodesFeatureInput,
)
from silenteight.datasource.api.country.v1.country_pb2 import CountryFeatureInput
from silenteight.datasource.api.date.v1.date_pb2 import DateFeatureInput
from silenteight.datasource.api.document.v1.document_pb2 import DocumentFeatureInput
from silenteight.datasource.api.event.v1.event_pb2 import EventFeatureInput
from silenteight.datasource.api.freetext.v1.freetext_pb2 import FreeTextFeatureInput
from silenteight.datasource.api.gender.v1.gender_pb2 import GenderFeatureInput
from silenteight.datasource.api.historicaldecisions.v2.historical_decisions_pb2 import (
    AlertedParty,
    Discriminator,
    HistoricalDecisionsFeatureInput,
    ModelKey,
)
from silenteight.datasource.api.hittype.v1.hit_type_pb2 import (
    HitTypeFeatureInput,
    StringList,
    TokensMap,
)
from silenteight.datasource.api.location.v1.location_pb2 import LocationFeatureInput
from silenteight.datasource.api.name.v1.name_pb2 import (
    AlertedPartyName,
    NameFeatureInput,
    WatchlistName,
)
from silenteight.datasource.api.nationalid.v1.national_id_pb2 import NationalIdFeatureInput
from silenteight.datasource.api.transaction.v1.transaction_pb2 import TransactionFeatureInput
from silenteight.datasource.categories.api.v2.category_value_pb2 import CategoryValue

logger = logging.getLogger("main").getChild("producers")


def safe_produce(func):
    def wrap(*args, **kwargs):
        try:
            result = func(*args, **kwargs)
        except Exception as e:
            logger.error(e)
            result = None
        return result

    return wrap


class Producer(ABC):
    def __init__(self, prefix, feature_name, field_maps):
        self.feature_name = f"{prefix}/{feature_name}"
        self.fields = field_maps

    @abstractmethod
    def produce_feature_input(self, match_payload, payload):
        pass

    def produce_fields(self, payload, auxiliary_payload=None):
        fields = deepcopy(self.fields)
        payload = deepcopy(payload)
        auxiliary_payload = deepcopy(auxiliary_payload)
        for input_key, payload_key in self.fields.items():
            if "." in payload_key:
                keys = payload_key.split(".")
                value = auxiliary_payload
                for key in keys:
                    value = value.get(key, {})
                fields[input_key] = value
            else:
                fields[input_key] = payload.get(payload_key, [])
            if isinstance(fields[input_key], list):
                fields[input_key] = [i for i in fields[input_key] if i]
        return fields


class CountryFeatureInputProducer(Producer):
    @safe_produce
    def produce_feature_input(self, payload):
        fields = super().produce_fields(payload)
        return CountryFeatureInput(
            feature=self.feature_name,
            **fields,
        )


class DateFeatureInputProducer(Producer):
    @safe_produce
    def produce_feature_input(self, payload, auxiliary_payload=None):
        fields = super().produce_fields(payload)
        return DateFeatureInput(
            feature=self.feature_name,
            alerted_party_type=DateFeatureInput.EntityType.INDIVIDUAL,
            mode=DateFeatureInput.SeverityMode.NORMAL,
            **fields,
        )


class DocumentFeatureInputProducer(Producer):
    @safe_produce
    def produce_feature_input(self, payload, auxiliary_payload=None):
        fields = super().produce_fields(payload)
        for input_key, payload_key in self.fields.items():
            fields[input_key] = [
                i for i in payload.get(payload_key, []) if i is not None and i != "None"
            ]
        return DocumentFeatureInput(
            feature=self.feature_name,
            **fields,
        )


class HistoricalDecisionsFeatureInputProducer(Producer):
    @safe_produce
    def produce_feature_input(self, payload, auxiliary_payload=None):
        fields = super().produce_fields(payload)
        alerted_party = AlertedParty(id=str(fields["alerted_party"]))
        disc = Discriminator(value=str(self.fields["discriminator"]))

        return HistoricalDecisionsFeatureInput(
            feature=self.feature_name,
            model_key=ModelKey(alerted_party=alerted_party),
            discriminator=disc,
        )


class LocationFeatureInputProducer(Producer):
    @safe_produce
    def produce_feature_input(self, payload, auxiliary_payload=None):
        fields = super().produce_fields(payload, auxiliary_payload)
        for input_key in self.fields:
            fields[input_key] = " ".join([i for i in fields[input_key] if i and i != "None"])

        return LocationFeatureInput(
            feature=self.feature_name,
            **fields,
        )


class NameFeatureInputProducer(Producer):
    AP_TYPE_MAPPING = {
        "I": NameFeatureInput.EntityType.INDIVIDUAL,
        "C": NameFeatureInput.EntityType.ORGANIZATION,
        "UNKNOWN": NameFeatureInput.EntityType.ENTITY_TYPE_UNSPECIFIED,
    }

    @safe_produce
    def produce_feature_input(self, payload, auxiliary_payload=None):
        fields = super().produce_fields(payload, auxiliary_payload)
        alerted_party_type = fields["alerted_party_type"]
        if alerted_party_type == "UNKNOWN":
            alerted_party_type = payload.get("WLP_TYPE")
        return NameFeatureInput(
            feature=self.feature_name,
            alerted_party_type=NameFeatureInputProducer.AP_TYPE_MAPPING.get(
                alerted_party_type, None
            ),
            alerted_party_names=[
                AlertedPartyName(name=str(i)) for i in fields["alerted_party_names"]
            ],
            watchlist_names=[
                WatchlistName(name=str(i), type=WatchlistName.NameType.REGULAR)
                for i in fields["watchlist_names"]
            ],
        )


class CategoryProducer(Producer):
    @safe_produce
    def produce_feature_input(self, payload, alert, match_name, auxiliary_payload=None):
        fields = super().produce_fields(payload, auxiliary_payload)
        return CategoryValue(single_value=fields["type"], alert=alert, match=match_name)


class HitTypeFeatureInputProducer(Producer):
    @safe_produce
    def produce_feature_input(self, payload, auxiliary_payload=None):
        fields = deepcopy(dict(self.fields))
        payload = deepcopy(dict(payload))
        for input_key, payload_key in self.fields.items():
            if input_key == "normal_trigger_categories":
                continue
            fields[input_key] = payload.get(payload_key, [])
        for analyzed_token, map_ in fields["triggered_tokens"].items():

            map_of_tokens = {}
            for found_token, list_of_fields in map_.items():
                tokens = StringList(tokens=list_of_fields)
                map_of_tokens[found_token] = tokens

            fields["triggered_tokens"][analyzed_token] = TokensMap(tokens_map=map_of_tokens)
        fields["trigger_categories"] = deepcopy(dict(fields["trigger_categories"][0]))
        for category in fields["trigger_categories"]:
            fields["trigger_categories"][category] = StringList(
                tokens=list(fields["trigger_categories"][category])
            )
        return HitTypeFeatureInput(feature=self.feature_name, **fields)


# Legacy producers
class TransactionFeatureInputProducer(Producer):
    feature_name = "features/transaction"

    def produce_feature_input(self, payload, auxiliary_payload=None):
        return TransactionFeatureInput(
            feature=self.feature_name,
            transaction_messages=[],
            watchlist_type=TransactionFeatureInput.WatchlistType.WATCHLIST_TYPE_UNSPECIFIED,
            matching_texts=[],
        )


class NationalIdFeatureInputProducer(Producer):
    feature_name = "features/nationalid"

    def produce_feature_input(self, payload, auxiliary_payload=None):
        return NationalIdFeatureInput(
            feature=self.feature_name,
            alerted_party_document_numbers=[
                element for element in payload.get("ap_all_document_numbers_aggregated", [])
            ],
            watchlist_document_numbers=[
                element for element in payload.get("wl_all_document_numbers_aggregated", [])
            ],
            alerted_party_countries=[
                element for element in payload.get("ap_all_countries_aggregated", [])
            ],
            watchlist_countries=[
                element for element in payload.get("wl_all_countries_aggregated", [])
            ],
        )


class GenderFeatureInputProducer(Producer):
    feature_name = "features/gender"

    def produce_feature_input(self, payload, auxiliary_payload=None):
        return GenderFeatureInput(
            feature=self.feature_name,
            alerted_party_genders=[
                element for element in payload.get("ap_all_genders_aggregated", [])
            ],
            watchlist_genders=[
                element for element in payload.get("wl_all_genders_aggregated", [])
            ],
        )


class FreeTextFeatureInputProducer(Producer):
    feature_name = "features/freetext"

    def produce_feature_input(self, payload, auxiliary_payload=None):
        return FreeTextFeatureInput(
            feature=self.feature_name,
            matched_name="",
            matched_name_synonym="",
            matched_type="",
            matching_texts=[],
            freetext="",
        )


class EventFeatureInputProducer(Producer):
    feature_name = "features/event"

    def produce_feature_input(self, payload, auxiliary_payload=None):
        return EventFeatureInput(
            feature=self.feature_name,
            alerted_party_dates=[
                element for element in payload.get("ap_all_dates_aggregated", [])
            ],
            watchlist_events=[element for element in payload.get("wl_all_events_aggregated", [])],
        )


class BankIdentificationCodesFeatureInputProducer(Producer):
    feature_name = "features/bankidentificationcodes"

    def produce_feature_input(self, payload, auxiliary_payload=None):
        return BankIdentificationCodesFeatureInput(
            feature=self.feature_name,
            alerted_party_matching_field=payload.get("ap_all_matching_field_aggregated", ""),
            watchlist_matching_text=payload.get("wl_all_matching_text_aggregated", ""),
            watchlist_type=payload.get("wl_all_type_aggregated", ""),
            watchlist_search_codes=[
                element for element in payload.get("wl_all_search_codes_aggregated", [])
            ],
            watchlist_bic_codes=[
                element for element in payload.get("wl_all_bic_codes_aggregated", [])
            ],
        )


class AllowListFeatureInputProducer(Producer):
    feature_name = "features/allowlist"

    def produce_feature_input(self, payload, auxiliary_payload=None):
        return AllowListFeatureInput(
            feature=self.feature_name,
            characteristics_values=[],
            allow_list_name=[],
        )
