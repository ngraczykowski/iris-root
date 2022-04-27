import unittest
from copy import deepcopy

from etl_pipeline.config import pipeline_config
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from tests.test_json.constant import (
    EXAMPLE_FOR_TEST_SET_REF_KEY,
    EXAMPLE_PARTIES,
    EXAMPLE_PARTIES_WITH_NAMES,
    RESULT_FOR_EXAMPLE_FOR_TEST_SET_REF_KEY,
)

cn = pipeline_config.cn

TEST_PATH = "tests/shared/test_ms_pipeline/"


class TestEngine(unittest.TestCase):
    @classmethod
    def setUpClass(self):
        self.uut = JsonProcessingEngine(pipeline_config)

    def test_set_trigger_reasons(self):
        match = EXAMPLE_FOR_TEST_SET_REF_KEY
        result = self.uut.set_trigger_reasons(match, pipeline_config.config.FUZZINESS_LEVEL)
        self.assertEqual(sorted(result), sorted(RESULT_FOR_EXAMPLE_FOR_TEST_SET_REF_KEY))

    def test_set_beneficiary_hits(self):
        match = EXAMPLE_FOR_TEST_SET_REF_KEY
        assert match.get(cn.IS_BENEFICIARY_HIT, None) is None
        self.uut.set_beneficiary_hits(match)
        assert not match.get(cn.IS_BENEFICIARY_HIT, None)

    def test_connect_full_names(self):
        parties = deepcopy(EXAMPLE_PARTIES)
        self.uut.connect_full_names(parties)
        for party in parties:
            assert party[cn.CONNECTED_FULL_NAME] == ""

        parties = deepcopy(EXAMPLE_PARTIES_WITH_NAMES)
        self.uut.connect_full_names(parties)
        assert parties[0][cn.CONNECTED_FULL_NAME] == "Ultra Giga Pole"
        assert parties[1][cn.CONNECTED_FULL_NAME] == ""

    def test_collect_party_values_from_parties(self):
        payload = {}
        parties = deepcopy(EXAMPLE_PARTIES)
        self.uut.collect_party_values_from_parties(parties, payload)
        assert payload == {
            "ALL_CONNECTED_PARTIES_NAMES": [],
            "ALL_CONNECTED_PARTY_TYPES": [],
            "ALL_CONNECTED_GOVT_IDS": [],
            "ALL_CONNECTED_PARTY_NAMES": ["Shaolin kung fu master", "John, Doe Doe"],
            "ALL_CONNECTED_TAX_IDS": ["1231413412312", "12097381208937"],
            "ALL_CONNECTED_PARTY_DOBS": ["10/10/1969"],
            "ALL_CONNECTED_PARTY_BIRTH_COUNTRIES": ["1341412312312", "13413401280"],
            "ALL_CONNECTED_PARTY_CITIZENSHIP_COUNTRIES": ["Arabian Emirates"],
            "ALL_CONNECTED_PARTY_RESIDENCY_COUNTRIES": [],
            "ALL_CONNECTED_COUNTRY_OF_INCORPORATION": [],
        }

    def test_get_clean_names_from_concat_name(self):
        assert self.uut.get_clean_names_from_concat_name(
            "KA LAI JOSEPH CHAN & KAR LUN KAREN LEE LUNKAREN",
            {
                "PRIN_OWN_NM": "KA LAI JOSEPH CHAN",
                "ORD_PLACR_NM": "KA LAI JOSEPH CHAN",
            },
        ) == {
            "PRIN_OWN_NM": "KA LAI JOSEPH CHAN",
            "concat_residue": " & KAR LUN KAREN LEE LUNKAREN",
        }

        assert self.uut.get_clean_names_from_concat_name(
            "MANULIFE INVEST MANAGEMENT (US) LLC - MD SHORT TERM BOND FUND - SPOT ONLY REPATRIATION MANAGEMENT(US) BONDFUND",
            {
                "LAST_NM": "MANULIFE INVEST MANAGEMENT",
                "FRST_NM": "MANULIFE INVEST MANAGEMENT",
                "PRIN_OWN_NM": "MD SHORT TERM BOND FUND",
            },
        ) == {
            "LAST_NM": "MANULIFE INVEST MANAGEMENT",
            "PRIN_OWN_NM": "MD SHORT TERM BOND FUND",
            "concat_residue": " (US) LLC -  - SPOT ONLY REPATRIATION MANAGEMENT(US) BONDFUND",
        }

        assert self.uut.get_clean_names_from_concat_name(
            "VTB CAPITAL PLC A/C JP MORGAN CHASE BANK NA PLCA/C",
            {" ": ["VTB CAPITAL PLC", "Zoria"]},
        ) == {" ": "VTB CAPITAL PLC", "concat_residue": " A/C JP MORGAN CHASE BANK NA PLCA/C"}

        assert self.uut.get_clean_names_from_concat_name(
            "MSSB C/F SALVATORE P TADDEO JR IRA STANDARD DATED 09/11/97 28 WARREN ST RUMSON NJ 07760",
            {"all_party_names": ["Taddeo, Lisa", "Taddeo, Sal"]},
        ) == {
            "concat_residue": "MSSB C/F SALVATORE P TADDEO JR IRA STANDARD DATED 09/11/97 28 WARREN ST RUMSON NJ 07760"
        }
