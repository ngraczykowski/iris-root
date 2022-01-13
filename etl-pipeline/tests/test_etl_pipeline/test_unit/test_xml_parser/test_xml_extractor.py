import lxml.etree
import pytest

from etl_pipeline.xml_parser.xml_pipeline import XMLExtractor


@pytest.mark.parametrize(
    ("string", "expected_result"), [("true", True), ("false", False), ("null", None)]
)
def test_convert_text_to_boolean(string, expected_result):
    uut = XMLExtractor()
    assert uut._convert_text_to_boolean(string) == expected_result


@pytest.mark.parametrize(
    ("string", "expected_result"),
    [
        ("true", True),
        ("True", True),
        ("false", False),
        ("False", False),
        ("fals", None),
        ("tru", None),
        ("null", None),
        ("", None),
        (1, True),
        (0, False),
    ],
)
def test_extract_boolean_value(string, expected_result):
    uut = XMLExtractor()
    xml = lxml.etree.fromstring(f'<alert><elem name="alertId">{string}</elem></alert>')
    assert uut.extract_boolean_value(xml, 'elem[@name="alertId"]') == expected_result


def test_extract_string_elements_array_one_if_doc_is_none():
    uut = XMLExtractor()
    assert uut.extract_string_elements_array(None, 'elem[@name="alertId"]') == []


@pytest.mark.parametrize(
    ("string"),
    [("janusz")],
)
def test_extract_value(string):
    uut = XMLExtractor()
    xml = lxml.etree.fromstring(f'<alert><elem name="alertId">{string}</elem></alert>')
    assert uut.extract_value(xml, 'elem[@name="alertId"]') == string


@pytest.mark.parametrize(
    ("string", "expected_result"),
    [("janusz", None)],
)
def test_do_not_extract_value_if_there_are_many_values(string, expected_result):
    uut = XMLExtractor()
    xml = lxml.etree.fromstring(
        f'<alert><elem name="alertId">{string}</elem><elem name="alertId">{string}</elem></alert>'
    )
    assert uut.extract_value(xml, 'elem[@name="alertId"]') == expected_result


@pytest.mark.parametrize(
    ("string1", "string2", "expected_result"),
    [("False", "true", [False, True]), ("tru", "true", [None, True])],
)
def test_extract_boolean_array(string1, string2, expected_result):
    uut = XMLExtractor()
    xml = lxml.etree.fromstring(
        f'<alert><elem name="alertId">{string1}</elem><elem name="alertId">{string2}</elem></alert>'
    )
    assert uut.extract_boolean_array(xml, 'elem[@name="alertId"]') == expected_result


@pytest.mark.parametrize(
    ("string1", "string2", "expected_result"),
    [("False", "true", ["False", "true"]), ("tru", "true", ["tru", "true"])],
)
def test_extract_string_elements_array(string1, string2, expected_result):
    uut = XMLExtractor()
    xml = lxml.etree.fromstring(
        f'<alert><elem name="alertId">{string1}</elem><elem name="alertId">{string2}</elem></alert>'
    )
    assert uut.extract_string_elements_array(xml, 'elem[@name="alertId"]') == expected_result


def test_if_there_is_no_element_of_given_type_then_return_empty_list():
    string1 = "test"
    string2 = "test2"
    uut = XMLExtractor()
    xml = lxml.etree.fromstring(
        f'<alert><elem name="alertId">{string1}</elem><elem name="alertId">{string2}</elem></alert>'
    )
    assert uut.extract_string_elements_array(xml, 'elem[@name="alertIdNone"]') == []


def test_if_there_is_text_in_xpath_then_raise_value_error():
    uut = XMLExtractor()
    xml = lxml.etree.fromstring('<alert><elem name="alertId">text</elem></alert>')
    with pytest.raises(ValueError):
        assert uut.extract_string_elements_array(xml, 'elem[@name="alertId"]//text()') == []
