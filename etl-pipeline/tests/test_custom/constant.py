from etl_pipeline.config import pipeline_config

cn = pipeline_config.cn


EXAMPLES = [
    (
        {
            "masterId": "72951854",
            "accountSeq": "1",
            "datasetId": "1044",
            "uniqueCustomerId": "R_US_Active_Address_A05003324172_2020-01-07-07.06.28.836480\n      ",
            "masterVersion": "412740c151535f0756e8dbec7440b726c7a3e135",
            "matchId": "49159509",
            "matchStatus": "O",
            "riskScore": "-1.0",
            cn.MATCH_RECORD_VERSION_ID: "122438658",
            "matchType": "GWL",
            "entityId": "908043",
            "entityVersion": "20150505194929",
            "entity": {
                "id": "908049",
                "version": "20150505194929",
                "name": "Joe Ding",
                "listId": "15991",
                "listCode": "-1003",
                "entityType": "03",
                "createdDate": "10/16/2018",
                "lastUpdateDate": "04/19/2019",
                "source": "CON",
                "dobs": [{"dob": "MAY 6, 1981"}],
                "ids": None,
                "programs": {"program": {"type": "MSSBGWM"}},
                "sdfs": {
                    "sdf": [
                        "N/A",
                        "N/A",
                        "N/A",
                        "N/A",
                        "N/A",
                        "PLEASE CALL AML HOT LINE AT 212 762 8000 AND REF CI-079385",
                        "05/05/2015 19:49:26",
                    ]
                },
                "addresses": {
                    "address": {
                        "address1": "111 STREET 999",
                        "city": "ATLANTA",
                        "postalCode": "11111",
                        "address2": "FL;US",
                    }
                },
            },
            "entityType": "03",
            "entityTextType": "UPID",
            "sourceCode": "CON",
            "stopDescriptors": [
                {
                    "name": "Joe Doe",
                    "totalMatchScore": "0.9230769230769231",
                    "stopDescriptorDetail": {
                        "inputToken": "Doe",
                        "inputSynonym": "null",
                        "sdToken": "Doe",
                        "matchScore": "0.8571428571428572",
                    },
                },
                {
                    "name": "Janusz Tracz Doe",
                    "totalMatchScore": "0.9230769230769231",
                    "stopDescriptorDetail": {
                        "inputToken": "Traczos",
                        "inputSynonym": "null",
                        "sdToken": "Traczos",
                        "matchScore": "0.8571428571428572",
                    },
                },
            ],
            "firstMatchedDate": "01/08/20",
            "lastMatchedDate": "01/08/20",
            "lastReviewDate": "01/08/20",
        },
        {
            "masterId": "72951854",
            "accountSeq": "1",
            "datasetId": "1044",
            "uniqueCustomerId": "R_US_Active_Address_A05003324172_2020-01-07-07.06.28.836480\n      ",
            "masterVersion": "412740c151535f0756e8dbec7440b726c7a3e135",
            "matchId": "49159509",
            "matchStatus": "O",
            "riskScore": "-1.0",
            cn.MATCH_RECORD_VERSION_ID: "122438658",
            "matchType": "GWL",
            "entityId": "908043",
            "entityVersion": "20150505194929",
            "entity": {
                "id": "908049",
                "version": "20150505194929",
                "name": "Joe Ding",
                "listId": "15991",
                "listCode": "-1003",
                "entityType": "03",
                "createdDate": "10/16/2018",
                "lastUpdateDate": "04/19/2019",
                "source": "CON",
                "dobs": [{"dob": "MAY 6, 1981"}],
                "ids": None,
                "programs": {"program": {"type": "MSSBGWM"}},
                "sdfs": {
                    "sdf": [
                        "N/A",
                        "N/A",
                        "N/A",
                        "N/A",
                        "N/A",
                        "PLEASE CALL AML HOT LINE AT 212 762 8000 AND REF CI-079385",
                        "05/05/2015 19:49:26",
                    ]
                },
                "addresses": {
                    "address": {
                        "address1": "111 STREET 999",
                        "city": "ATLANTA",
                        "postalCode": "11111",
                        "address2": "FL;US",
                    }
                },
            },
            "entityType": "03",
            "entityTextType": "UPID",
            "sourceCode": "CON",
            "stopDescriptors": [
                {
                    "name": "Joe Doe",
                    "totalMatchScore": "0.9230769230769231",
                    "stopDescriptorDetail": {
                        "inputToken": "Doe",
                        "inputSynonym": "null",
                        "sdToken": "Doe",
                        "matchScore": "0.8571428571428572",
                    },
                },
                {
                    "name": "Janusz Tracz Doe",
                    "totalMatchScore": "0.9230769230769231",
                    "stopDescriptorDetail": {
                        "inputToken": "Traczos",
                        "inputSynonym": "null",
                        "sdToken": "Traczos",
                        "matchScore": "0.8571428571428572",
                    },
                },
            ],
            "firstMatchedDate": "01/08/20",
            "lastMatchedDate": "01/08/20",
            "lastReviewDate": "01/08/20",
            "SRC_REF_KEY": "R_US_Active_Address_A05003324172_2020-01-07-07.06.28.836480\n      ",
            "VERSION_ID": "122438658",
            "ENTITY_ID": "908043",
            "ENTITY_VERSION": "20150505194929",
            "WL_NAME": "Joe Ding",
            "WL_DOB": ["MAY 6, 1981", "//"],
            "WL_ENTITYTYPE": "03",
            "WL_COUNTRY": None,
            "WL_COUNTRY_NAME": None,
            "WL_NATIONALITY": [""],
            "WL_CITIZENSHIP": [""],
            "WL_POB": [""],
            "WL_ALIASES": [""],
            "WL_ADDRESS1": ["111 STREET 999"],
            "WL_CITY": ["ATLANTA"],
            "WL_POSTALCODE": ["11111"],
            "WL_ADDRESS2": ["FL;US"],
            "WL_MATCHED_TOKENS": '["Doe", "Traczos"]',
            "WL_DOCUMENT_NUMBER": "",
            "WLP_TYPE": "I",
        },
    )
]


EXAMPLE_FOR_TEST_SET_REF_KEY = {
    "entityTextType": "UPIDDDD",
    "AP_TRIGGERS": {"doe": {"ALL_PARTY_NAMES": ["John, Doe Doe"]}, "johnny": {}, "sude": {}},
    "TRIGGERED_BY": [
        "entityTextType",
        "SRC_REF_KEY",
        "masterVersion",
        "entity",
        "matchStatus",
        "sourceCode",
        "matchName",
        "matchType",
        "stopDescriptors",
        "uniqueCustomerId",
    ],
    "SRC_REF_KEY": "R_RUS_Active_Address_2010-02-11-11.11",
    "masterVersion": "asd123123343124141",
    "entity": {
        "addresses": {},
        "aliases": [{"alias": "DONNY JOHN", "type": "Alias"}],
        "citizenships": [],
        "createdDate": "12/09/2010",
        "dobs": [{"dob": "01/11/1924"}],
        "entityType": "02",
        "id": "12321",
        "ids": [],
        "lastUpdateDate": "08/31/2010",
        "listCode": "EUS",
        "listId": "123",
        "name": "DOE DOE JOHN",
        "nationalities": [{"countryCode": "US", "nationality": "CHIC"}],
        "otherIds": [{"childId": "124134314"}],
        "pobs": [],
        "programs": [{"program": "UNITED STATES OF AMERICA", "type": "PPK"}],
        "sdfs": [
            {"name": "EffectiveDate", "sdf": "09/01/2010"},
            {"name": "Legal_Basis", "sdf": "2010/1209 (OJ L333)"},
            {"name": "OriginalID", "sdf": "123143414"},
        ],
        "source": "EUS",
        "titles": [{"title": "PRESIDENT OF THE ACADEMY"}],
        "version": "32131312313213",
    },
    cn.WL_MATCHED_TOKENS: '["doe", "johnny", "sude", "doe"]',
}


RESULT_FOR_EXAMPLE_FOR_TEST_SET_REF_KEY = [
    "entityTextType",
    "AP_TRIGGERS",
    "TRIGGERED_BY",
    "SRC_REF_KEY",
    "masterVersion",
    "entity",
]


EXAMPLE_PARTIES = [
    {
        "accountBU": "US_WM",
        "countryOfIncorporation": "Arabian Emirates",
        "id": "1231231231236",
        "partyCountryOfBirth": "1341412312312",
        "partyId": "1324134123123",
        "partyName": "Shaolin kung fu master",
        "taxId": "1231413412312",
        "taxIdType": "AUS",
    },
    {
        "accountBU": "US_VM_SS",
        "dobDate": "10/10/1969",
        "id": "43134134123123",
        "partyCountryOfBirth": "13413401280",
        "partyId": "31231241234124",
        "partyName": "John, Doe Doe",
        "partyPrimaryCitizenshipCountry": "Arabian Emirates",
        "taxId": "12097381208937",
        "taxIdType": "SSH",
    },
]

EXAMPLE_PARTIES_WITH_NAMES = [
    {
        "accountBU": "US_WM",
        "countryOfIncorporation": "Arabian Emirates",
        "id": "1231231231236",
        "partyCountryOfBirth": "1341412312312",
        "partyId": "1324134123123",
        "partyName": "Shaolin kung fu master",
        "taxId": "1231413412312",
        "taxIdType": "AUS",
        "partyFirstName": "Ultra",
        "partyMiddleName": "Giga",
        "partyLastName": "Pole",
    },
    {
        "accountBU": "US_VM_SS",
        "dobDate": "10/10/1969",
        "id": "43134134123123",
        "partyCountryOfBirth": "13413401280",
        "partyId": "31231241234124",
        "partyName": "John, Doe Doe",
        "partyPrimaryCitizenshipCountry": "Arabian Emirates",
        "taxId": "12097381208937",
        "taxIdType": "SSH",
    },
]
