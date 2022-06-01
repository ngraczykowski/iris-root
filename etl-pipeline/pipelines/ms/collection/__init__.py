import logging

from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.datatypes.field import InputRecordField
from etl_pipeline.custom.ms.watchlist_extractor import WatchlistExtractor

cn = pipeline_config.cn

logger = logging.getLogger("main").getChild("collections")


class Collections:
    def __init__(self):
        self.watchlist_extractor = WatchlistExtractor()

    def prepare_wl_values(self, match):
        self.watchlist_extractor.update_match_with_wl_values(match)

    def get_parties(self, payload):
        try:
            alerted_parties = payload[cn.ALERTED_PARTY_FIELD][cn.SUPPLEMENTAL_INFO][
                cn.RELATED_PARTIES
            ][cn.PARTIES]
        except (KeyError, IndexError, TypeError):
            logger.warning("No parties")
            alerted_parties = []
        return alerted_parties

    def flatten_fields(self, fields):
        for num, party in enumerate(fields):
            fields[num] = party[cn.FIELDS]

    def get_accounts(self, payload):
        try:
            accounts = payload[cn.ALERTED_PARTY_FIELD][cn.SUPPLEMENTAL_INFO][cn.RELATED_ACCOUNTS][
                cn.ACCOUNTS
            ]
        except (KeyError, IndexError, TypeError):
            logger.warning("No accounts")
            accounts = []
        return accounts

    def get_alert_supplemental_info(self, payload, field_name):
        return payload.get("alertSupplementalInfo", {}).get(field_name, {})

    def get_xml_field(self, payload, field_name):
        try:
            input_records = payload[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][cn.INPUT_RECORDS]
            fields = input_records[cn.INPUT_FIELD]
            return fields.get(field_name, None)
        except (KeyError, IndexError, TypeError):
            logger.warning("No fields")
            fields = None
        return fields

    def prepare_collections(self, payloads):
        alerted_parties = self.get_parties(payloads)
        accounts = self.get_accounts(payloads)

        if alerted_parties:
            self.flatten_fields(alerted_parties)
        if accounts:
            self.flatten_fields(accounts)

        try:
            input_records = payloads[cn.ALERTED_PARTY_FIELD][cn.INPUT_RECORD_HIST][
                cn.INPUT_RECORDS
            ]
        except (KeyError, IndexError):
            logger.warning("No input_records")
            return {}

        self.prepare_xml_fields(input_records)
        return payloads

    def prepare_xml_fields(self, input_records):
        for input_record in input_records:
            input_record[cn.INPUT_FIELD] = {
                i["name"]: InputRecordField(**i) for i in input_record.get(cn.FIELDS, [])
            }
