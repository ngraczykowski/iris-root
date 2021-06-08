from company_name.names.token import Token
from company_name.names.tokens_sequence import TokensSequence
from company_name.utils.clear_name import clear_name, divide


def create_tokens(name: str) -> TokensSequence:
    return TokensSequence(
        [
            Token(original=w, cleaned=clear_name(w))
            for w in divide(name)
            if clear_name(w)
        ]
    )
