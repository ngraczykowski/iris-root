from company_name.knowledge_base import KnowledgeBase
from company_name.names.token import Token
from company_name.names.tokens_sequence import TokensSequence
from company_name.utils.clear_name import clear_name, divide


def _create_token(original_word: str) -> Token:
    cleaned = clear_name(original_word)
    return (
        Token(
            original=original_word,
            cleaned=KnowledgeBase.words_mapping.get(cleaned, cleaned),
        )
        if cleaned
        else None
    )


def create_tokens(name: str) -> TokensSequence:
    return TokensSequence(filter(None, (_create_token(word) for word in divide(name))))
