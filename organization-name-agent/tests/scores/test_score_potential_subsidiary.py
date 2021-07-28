from company_name.compare import compare


def test_potential_subsidiary():
    result = compare("XBS Group", "XBS")
    print(result)
    assert result["potential_subsidiary"].value == 1
    assert result["potential_subsidiary"].compared == (("XBS Group",), ())


def test_not_potential_subsidiary():
    result = compare("NOTARIA DAURA & ROMEO S.C.", "NOTARIA DAURA")
    print(result)
    assert result["potential_subsidiary"].value == 0
    assert result["potential_subsidiary"].compared == ((), ())
