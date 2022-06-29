import pytest

from company_name import CompanyNameAgent


@pytest.mark.parametrize(
    ("ap_names", "mp_names"),
    (
        (("",), ("",)),
        ((), ()),
        (("",), ()),
        ((), ("",)),
    ),
)
def test_empty(ap_names, mp_names):
    print(ap_names, mp_names)
    result = CompanyNameAgent().resolve(ap_names, mp_names)
    assert result.solution == "NO_DATA"


@pytest.mark.parametrize(
    ("ap_names", "mp_names"),
    (
        (("??? ????",), ("google",)),
        (("google",), ("??? ????",)),
        ((".",), ("..",)),
    ),
)
def test_only_symbols(ap_names, mp_names):
    print(ap_names, mp_names)
    result = CompanyNameAgent().resolve(ap_names, mp_names)
    assert result.solution.value in ("INCONCLUSIVE", "MATCH")


@pytest.mark.parametrize(
    ("ap_names", "mp_names"),
    (
        (("a" * 200,), ("b" * 200, "c" * 50)),
        (("a " * 200,), ("b " * 200,)),
    ),
)
def test_long_names(ap_names, mp_names):
    print(ap_names, mp_names)
    result = CompanyNameAgent().resolve(ap_names, mp_names)
    assert result.solution == "INCONCLUSIVE"
