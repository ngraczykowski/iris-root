#!/usr/bin/env bash

curl -v \
  -H 'Content-Type: application/json' \
  -d '{
    "Header": null,
    "Body": {
        "msg_SendMessage": {
            "VersionTag": "1",
            "Authentication": {
                "UserLogin": "login",
                "UserPassword": "password",
                "UserRealm": ""
            },
            "Messages": [
                {
                    "Message": {
                        "Unit": "HK",
                        "BusinessUnit": "ROOT",
                        "MessageID": "2020061300000008",
                        "SystemID": "MTH20210106102116-00003-16630-3",
                        "Bypass": "0",
                        "Command": "1",
                        "BusinessType": "MCTSMTHO",
                        "MessageType": "103",
                        "IOIndicator": "O",
                        "SenderReference": "MX020054487902B",
                        "Currency": "AUD",
                        "Amount": "123.00",
                        "ApplicationCode": "STAR0026",
                        "ToApplication": "",
                        "MessageFormat": "SWIFT",
                        "SenderCode": "SCBLSGS0AZPB",
                        "ReceiverCode": "WPACAU2SXXXX",
                        "ApplicationPriority": "0",
                        "CutoffTime": "",
                        "NormalizedAmount": "",
                        "MessageData": "{1:F01SCBLINBBAXXX0000000000}{2:I199BOKLNPKAXXXXN}{3:{108:IR36701908280002}}{4:\r\n:20:IR36701908280002\r\n:50F:IT36701908273410\r\n1/SMITH ANONYM\n2/299, PARK AVENUE\n3/US/NEW YORK, NY 10017\n:79:REFER YOUR MT103\r\nDATED 27/08/2019\r\nVIDE REF XYZJOHNABEXYZ\r\nFOR INR 98940.00\r\nPLS BE ADVISED THAT WE HAVE\r\nEFFECTED PAYMENT AS PER YOUR INSTN.\r\nHOWEVER FUNDS WERE RETURNED BY\r\nBENE BANK REASON BEING\r\nA/C DOES NOT EXIST\r\nWE CONFIRM HAVING CREDITED YOUR\r\nACCOUNT WITH US FOR INR 98645.00\r\nDATED 28/08/2019\r\nBEING REFUND OF THE CAPTIONED\r\nMESSAGE.NET OUR CHARGES\r\nKINDLY SEND US FRESH PYMT INSTN\r\nIF YOU STILL WISH\r\nTO EFFECT THE PYMT WITHOUT QUOTING\r\nPREVIOUS REFERENCES.\r\nOUR REF IT36701908273410\r\nREGARDS\r\nPAYMENTS PROCESSING CENTRE\r\nINDIA\r\nKIND ATTENTION - TO ENSURE PRECISE\r\nPROCESSING OF TRANSACTIONS, PLEASE\r\nPROVIDE CORRECT IFSC CODE OF THE\r\nBENEFICIARY BANK.\r\n-}",
                        "LastComment": "UAT",
                        "LastOperator": "1533636",
                        "ValueDate": "2018\/06\/19",
                        "FilteredDate": "2021\/01\/06 10:21:16",
                        "RelatedReference": "MX020054487902A",
                        "CreatedDate": "2021\/01\/06 10:21:16",
                        "Priority": "40",
                        "Confidentiality": "0",
                        "Blocking": "2",
                        "NonBlocking": "0",
                        "CopyService": "",
                        "Hits": [
                            {
                                "Hit": {
                                    "MatchingText": "PARK",
                                    "Positions": [
                                        {
                                            "Position": {
                                                "PositionStart": "378",
                                                "PositionEnd": "389"
                                            }
                                        }
                                    ],
                                    "Score": "0.0",
                                    "Tag": "50F",
                                    "SolutionType": "1",
                                    "SynonymIndex": "1",
                                    "RulesContext": {
                                        "Type": "1",
                                        "Priority": "40",
                                        "Confidentiality": "0",
                                        "Info": "+SCSTAR PRIORITY SG"
                                    },
                                    "EntityText": "JAY PARK",
                                    "HittedEntity": {
                                        "ID": "TEST000051",
                                        "IsException": "0",
                                        "OfficialReferences": [
                                            {
                                                "OfficialReference": {
                                                    "Name": ""
                                                }
                                            }
                                        ],
                                        "Names": [
                                            {
                                                "Name": "JAY PARK"
                                            },
                                            {
                                                "Name": "JAY PARK ABC"
                                            }
                                        ],
                                        "Addresses": [
                                            {
                                                "Address": {
                                                    "IsMain": "1",
                                                    "PostalAddress": "",
                                                    "Cities": [
                                                    ],
                                                    "States": [
                                                    ],
                                                    "Countries": [
                                                    ]
                                                }
                                            }
                                        ],
                                        "Origin": "UN",
                                        "Designation": "NORTH_KOREA",
                                        "Keywords": [
                                            {
                                                "Keyword": "none"
                                            }
                                        ],
                                        "Type": "I",
                                        "Codes": [
                                            {
                                                "Code": {
                                                    "Name": "BA366700",
                                                    "Type": "Passport"
                                                }
                                            }
                                        ],
                                        "AdditionalInfo": "",
                                        "DatesOfBirth": [
                                        ],
                                        "PlacesOfBirth": [
                                        ],
                                        "Hyperlinks": [
                                            {
                                                "Hyperlink": "none"
                                            }
                                        ],
                                        "UserData1": "",
                                        "UserData2": "",
                                        "Nationality": "",
                                        "IsPEP": "0",
                                        "IsFEP": "0",
                                        "HideOnlyIDs": [
                                            {
                                                "HideOnlyID": ""
                                            }
                                        ],
                                        "HideIDs": [
                                        ],
                                        "HideOrigins": [
                                        ],
                                        "HideInTags": [
                                        ],
                                        "HideUnits": [
                                        ],
                                        "HideSenderReceivers": [
                                        ]
                                    }
                                }
                            }
                        ],
                        "Actions": [
                            {
                                "Action": {
                                    "DateTime": "20210106102116",
                                    "Operator": "Filter Engine",
                                    "Comment": "Stop: 23, Nonblocking: 0",
                                    "Status": {
                                        "ID": "0",
                                        "Name": "FILTER",
                                        "RoutingCode": "0",
                                        "Checksum": "c4b4872d17cf6fa4f27f06e3b5d93c78"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106102117",
                                    "Operator": "Stripping Engine",
                                    "Comment": "Stop: 23, Nonblocking: 0",
                                    "Status": {
                                        "ID": "-2",
                                        "Name": "NO HIT",
                                        "RoutingCode": "-2",
                                        "Checksum": "1080a07f679004254edcc4854e8f66a7"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106103416",
                                    "Operator": "1548822",
                                    "Comment": "1548822 opened this message",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106103453",
                                    "Operator": "1548822",
                                    "Comment": "1548822 opened this message",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106103652",
                                    "Operator": "1548822",
                                    "Comment": "1548822 opened this message",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106104900",
                                    "Operator": "1548822",
                                    "Comment": "1548822 opened this message",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106105017",
                                    "Operator": "1548822",
                                    "Comment": "PTL",
                                    "Status": {
                                        "ID": "214",
                                        "Name": "PEND_PTL_MCH",
                                        "RoutingCode": "12",
                                        "Checksum": "866562c2901fb7664743f8290f5c6958"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106110023",
                                    "Operator": "1518386",
                                    "Comment": "1518386 opened this message in read-only mode",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106110338",
                                    "Operator": "1518386",
                                    "Comment": "1518386 unlocked this message",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106110516",
                                    "Operator": "1518386",
                                    "Comment": "pend",
                                    "Status": {
                                        "ID": "214",
                                        "Name": "PEND_PTL_MCH",
                                        "RoutingCode": "12",
                                        "Checksum": "866562c2901fb7664743f8290f5c6958"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106111525",
                                    "Operator": "1518387",
                                    "Comment": "1518387 opened this message",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210106112640",
                                    "Operator": "1518386",
                                    "Comment": "1518386 opened this message",
                                    "Status": {
                                        "ID": "-1000",
                                        "Name": "N\/A",
                                        "RoutingCode": "-1",
                                        "Checksum": "7163dc6253aa3c4467fdcb2ad60600df"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210107071320",
                                    "Operator": "1159469",
                                    "Comment": "UAT",
                                    "Status": {
                                        "ID": "214",
                                        "Name": "PEND_PTL_MCH",
                                        "RoutingCode": "12",
                                        "Checksum": "866562c2901fb7664743f8290f5c6958"
                                    }
                                }
                            },
                            {
                                "Action": {
                                    "DateTime": "20210107071511",
                                    "Operator": "1533636",
                                    "Comment": "UAT",
                                    "Status": {
                                        "ID": "211",
                                        "Name": "PENDING_012_REVW",
                                        "RoutingCode": "12",
                                        "Checksum": "bb6c8712a888eff9122d0f1709da063d"
                                    }
                                }
                            }
                        ],
                        "CurrentStatus": {
                            "ID": "211",
                            "Name": "PI_FILTER_D",
                            "RoutingCode": "12",
                            "Checksum": "bb6c8712a888eff9122d0f1709da063d"
                        },
                        "NextStatuses": [
                            {
                                "Status": {
                                    "ID": "1",
                                    "Name": "PASSED",
                                    "RoutingCode": "2",
                                    "Checksum": "bc67fdf269178b4bd32ac055fd2738d6"
                                }
                            },
                            {
                                "Status": {
                                    "ID": "2",
                                    "Name": "FAILED",
                                    "RoutingCode": "3",
                                    "Checksum": "bfb1776a1ec2d4a85ed14b2e370f5e61"
                                }
                            },
                            {
                                "Status": {
                                    "ID": "201",
                                    "Name": "CASE_CREATED",
                                    "RoutingCode": "12",
                                    "Checksum": "8117a4d7693553f41cc80089984c6c3b"
                                }
                            },
                            {
                                "Status": {
                                    "ID": "202",
                                    "Name": "FILTER_D",
                                    "RoutingCode": "2",
                                    "Checksum": "152c65fe973360004140a71f25a34cdd"
                                }
                            },
                            {
                                "Status": {
                                    "ID": "206",
                                    "Name": "RECHECK_48HR",
                                    "RoutingCode": "5",
                                    "Checksum": "00bc2a8b05194464704018e5e070aa90"
                                }
                            },
                            {
                                "Status": {
                                    "ID": "266",
                                    "Name": "RECHECK_MAN_FCSU",
                                    "RoutingCode": "5",
                                    "Checksum": "e21251a0e4bf8792768bf715c24bdf26"
                                }
                            }
                        ]
                    }
                }
            ]
        }
    }
}
' \
  http://localhost:24602/rest/pb/alert
