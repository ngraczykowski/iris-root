import pytest
from silenteight.datasource.api.country.v1.country_pb2 import CountryFeatureInput
from silenteight.datasource.api.date.v1.date_pb2 import DateFeatureInput
from silenteight.datasource.api.document.v1.document_pb2 import DocumentFeatureInput
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
from silenteight.datasource.categories.api.v2.category_value_pb2 import CategoryValue

from etl_pipeline.service.agent_router.producers import (
    CategoryProducer,
    CountryFeatureInputProducer,
    DateFeatureInputProducer,
    DocumentFeatureInputProducer,
    HistoricalDecisionsFeatureInputProducer,
    HitTypeFeatureInputProducer,
    LocationFeatureInputProducer,
    NameFeatureInputProducer,
)


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "features",
                "feature_name": "geoResidencies",
                "field_maps": {
                    "alerted_party_countries": "ap_payload_countries",
                    "watchlist_countries": "wl_payload_countries",
                },
            },
            {"ap_payload_countries": ["Moominland"], "wl_payload_countries": ["Tattooine"]},
            {"feature": "features/geoResidencies", "ap": ["Moominland"], "wl": ["Tattooine"]},
        ),
    ],
)
def test_produce_country_feature_input_producer(
    initialization_params, payload_fields, reference_fields
):
    producer = CountryFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    assert result == CountryFeatureInput(
        feature=reference_fields["feature"],
        alerted_party_countries=reference_fields["ap"],
        watchlist_countries=reference_fields["wl"],
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "features",
                "feature_name": "dateOfBirth",
                "field_maps": {
                    "alerted_party_dates": "ap_payload_dobs",
                    "watchlist_dates": "wl_payload_dobs",
                },
            },
            {"ap_payload_dobs": ["MAY 6, 1981"], "wl_payload_dobs": ["April 16, 1777"]},
            {"feature": "features/dateOfBirth", "ap": ["MAY 6, 1981"], "wl": ["April 16, 1777"]},
        ),
    ],
)
def test_produce_date_feature_input_producer(
    initialization_params, payload_fields, reference_fields
):
    producer = DateFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    assert result == DateFeatureInput(
        feature=reference_fields["feature"],
        alerted_party_type=DateFeatureInput.EntityType.INDIVIDUAL,
        mode=DateFeatureInput.SeverityMode.NORMAL,
        alerted_party_dates=reference_fields["ap"],
        watchlist_dates=reference_fields["wl"],
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "features",
                "feature_name": "document",
                "field_maps": {
                    "alerted_party_documents": "ap_payload_documents",
                    "watchlist_documents": "wl_payload_documents",
                },
            },
            {"ap_payload_documents": ["Moominland"], "wl_payload_documents": ["Tattooine"]},
            {"feature": "features/document", "ap": ["Moominland"], "wl": ["Tattooine"]},
        ),
        (
            {
                "prefix": "features",
                "feature_name": "other_document",
                "field_maps": {
                    "alerted_party_documents": "ap_payload_documents",
                    "watchlist_documents": "wl_payload_documents",
                },
            },
            {"ap_payload_documents": ["Trolloland", None, "None", "Moomin"]},
            {"feature": "features/other_document", "ap": ["Trolloland", "Moomin"], "wl": []},
        ),
    ],
)
def test_produce_document_feature_input_producer(
    initialization_params, payload_fields, reference_fields
):
    producer = DocumentFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    assert result == DocumentFeatureInput(
        feature=reference_fields["feature"],
        alerted_party_documents=reference_fields["ap"],
        watchlist_documents=reference_fields["wl"],
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "features",
                "feature_name": "employerName",
                "field_maps": {
                    "alerted_party_names": "ap_payload_employer_names",
                    "alerted_party_type": "ap_type",
                    "watchlist_names": "wl_payload_employer_names",
                },
            },
            {
                "ap_payload_employer_names": ["Tech Ltd.", None, "Bingo"],
                "wl_payload_employer_names": ["Soft Pte."],
                "ap_type": "I",
            },
            {
                "feature": "features/employerName",
                "ap": [
                    AlertedPartyName(name="Tech Ltd."),
                    AlertedPartyName(name="None"),
                    AlertedPartyName(name="Bingo"),
                ],
                "wl": [WatchlistName(name="Soft Pte.", type=WatchlistName.NameType.REGULAR)],
            },
        ),
    ],
)
def test_produce_employer_name_feature_input_producer(
    initialization_params, payload_fields, reference_fields
):
    producer = NameFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    assert result == NameFeatureInput(
        feature=reference_fields["feature"],
        alerted_party_type=NameFeatureInput.EntityType.INDIVIDUAL,
        alerted_party_names=reference_fields["ap"],
        watchlist_names=reference_fields["wl"],
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "features",
                "feature_name": "isApNameTpMarked",
                "field_maps": {
                    "alerted_party": "payload_alerted_party",
                    "discriminator": "payload_discriminator",
                },
            },
            {
                "payload_alerted_party": "Joe",
            },
            {
                "feature": "features/isApNameTpMarked",
                "model_key": ModelKey(alerted_party=AlertedParty(id="Joe")),
            },
        ),
        (
            {
                "prefix": "features",
                "feature_name": "marked_custom_feature",
                "field_maps": {
                    "alerted_party": "payload_alerted_party",
                    "discriminator": "payload_discriminator",
                },
            },
            {
                "payload_alerted_party": "Jimmy",
            },
            {
                "feature": "features/marked_custom_feature",
                "model_key": ModelKey(alerted_party=AlertedParty(id="Jimmy")),
            },
        ),
    ],
)
def test_produce_isApNameTpMarked_feature_input_producer(
    initialization_params, payload_fields, reference_fields
):
    producer = HistoricalDecisionsFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    assert result == HistoricalDecisionsFeatureInput(
        feature=reference_fields["feature"],
        model_key=reference_fields["model_key"],
        discriminator=Discriminator(value=initialization_params["field_maps"]["discriminator"]),
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "features",
                "feature_name": "geoNattionality",
                "field_maps": {
                    "alerted_party_location": "ap_payload_locations",
                    "watchlist_location": "wl_payload_locations",
                },
            },
            {"ap_payload_locations": ["Moominland"], "wl_payload_locations": ["Tattooine"]},
            {"feature": "features/geoNattionality", "ap": "Moominland", "wl": "Tattooine"},
        ),
        (
            {
                "prefix": "features",
                "feature_name": "geoNationality",
                "field_maps": {
                    "alerted_party_location": "ap_payload_locations",
                    "watchlist_location": "wl_payload_locations",
                },
            },
            {"ap_payload_locations": ["None"], "wl_payload_locations": [None]},
            {"feature": "features/geoNationality", "ap": "", "wl": ""},
        ),
        (
            {
                "prefix": "features",
                "feature_name": "geoNaNationality",
                "field_maps": {
                    "alerted_party_location": "ap_payload_locations",
                    "watchlist_location": "wl_payload_locations",
                },
            },
            {
                "ap_payload_locations": ["X", "Y"],
                "wl_payload_locations": ["Tattooine", "San Francisco"],
            },
            {"feature": "features/geoNaNationality", "ap": "X Y", "wl": "Tattooine San Francisco"},
        ),
        (
            {
                "prefix": "features",
                "feature_name": "geoNationality",
                "field_maps": {
                    "alerted_party_location": "ap_payload_locations",
                    "watchlist_location": "wl_payload_locations",
                },
            },
            {
                "ap_payload_locations": ["Paris", None, "London", "", "Madrid", "None", "Rome"],
                "wl_payload_locations": [None, "None", "", "Tattooine", "San Francisco"],
            },
            {
                "feature": "features/geoNationality",
                "ap": "Paris London Madrid Rome",
                "wl": "Tattooine San Francisco",
            },
        ),
    ],
)
def test_produce_location_feature_input_producer(
    initialization_params, payload_fields, reference_fields
):
    producer = LocationFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    assert result == LocationFeatureInput(
        feature=reference_fields["feature"],
        alerted_party_location=reference_fields["ap"],
        watchlist_location=reference_fields["wl"],
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "",
                "feature_name": "",
                "field_maps": {
                    "alerted_party_names": "ap_payload_names",
                    "watchlist_names": "wl_payload_names",
                    "alerted_party_type": "ap_type",
                },
            },
            {
                "ap_payload_names": ["John Smith"],
                "wl_payload_names": ["Smith Joe"],
                "ap_type": "C",
            },
            {
                "feature": "/",
                "ap": [AlertedPartyName(name="John Smith")],
                "wl": [WatchlistName(name="Smith Joe", type=WatchlistName.NameType.REGULAR)],
            },
        ),
        (
            {
                "prefix": "features",
                "feature_name": "name",
                "field_maps": {
                    "alerted_party_names": "ap_payload_names",
                    "watchlist_names": "wl_payload_names",
                    "alerted_party_type": "ap_type",
                },
            },
            {"ap_payload_names": [""], "wl_payload_names": [], "ap_type": "C"},
            {"feature": "features/name", "ap": [AlertedPartyName(name="")], "wl": []},
        ),
        (
            {
                "prefix": "features",
                "feature_name": "last_name",
                "field_maps": {
                    "alerted_party_names": "ap_payload_names",
                    "alerted_party_type": "ap_type",
                    "watchlist_names": "wl_payload_names",
                },
            },
            {"ap_payload_names": [None], "wl_payload_names": ["Jim"], "ap_type": "UNKNOWN"},
            {
                "feature": "features/last_name",
                "ap": [AlertedPartyName(name="None")],
                "wl": [WatchlistName(name="Jim", type=WatchlistName.NameType.REGULAR)],
            },
        ),
    ],
)
def test_produce_name_feature_input_producer(
    initialization_params, payload_fields, reference_fields
):
    producer = NameFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    assert result == NameFeatureInput(
        feature=reference_fields["feature"],
        alerted_party_type=NameFeatureInputProducer.AP_TYPE_MAPPING.get(payload_fields["ap_type"]),
        alerted_party_names=reference_fields["ap"],
        watchlist_names=reference_fields["wl"],
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields", "reference_fields"],
    [
        (
            {
                "prefix": "",
                "feature_name": "",
                "field_maps": {"type": "type_field"},
            },
            ({}, {"type_field": "test name"}, "test_alert", "test_match"),
            {"single_value": "test name", "alert": "test_alert", "match": "test_match"},
        ),
        (
            {
                "prefix": "",
                "feature_name": "",
                "field_maps": {"type": "type_field"},
            },
            ({}, {"type_field": ""}, "None", None),
            {"single_value": "", "alert": "None", "match": ""},
        ),
    ],
)
def test_produce_category_producer(initialization_params, payload_fields, reference_fields):
    producer = CategoryProducer(**initialization_params)
    result = producer.produce_feature_input(*payload_fields)
    assert result == CategoryValue(
        single_value=reference_fields["single_value"],
        alert=reference_fields["alert"],
        match=reference_fields["match"],
    )


@pytest.mark.parametrize(
    ["initialization_params", "payload_fields"],
    [
        (
            {
                "prefix": "",
                "feature_name": "test",
                "field_maps": {
                    "normal_trigger_categories": ["name"],
                    "trigger_categories": "field_with_trigger_categories",
                    "triggered_tokens": "fields_with_triggered_tokens",
                },
            },
            {
                "field_with_trigger_categories": [{"name": ["test"]}],
                "fields_with_triggered_tokens": {"Doe": {"Field": ["Another", "test"]}},
            },
        ),
        (
            {
                "prefix": "",
                "feature_name": "test",
                "field_maps": {
                    "normal_trigger_categories": ["name"],
                    "trigger_categories": "field_with_trigger_categories",
                    "triggered_tokens": "fields_with_triggered_tokens",
                },
            },
            {
                "fields_with_triggered_tokens": {"doe": {}, "johnny": {}, "sude": {}},
                "field_with_trigger_categories": [
                    {"name": [], "country": ["ADDRESS1_COUNTRY"], "job": [], "other": []}
                ],
            },
        ),
        (
            {
                "prefix": "",
                "feature_name": "test",
                "field_maps": {
                    "normal_trigger_categories": ["name", "country"],
                    "trigger_categories": "field_with_trigger_categories",
                    "triggered_tokens": "fields_with_triggered_tokens",
                },
            },
            {
                "fields_with_triggered_tokens": {"doe": {}, "johnny": {}, "sude": {}},
                "field_with_trigger_categories": [
                    {"name": [], "country": ["ADDRESS1_COUNTRY"], "job": [], "other": []}
                ],
            },
        ),
    ],
)
def test_produce_hit_type_input(initialization_params, payload_fields):
    producer = HitTypeFeatureInputProducer(**initialization_params)
    result = producer.produce_feature_input(payload_fields)
    reference_dict = {}
    for type_ in payload_fields["field_with_trigger_categories"][0]:
        reference_dict[type_] = StringList(
            tokens=payload_fields["field_with_trigger_categories"][0][type_]
        )

    for analyzed_token in payload_fields["fields_with_triggered_tokens"]:

        map_of_tokens = {}
        for found_token, list_of_fields in payload_fields["fields_with_triggered_tokens"][
            analyzed_token
        ].items():
            tokens = StringList(tokens=list_of_fields)
            map_of_tokens[found_token] = tokens

        payload_fields["fields_with_triggered_tokens"][analyzed_token] = TokensMap(
            tokens_map=map_of_tokens
        )
    expected_result = HitTypeFeatureInput(
        feature=f"/{initialization_params['feature_name']}",
        normal_trigger_categories=initialization_params["field_maps"]["normal_trigger_categories"],
        trigger_categories=reference_dict,
        triggered_tokens=payload_fields["fields_with_triggered_tokens"],
    )
    assert result == expected_result
