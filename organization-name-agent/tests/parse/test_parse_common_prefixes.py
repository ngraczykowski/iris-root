import pytest

from company_name.compare import parse_name


@pytest.mark.parametrize(("value", "prefixes"), (("the al", ("the",)),))
def test_parse_common_prefixes(value, prefixes):
    name = parse_name(value)
    print(name)
    assert name.common_prefixes == list(prefixes)
