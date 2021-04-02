import collections
import itertools
import statistics
from typing import Mapping, Sequence, Set, Tuple

from unidecode import unidecode

KNOWN_LEGAL_ENTITIES = {
    "": ("&", "e", "y", "und", "and"),
    "Akciová společnost": ("a.s.", "akc. spol."),
    "Aksjeselskap": ("AS", ),
    "aktiebolag": ("ab",),
    "aktiengesellschaft": ("ag",),
    "Aktieselskab": ("A/S",),
    "Aktsionernoye obschestvo": ("AO", ),
    "Allmennaksjeselskap": ("ASA", ),
    "Andelsselskab med begrænset ansvar": ("A.M.B.A.",),
    "anonim şirketi": ("anonim şirket", ),
    "Anpartsselskab": ("ApS",),
    "Ansvarlig selskap": ("ANS", ),
    "berhad": ("bhd",),
    "besloten vennootschap": ("bv",),
    "Borettslag": ("BL", ),
    "charitable incorporated organisation": ("cio",),
    "close corporation": ("c.c.", ),
    "commanditaire vennootschap": ("c.v.",),
    "community interest company": ("cic",),
    "Company": ("co.",),
    "cooperative": ("družstvo", ),
    "copropiedad": (),
    "Corporation": ("corp.",),
    "de capital variable": ("de c.v.", "capital variable"),
    "deoničarsko društvo": ("deoničko društvo", "akcionarsko društvo", "d.d.", "a.d."),
    "drujestvo s ogranichena otgovornost": ("ood", ),
    "društvo sa ograničenom odgovornošću":  ("друштво са ограниченом одговорношћу", "d.o.o.", "д.о.о."),
    "ednolichno druzhestvo s ogranichena otgovornost": ("e.o.o.d.", ),
    "eingetragenes Einzelunternehmen": ("e.U.",),
    "ekonomisk forening": ("ek",),
    "Empresa Individual de Responsabilidad Limitada": ("e.i.r.l.", ),
    "Enkeltmandsvirksomhed": (),
    "Enkeltpersonforetak": (),
    "Erhvervsdrivende fond": (),
    "Forening med begrænset ansvar": ("F.M.B.A.",),
    "Forening": (),
    "free zone company": ("fzco", "fzc", "fz-llc", "fz llc", "fz company", "fz co"),
    "free zone establishment": ("fze",),
    "Fylkeskommunalt foretak": ("FKF", ),
    "General Partnership": ("GP",),
    "Genossenschaft": ("gen", ),
    "Gensidigt selskab": ("G/S",),
    "gesellschaft mit beschrankter haftung": ("gmbh", "gesellschaft m.b.h.", "ges.m.b.h.",),
    "government owned enterprise": ("goe",),
    "handelsbolag": ("hb",),
    "helseforetak": ("HF", ),
    "incorporated limited partnership": ("ilp",),
    "Incorporated": ("inc.", "incorporation"),
    "Interessentskab": ("I/S",),
    "Iværksætterselskab": ("IVS",),
    "javno preduzeće": ("j.p.", ),
    "joint stock company": ("jsc", ),
    "julkinen osakeyhtiö": ("Oyj", ),
    "kabushiki gaisha": ("k.k.",),
    "komanditno društvo": ("k.d.", ),
    "komanditní společnost": ("k.s.", "kom. spol."),
    "kommanditbolag": ("kb",),
    "Kommanditgesellschaft": ("KG", ),
    "Kommanditselskab": ("K/S",),
    "korlátolt felelősségű társaság": ("kft.", ),
    "közhasznú társaság": ("kht.", ),
    "limited liability company": ("limited", "ltd", "lc", "l.l.c.", "ltda", "limitada", "lda"),
    "limited liability limited partnership": ("lllp",),
    "limited liability partnership": ("llp",),
    "Limited Partnership": ("LP",),
    "limited şirketi": ("limited şirket", ),
    "Mažoji bendrija": ("mb", ),
    "naamloze vennootschap": ("nv",),
    "National association": ("NA", ),
    "nyilvánosan működő részvénytársaság": ("Nyrt.", ),
    "obecně prospěšná společnost": ("o.p.s.",),
    "obschestvo s ogranichennoy otvetstvennostyu": ("ooo",),
    "Občanské sdružení": ("o.s.",),
    "open joint stock company": ("ojsc",),
    "ortačko društvo": ("o.d.", ),
    "osakeyhtiö": ("oy", ),
    "Osaühing": ("OÜ", ),
    "osuuskunta": ("osk", ),
    "otkrytoye aktsionernoye obschestvo": ("oao",),
    "Partnerselskab": ("Kommanditaktieselskab", "P/S"),
    "Partsrederi": (),
    "preduzetnik": (),
    "Private Limited Company": ("Pte Ltd",),
    "Private": ("pte", "pvt"),
    "professional corporation": ("pc",),
    "professional limited liability company": ("pplc",),
    "przedsiębiorstwo państwowe": ("p.p.", ),
    "Public Establishment with Economic characteristics": ("PEEC",),
    "public joint stock company": ("pjsc",),
    "Public Limited Company": ("Plc Ltd", "plc"),
    "publichnoye aktsionernoye obschestvo": ("pao",),
    "příspěvková organizace": (),
    "representative trade office": ("rto", ),
    "Sabiedrība ar ierobežotu atbildību": ("sia", ),
    "Selskab med begrænset ansvar": ("S.M.B.A.", "BA", ),
    "Selskap med delt ansvar": ("DA", ),
    "sendirian berhad": ("sdn. bhd.",),
    "Sharikat al-Mossahamah": ("sae", ),
    "Shoqëri me përgjegjësi të kufizuar": ("Sh.p.k.", ),
    "Single Member Private Limited Company": ("SM Pte Ltd",),
    "Sociedad Anónima Cerrada": ("S.A.C.", ),
    "sociedad colectiva": ("sc",),
    "sociedad de capital e industria": ("sci",),
    "sociedad de responsabilidad limitada": ("srl", "Societate cu răspundere limitată", "Societatea cu răspundere limitată", "s. de r.l."),
    "sociedad en nombre colectivo": ("Cia.", ),
    "sociedad limitada nueva empresa": ("S.L.N.E.", ),
    "sociedad limitada": ("s.l.", ),
    "sociedad por acciones simplificada": ("sas",),
    "Sociedad por acciones": ("SpA", ),
    "Sociedade Gestora de Participações Sociais": ("SGPS", ),
    "societe a responsabilite limitee": ("sarl", "s.a r.l."),
    "societe anonyme": ("sociedad anonima", "s.a.", "s/a"),
    "Sole Proprietorship": ("SP",),
    "Společnost s ručením omezeným": ("s.r.o.", "spol. s r.o."),
    "spółka jawna": ("sp.j.", ),
    "spółka komandytowa": ("sp.k.", ),
    "spółka partnerska": ("sp.k.", ),
    "spółka z ograniczoną odpowiedzialnością": ("Sp. z o.o.",),
    "srltda": (),
    "State Company": ("plc",),
    "State Joint Venture Company": ("plc",),
    "state owned asset": ("soa", ),
    "state owned enterprise": ("soe", ),
    "státní podnik": ("s.p.",),
    "Tovarystvo z obmezhenoiu vidpovidalnistiu": ("T.O.V.", ),
    "unlimited proprietary": ("pty",),
    "Uždaroji akcinė bnendrovė": ("UAB", ),
    "veřejná obchodní společnost": ("v.o.s", "veř. obch. spol.", "a. spol."),
    "Vidkryte aktsionerne tovarystvo)": ("VAT", ),
    "zapsaný spolek": ("z.s.",),
    "zártkörűen működő részvénytársaság": ("Zrt.", ),
    "živnost": (),
}


def _prepare_dict_from_list_with_duplicates(items):
    d = collections.defaultdict(list)
    for k, v in items:
        d[k].append(v)
    return dict(d)


def _clean_name(name):
    return unidecode(name).lower().strip()


def _legal_term_variants(term: str) -> Set[str]:
    # TODO - it is probably way to complicated
    if "." in term:
        variants = {
            term,
            term.strip("."),
            term.replace(".", "") + ".",
            term.replace(".", " ").replace("  ", " ").strip(),
            term.replace(".", ""),
            term.replace(".", ". ").replace("  ", " ").strip(),
        }
        return variants

    return {
        term,
        term + ".",
        *(
            " ".join(w).strip()
            for w in itertools.product(
                *[
                    (" ".join(t), "".join(t), ". ".join(t) + ".", ".".join(t) + ".")
                    for t in term.split()
                ]
            )
        ),
    }


CLEANED_LEGAL_ENTITIES: Mapping[str, Tuple[str, ...]] = {
    _clean_name(name): tuple(
        itertools.chain.from_iterable(_legal_term_variants(_clean_name(n)) for n in abbreviations)
    )
    for name, abbreviations in KNOWN_LEGAL_ENTITIES.items()
}

if len(KNOWN_LEGAL_ENTITIES) != len(CLEANED_LEGAL_ENTITIES):
    raise Exception("Duplicated entries in known legal entities")


ALL_LEGAL_TERMS: Set[str] = set(
    itertools.chain.from_iterable(
        {name, *abbreviations}
        for name, abbreviations in CLEANED_LEGAL_ENTITIES.items()
    )
)


LEGAL_TERMS_MAPPING: Mapping[
    str, Sequence[str]
] = _prepare_dict_from_list_with_duplicates(
    itertools.chain.from_iterable(
        [(a, name) for a in abbreviations] + [(name, name)]
        for name, abbreviations in CLEANED_LEGAL_ENTITIES.items()
    )
)


def legal_score(first: Sequence[str], second: Sequence[str]) -> float:
    if not first or not second:
        # not enough information - maybe should return None?
        return 0.5

    normalized_first: Sequence[Sequence[str]] = [
        LEGAL_TERMS_MAPPING.get(t, t) for t in first
    ]
    normalized_second: Sequence[Sequence[str]] = [
        LEGAL_TERMS_MAPPING.get(t, t) for t in second
    ]

    appeared_in_first: Set[str] = set(itertools.chain(*normalized_first))
    appeared_in_second: Set[str] = set(itertools.chain(*normalized_second))

    first_coverage: float = statistics.mean(
        any(possible_name in appeared_in_second for possible_name in legal_term)
        for legal_term in normalized_first
    )
    second_coverage: float = statistics.mean(
        any(possible_name in appeared_in_first for possible_name in legal_term)
        for legal_term in normalized_second
    )

    return (first_coverage + second_coverage) / 2

