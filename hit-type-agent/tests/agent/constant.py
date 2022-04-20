TEST_CASES = [
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["PARTY1_NAME_FULL"],
                    "country": [
                        "PARTY1_COUNTRY2_CITIZENSHIP",
                        "PARTY1_COUNTRY_OF_INCORPORATION",
                        "PARTY1_COUNTRY_DOMICILE1",
                        "PARTY1_COUNTRY_PEP",
                    ],
                    "job": ["PARTY1_ORGANIZATION_NAME", "PARTY1_EMPLOYER"],
                    "other": ["PARTY1_POSITION_TITLE"],
                },
                "triggered_tokens": {
                    "invest": {
                        "PARTY1_NAME_FULL": ["H BARTON CO-INVEST FUND IV, LLC"],
                        "PARTY1_ORGANIZATION_NAME": ["H BARTON CO-INVEST FUND IV, LLC"],
                    }
                },
            },
            "result": {
                "solution": "NORMAL",
                "hit_categories": ["name"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name", "country"],
                "trigger_categories": {
                    "name": ["PARTY1_NAME_FULL"],
                    "country": [
                        "PARTY1_COUNTRY2_CITIZENSHIP",
                        "PARTY1_COUNTRY_OF_INCORPORATION",
                        "PARTY1_COUNTRY_DOMICILE1",
                        "PARTY1_COUNTRY_PEP",
                    ],
                    "job": ["PARTY1_ORGANIZATION_NAME", "PARTY1_EMPLOYER"],
                    "other": ["PARTY1_POSITION_TITLE"],
                },
                "triggered_tokens": {
                    "invest": {
                        "PARTY1_NAME_FULL": ["H BARTON CO-INVEST FUND IV, LLC"],
                        "PARTY1_ORGANIZATION_NAME": ["H BARTON CO-INVEST FUND IV, LLC"],
                    },
                    "us": {"PARTY1_COUNTRY2_CITIZENSHIP": "USA"},
                },
            },
            "result": {
                "solution": "NORMAL",
                "hit_categories": ["name", "country"],
                "normal_categories": ["name", "country"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["PARTY1_NAME_FULL"],
                    "country": [
                        "PARTY1_COUNTRY2_CITIZENSHIP",
                        "PARTY1_COUNTRY_PEP",
                        "PARTY1_COUNTRY_DOMICILE1",
                        "PARTY1_COUNTRY_OF_INCORPORATION",
                    ],
                    "job": ["PARTY1_ORGANIZATION_NAME", "PARTY1_EMPLOYER"],
                    "other": ["PARTY1_POSITION_TITLE"],
                },
                "triggered_tokens": {
                    "acf": {"PARTY1_NAME_FULL": ["RAGON, CHELSEA RAGON ACF BIANCA"]}
                },
            },
            "result": {
                "solution": "NORMAL",
                "hit_categories": ["name"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["CONCAT_ADDRESS"],
                    "country": ["ADDRESS1_COUNTRY"],
                    "job": [],
                    "other": [],
                },
                "triggered_tokens": {
                    "sherman": {
                        "ADDRESS1_LINE1": ["ROBERT E & SHARON M SHERMAN TTEES"],
                        "ADDRESS1_LINE2": ["ROBERT E & SHARON M SHERMAN"],
                        "CONCAT_ADDRESS": [
                            "ROBERT E & SHARON M SHERMAN TTEES ROBERT E & SHARON M SHERMAN "
                        ],
                    }
                },
            },
            "result": {
                "solution": "NORMAL",
                "hit_categories": ["name"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["PARTY1_NAME_FULL"],
                    "country": [
                        "PARTY1_COUNTRY2_CITIZENSHIP",
                        "PARTY1_COUNTRY_OF_INCORPORATION",
                        "PARTY1_COUNTRY_DOMICILE1",
                        "PARTY1_COUNTRY_PEP",
                    ],
                    "job": ["PARTY1_ORGANIZATION_NAME", "PARTY1_EMPLOYER"],
                    "other": ["PARTY1_POSITION_TITLE"],
                },
                "triggered_tokens": {
                    "invest": {
                        "PARTY1_COUNTRY_OF_INCORPORATION": ["H BARTON CO-INVEST FUND IV, LLC"],
                        "PARTY1_COUNTRY_DOMICILE1": ["H BARTON CO-INVEST FUND IV, LLC"],
                    }
                },
            },
            "result": {
                "solution": "ENTITY_TYPE_MISMATCH",
                "hit_categories": ["country"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["PARTY1_NAME_FULL"],
                    "country": [
                        "PARTY1_COUNTRY2_CITIZENSHIP",
                        "PARTY1_COUNTRY_PEP",
                        "PARTY1_COUNTRY_DOMICILE1",
                        "PARTY1_COUNTRY_OF_INCORPORATION",
                    ],
                    "job": ["PARTY1_ORGANIZATION_NAME", "PARTY1_EMPLOYER"],
                    "other": ["PARTY1_POSITION_TITLE"],
                },
                "triggered_tokens": {
                    "acf": {"PARTY1_EMPLOYER": ["RAGON, CHELSEA RAGON ACF BIANCA"]}
                },
            },
            "result": {
                "solution": "ENTITY_TYPE_MISMATCH",
                "hit_categories": ["job"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["CONCAT_ADDRESS"],
                    "country": ["ADDRESS1_COUNTRY"],
                    "job": [],
                    "other": [],
                },
                "triggered_tokens": {
                    "sherman": {
                        "ADDRESS1_LINE1": ["ROBERT E & SHARON M SHERMAN TTEES"],
                        "ADDRESS1_LINE2": ["ROBERT E & SHARON M SHERMAN"],
                    }
                },
            },
            "result": {
                "solution": "INCONCLUSIVE",
                "hit_categories": ["unknown"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["PARTY1_NAME_FULL"],
                    "country": [
                        "PARTY1_COUNTRY2_CITIZENSHIP",
                        "PARTY1_COUNTRY_OF_INCORPORATION",
                        "PARTY1_COUNTRY_DOMICILE1",
                        "PARTY1_COUNTRY_PEP",
                    ],
                    "job": ["PARTY1_ORGANIZATION_NAME", "PARTY1_EMPLOYER"],
                    "other": ["PARTY1_POSITION_TITLE"],
                },
                "triggered_tokens": {
                    "invest": {
                        "PARTY1_COUNTRY_OF_INCORPORATION": ["H BARTON CO-INVEST FUND IV, LLC"],
                        "PARTY1_COUNTRY_DOMICILE1": ["H BARTON CO-INVEST FUND IV, LLC"],
                    },
                    "somename": {"PARTY1_NAME_FULL": ["somename lcc"]},
                },
            },
            "result": {
                "solution": "SCATTER",
                "hit_categories": ["country", "name"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["PARTY1_NAME_FULL"],
                    "country": [
                        "PARTY1_COUNTRY2_CITIZENSHIP",
                        "PARTY1_COUNTRY_PEP",
                        "PARTY1_COUNTRY_DOMICILE1",
                        "PARTY1_COUNTRY_OF_INCORPORATION",
                    ],
                    "job": ["PARTY1_ORGANIZATION_NAME", "PARTY1_EMPLOYER"],
                    "other": ["PARTY1_POSITION_TITLE"],
                },
                "triggered_tokens": {
                    "acf": {"PARTY1_EMPLOYER": ["RAGON, CHELSEA RAGON ACF BIANCA"]},
                    "somename": {"PARTY1_NAME_FULL": ["somename lcc"]},
                },
            },
            "result": {
                "solution": "SCATTER",
                "hit_categories": ["job", "name"],
                "normal_categories": ["name"],
            },
        }
    ),
    (
        {
            "data": {
                "normal_trigger_categories": ["name"],
                "trigger_categories": {
                    "name": ["CONCAT_ADDRESS"],
                    "country": ["ADDRESS1_COUNTRY"],
                    "job": [],
                    "other": [],
                },
                "triggered_tokens": {
                    "sherman": {
                        "ADDRESS1_LINE1": ["ROBERT E & SHARON M SHERMAN TTEES"],
                        "ADDRESS1_LINE2": ["ROBERT E & SHARON M SHERMAN"],
                    },
                    "somename": {"CONCAT_ADDRESS": ["somename lcc"]},
                },
            },
            "result": {
                "solution": "INCONCLUSIVE",
                "hit_categories": ["unknown", "name"],
                "normal_categories": ["name"],
            },
        }
    ),
]
