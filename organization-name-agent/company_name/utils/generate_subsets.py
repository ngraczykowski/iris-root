from typing import Iterator, Sequence


def generate_words_subsets(text: Sequence[str], length: int) -> Iterator[str]:
    text_words_count = len(text)
    for index in range(0, text_words_count - length + 1):
        yield " ".join(text[index : index + length])
