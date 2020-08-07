import logging
import pytest

from typing import Tuple
from unittest.mock import Mock
from attr import attrs, attrib
from idmismatchagent.agent import search_code_mismatch_agent
from idmismatchagent.api import Result, MatchingTextIsPartOfLongerSequenceReason, Reason, MatchingTextMatchesWlSearchCodeReason, \
    MatchingTextDoesNotMatchWlSearchCodeReason, MatchingTextIsOnlyPartialMatchForSearchCodeReason, \
    NoSearchCodeInWatchlistReason, SearchCodeMismatchAgentInput, MatchingTextDoesNotMatchMatchingFieldReason
from idmismatchagent.comments import get_comment
from utils import LogLevel, generate_logger


@pytest.fixture
def mock_get_comment(monkeypatch):
    from idmismatchagent import agent as agent_module
    mock = Mock()
    monkeypatch.setattr(agent_module, 'get_comment', mock)
    return mock


@attrs(frozen=True)
class Case:
    input: SearchCodeMismatchAgentInput = attrib()
    expected_values: Tuple[Result, Reason] = attrib()


@pytest.mark.usefixtures('mock_get_comment')
@pytest.mark.parametrize("test_case", [
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="WE REFER TO 23190617054158 FOR15,990.00",
            matching_text="190617",
            wl_search_codes="190617",
            wl_type="Individual"
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text='190617',
                    raw_matched_sequence='23190617054158',
                    matching_field='WE REFER TO 23190617054158 FOR15,990.00',
                    partial_match='190617',
                    wl_type="Individual"),
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="WE REFER TO 190617 company",
            matching_text="190617",
            wl_search_codes="190617",
            wl_type="Individual",
        ),
        expected_values=(Result.NO_MATCH, MatchingTextMatchesWlSearchCodeReason(
            matching_text='190617', search_code='190617', wl_type="Individual"
        ))
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field=" ORDER\r\n190617\r\nDRAWEE",
            matching_text="190617",
            wl_search_codes="190617",
            wl_type="Individual",
        ),
        expected_values=(Result.NO_MATCH, MatchingTextMatchesWlSearchCodeReason(
            matching_text='190617', search_code='190617', wl_type="Individual"
        ))
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="WE REFER TO 190617 company",
            matching_text="99999",
            wl_search_codes="99999",
            wl_type="Individual",
        ),
        expected_values=(
                Result.NO_DECISION,
                MatchingTextDoesNotMatchMatchingFieldReason(
                    matching_text='99999',
                    matching_field='WE REFER TO 190617 company',
                )
        )),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="Havana beach house in Cuba",
            matching_text="CUBA",
            wl_search_codes="SearchCodeOfCuba",
            wl_type="Individual",
        ),
        expected_values=
        (
                Result.MATCH,
                MatchingTextIsOnlyPartialMatchForSearchCodeReason(
                    matching_text='CUBA',
                    search_code='SEARCHCODEOFCUBA',
                    wl_type="Individual"
                ),
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="//01233053837\r\nLEOPARD SHIPPING AND LO",
            matching_text="33053",
            wl_search_codes="33053 44030861",
            wl_type="Individual",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text='33053',
                    raw_matched_sequence='01233053837',
                    partial_match='33053',
                    matching_field='//01233053837\r\nLEOPARD SHIPPING AND LO',
                    wl_type="Individual"
                ),
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="sequence match 01233053837 but no search code",
            matching_text="33053",
            wl_type="Individual",
            wl_search_codes="None",
        ),
        expected_values=(Result.NO_DECISION, NoSearchCodeInWatchlistReason())),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="sequence with spaces INV NO 315 345 346 347 ",
            matching_text="5 346 3",
            wl_search_codes="53463",
            wl_type="Individual",
        ),
        expected_values=(
                Result.MATCH, MatchingTextIsPartOfLongerSequenceReason(
                    matching_text='5 346 3',
                    raw_matched_sequence='345 346 347',
                    partial_match='53463',
                    matching_field='sequence with spaces INV NO 315 345 346 347 ',
                    wl_type="Individual"
                ),
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="sequence with dash 022-67091900 IEC NO",
            matching_text="22-670",
            wl_type="Company",
            wl_search_codes="22670",
        ),
        expected_values=
        (
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text='22-670',
                    raw_matched_sequence='02267091900',
                    partial_match='22670',
                    matching_field='sequence with dash 022-67091900 IEC NO',
                    wl_type="Company"
                ),
        )),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="sequence with comma sequence 2,267,095.45 ",
            matching_text="2,267,0",
            wl_type="Company",
            wl_search_codes="22670",
        ),
        expected_values=
        (
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text='2,267,0',
                    raw_matched_sequence='226709545',
                    partial_match='22670',
                    matching_field='sequence with comma sequence 2,267,095.45 ',
                    wl_type="Company"
                ),
        )),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="sequence with slash 1) 3/3 ORIGINALS ",
            matching_text="1) 3/3",
            wl_type="Vessel",
            wl_search_codes="N133JA",
        ),
        expected_values=
        (
                Result.MATCH,
                MatchingTextIsOnlyPartialMatchForSearchCodeReason(
                    matching_text='1) 3/3',
                    search_code='N133JA',
                    wl_type="Vessel"
                ),
        )),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="70",
            matching_field="T +9191773####4 Gift\n",
            matching_text="9191773",
            wl_type="Vessel",
            wl_search_codes="9191773",
        ),
        expected_values=(
                Result.NO_MATCH,
                MatchingTextMatchesWlSearchCodeReason(matching_text="9191773", search_code='9191773', wl_type="Vessel")
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="50K",
            matching_field="/0819974012\nXXXX YYYYY ASIA PTE. LTD.\n50 ... SINGAPORE",
            matching_text="199740",
            wl_type="Individual",
            wl_search_codes="199740",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="199740",
                    raw_matched_sequence="0819974012",
                    matching_field="/0819974012\nXXXX YYYYY ASIA PTE. LTD.\n50 ... SINGAPORE",
                    partial_match="199740",
                    wl_type="Individual"
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="79",
            matching_field="REF 1151697240970001 / 201992000053\n33LK...",
            matching_text="724097",
            wl_type="Individual",
            wl_search_codes="724097",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="724097",
                    matching_field="REF 1151697240970001 / 201992000053\n33LK...",
                    raw_matched_sequence="1151697240970001",
                    partial_match="724097",
                    wl_type="Individual"
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="50K",
            matching_field="/733053806946\nXXXXYYYY\n##/## ### AVE\n...CROYDON VIC3136 AU\n",
            matching_text="33053",
            wl_type="Company",
            wl_search_codes="33053 44030861",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="33053",
                    matching_field="/733053806946\nXXXXYYYY\n##/## ### AVE\n...CROYDON VIC3136 AU\n",
                    raw_matched_sequence="733053806946",
                    partial_match="33053",
                    wl_type="Company",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="72",
            matching_field="/ACC/FFC CUSTOMER IDENTIFICATION\n//NO 72711302\n/REC/INW\n-}	",
            matching_text="11302",
            wl_type="Vessel",
            wl_search_codes="11302",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="11302",
                    matching_field="/ACC/FFC CUSTOMER IDENTIFICATION\n//NO 72711302\n/REC/INW\n-}	",
                    raw_matched_sequence="72711302",
                    partial_match="11302",
                    wl_type="Vessel",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="57",
            matching_field="ICICI ICICI BANK LTD PUNE BLUERIDGE\nHINJEW... INDIA\n",
            matching_text="ICI IC",
            wl_type="Company",
            wl_search_codes="ICIIC",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="ICI IC",
                    matching_field="ICICI ICICI BANK LTD PUNE BLUERIDGE\nHINJEW... INDIA\n",
                    raw_matched_sequence="ICICI ICICI",
                    partial_match="ICIIC",
                    wl_type="Company",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="798",
            message_tag="72",
            matching_field="SCBLEID/ 12107842-1 /\nA--)-)\n-}	",
            matching_text="21078",
            wl_type="Vessel",
            wl_search_codes="EP-IRT\n",
        ),
        expected_values=(
                Result.NO_DECISION,
                MatchingTextDoesNotMatchWlSearchCodeReason(
                    matching_text="21078",
                    search_codes=['EP-IRT'],
                    wl_type="Vessel"
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="700",
            message_tag="45A",
            matching_field="QTY : 297.68 SQMT AT THE RATE OF INR 689.04 PER SQMT\nAMOUNT : INR 205113.43\n.\n2.) 19MM",
            matching_text="113.43",
            wl_type="Vessel",
            wl_search_codes="11343",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="113.43",
                    matching_field="QTY : 297.68 SQMT AT THE RATE OF INR 689.04 PER SQMT\nAMOUNT : INR "
                                   "205113.43\n.\n2.) 19MM",
                    raw_matched_sequence="20511343",
                    partial_match="11343",
                    wl_type="Vessel",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag="50",
            matching_field="0905316530012 \nSAGARMATHA AGRO TRADING P.LTD.\nADARSHNAGAR STREET BIRGUNJ,NEPAL \nPAN NO.PAN NO. 305530947 \n",
            matching_text="5530947",
            wl_type="Company",
            wl_search_codes="5530947",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="5530947",
                    matching_field="0905316530012 \nSAGARMATHA AGRO TRADING P.LTD.\nADARSHNAGAR STREET BIRGUNJ,NEPAL \nPAN NO.PAN NO. 305530947 \n",
                    raw_matched_sequence="305530947",
                    partial_match="5530947",
                    wl_type="Company",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="700",
            message_tag="45A",
            matching_field="TOTAL AMOUNT : INR 425,500.00...",
            matching_text="425,500",
            wl_type="Company",
            wl_search_codes="425500 044030727 044525763 049205798",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text="425,500",
                    matching_field="TOTAL AMOUNT : INR 425,500.00...",
                    raw_matched_sequence="42550000",
                    partial_match="425500",
                    wl_type="Company",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="103",
            message_tag=" 70",
            matching_field="PMT FOR FREIGHT\nBILL NO. 1153,1151,1152,1138,1144,\n1120,1127,1128,1137,1101,1148,1147,\n1146,1146  DATE 15.07.2019\n",
            matching_text=" 1151,1",
            wl_type="Vessel",
            wl_search_codes=" 11511",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text=" 1151,1",
                    matching_field="PMT FOR FREIGHT\nBILL NO. 1153,1151,1152,1138,1144,\n1120,1127,1128,1137,1101,1148,1147,\n1146,1146  DATE 15.07.2019\n",
                    raw_matched_sequence="11531151115211381144",
                    partial_match="11511",
                    wl_type="Vessel",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="700",
            message_tag=" 45A",
            matching_field="EICHER SPARE PARTS\n" +
                           "84219900, 84099941, 84099941, 85123010, 40169330, 84841090,\n",
            matching_text=" 9330, 848",
            wl_type="Vessel",
            wl_search_codes=" 9330848",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text=" 9330, 848",
                    matching_field="EICHER SPARE PARTS\n84219900, 84099941, 84099941, 85123010, 40169330, 84841090,\n",
                    raw_matched_sequence="40169330 84841090",
                    partial_match="9330848",
                    wl_type="Vessel",
                )
        )
    ),
    Case(
        input=SearchCodeMismatchAgentInput(
            message_type="700",
            message_tag=" 45A",
            matching_field="EICHER SPARE PARTS\n" +
                           "TOTAL AMOUNT:INR 942,469.19\n" +
                           "ADD FREIGHT AND INSURANCE:INR 19,239.56\n" +
                           "TOTAL LC VALUE:INR 961,708.75\n" +
                           "TOTAL QUANTITY: 10849 UNITS\n" +
                           "HARMONIC (H.S) CODE:73181500, 87084000, 87089100, 73182100,\n" +
                           "84553000, 87089900, 85122010, 73182990, 84219900, 87089400,\n" +
                           "84219900, 84099941, 84099941, 85123010, 40169330, 84841090,\n",
            matching_text=" 9330, 848",
            wl_type="Vessel",
            wl_search_codes=" 9330848",
        ),
        expected_values=(
                Result.MATCH,
                MatchingTextIsPartOfLongerSequenceReason(
                    matching_text=" 9330, 848",
                    matching_field="EICHER SPARE PARTS\n" +
                                   "TOTAL AMOUNT:INR 942,469.19\n" +
                                   "ADD FREIGHT AND INSURANCE:INR 19,239.56\n" +
                                   "TOTAL LC VALUE:INR 961,708.75\n" +
                                   "TOTAL QUANTITY: 10849 UNITS\n" +
                                   "HARMONIC (H.S) CODE:73181500, 87084000, 87089100, 73182100,\n" +
                                   "84553000, 87089900, 85122010, 73182990, 84219900, 87089400,\n" +
                                   "84219900, 84099941, 84099941, 85123010, 40169330, 84841090,\n",
                    raw_matched_sequence="40169330 84841090",
                    partial_match="9330848",
                    wl_type="Vessel",
                )
        )
    )
])
def test_search_code_mismatch_agent(
        mock_get_comment,
        test_case: Case
):
    expected_result, expected_reason = test_case.expected_values

    # when
    actual_result, actual_comment = search_code_mismatch_agent(test_case.input)
    # then
    assert actual_result == expected_result.value
    mock_get_comment.assert_called_with(expected_reason)

    logger = generate_logger(log_level=LogLevel.debug)
    logger.debug(f"""
          {test_case.input}
          Comment: {get_comment(expected_reason)}
    """)
