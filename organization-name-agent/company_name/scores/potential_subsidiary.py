from organization_name_knowledge.knowledge_base import KnowledgeBase
from organization_name_knowledge.names.name_information import NameInformation

from company_name.scores.score import Score


def _potential_subsidiary_parent(name: NameInformation) -> bool:
    group_sources = {
        " ".join(source)
        for source, terms in KnowledgeBase.legal_terms.source_to_legal_terms.items()
        if any(t.normalized == "group" for t in terms)
    }
    return group_sources.intersection(name.legal.cleaned_tuple) or any(
        group_name in name.name().cleaned_tuple for group_name in group_sources
    )


def get_potential_subsidiary_score(first: NameInformation, second: NameInformation) -> Score:
    values = _potential_subsidiary_parent(first), _potential_subsidiary_parent(second)
    return Score(
        value=float(any(values)),
        compared=(
            (first.source.original,) if values[0] else (),
            (second.source.original,) if values[1] else (),
        ),
    )
