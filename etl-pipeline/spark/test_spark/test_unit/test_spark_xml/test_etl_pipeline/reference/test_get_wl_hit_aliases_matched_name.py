INPUT_WHITELIST = [
    [
        (
            "CPF",
            "China Petroleum Finance Co. Ltd",
            "Zhongyou Caiwu",
            "Zhong You Cai Wu",
            "中油财务有限责任公司",
            "中油財務有限責任公司",
        ),
        (
            "CPF",
            "China Petroleum Finance Co. Ltd",
            "Zhongyou Caiwu",
            "Zhong You Cai Wu",
            "中油财务有限责任公司",
            "中油財務有限責任公司",
        ),
        ("CPF",),
    ],
    [
        (
            "アリ・コニー",
            "アリ・ラロボ・バシール",
            "Bashir,Ali,Lalobo",
            "Kapere,Otim",
            "Kony,Ali,Mohammed",
            "Labola,Ali,Mohammed",
            "Labolo,Ali,Mohammad",
            "Lalobo,Ali",
            "Lalobo,Ali,Bashir",
            "Lalobo,Ali,Mohammed",
            "Salongo,Ali,Mohammed",
            "Mohammed,Ali",
            "１・Ｐ",
            "1-P",
            "Bashir",
            "Caesar",
            "One-P",
            "Otim Kapere",
            "1 P",
            "One P",
            "Ali Mohammed",
        ),
        (
            "アリ・コニー",
            "アリ・ラロボ・バシール",
            "{GN=Ali,Lalobo}{SN=Bashir}",
            "{GN=Otim}{SN=Kapere}",
            "{GN=Ali,Mohammed}{SN=Kony}",
            "{GN=Ali,Mohammed}{SN=Labola}",
            "{GN=Ali,Mohammad}{SN=Labolo}",
            "{GN=Ali}{SN=Lalobo}",
            "{GN=Ali,Bashir}{SN=Lalobo}",
            "{GN=Ali,Mohammed}{SN=Lalobo}",
            "{GN=Ali,Mohammed}{SN=Salongo}",
            "{GN=Ali}{SN=Mohammed}",
            "１・Ｐ",
            "1-P",
            "Bashir",
            "Caesar",
            "One-P",
            "Otim Kapere",
            "1 P",
            "One P",
            "Ali Mohammed",
        ),
        ("One P", "One P"),
    ],
    [
        (
            "김동명",
            "キム・トンミョン",
            "Дон Мён Ким",
            "キム・チンソク",
            "Чин Сок Ким",
            "Kim,Chin-So'k",
            "Kim,Jin Sok",
            "Kim,Dong Myong",
            "Kim,Hyok Chol",
            "Kim,",
            "Hyok-Chol,",
            "Tong My'ong,Kim",
            "Jin-Sok,Kim",
            "Tong-Myong,Kim",
            "Chin-So'k,Kim",
            "Kim,Tong Myong",
            "Kim,Tong Myo'ng",
            "Kim,Tong-Myong",
            "Kim,Tong My'ong",
            "Kim,Chin-Sok",
            "Kim,Chin So'k",
            "Kim,Jin-Sok",
            "Kim,Hyok-Chol",
            "Jin Sok Kim",
        ),
        (
            "김동명",
            "キム・トンミョン",
            "Дон Мён Ким",
            "キム・チンソク",
            "Чин Сок Ким",
            "{GN=Chin-So'k}{SN=Kim}",
            "{GN=Jin Sok}{SN=Kim}",
            "{GN=Dong Myong}{SN=Kim}",
            "{GN=Hyok Chol}{SN=Kim}",
            "Kim,",
            "Hyok-Chol,",
            "{GN=Kim}{SN=Tong My'ong}",
            "{GN=Kim}{SN=Jin-Sok}",
            "{GN=Kim}{SN=Tong-Myong}",
            "{GN=Kim}{SN=Chin-So'k}",
            "{GN=Tong Myong}{SN=Kim}",
            "{GN=Tong Myo'ng}{SN=Kim}",
            "{GN=Tong-Myong}{SN=Kim}",
            "{GN=Tong My'ong}{SN=Kim}",
            "{GN=Chin-Sok}{SN=Kim}",
            "{GN=Chin So'k}{SN=Kim}",
            "{GN=Jin-Sok}{SN=Kim}",
            "{GN=Hyok-Chol}{SN=Kim}",
            "Jin Sok Kim",
        ),
        ("Kim,",),
    ],
]

REFERENCE_OUTPUT_FOR_TEST_WL_HIT_ALIASES_MATCHED_NAME = [
    ("Kim,",),
    (
        "김동명",
        "キム・トンミョン",
        "Дон Мён Ким",
        "キム・チンソク",
        "Чин Сок Ким",
        "Kim,Chin-So'k",
        "Kim,Jin Sok",
        "Kim,Dong Myong",
        "Kim,Hyok Chol",
        "Kim,",
        "Hyok-Chol,",
        "Tong My'ong,Kim",
        "Jin-Sok,Kim",
        "Tong-Myong,Kim",
        "Chin-So'k,Kim",
        "Kim,Tong Myong",
        "Kim,Tong Myo'ng",
        "Kim,Tong-Myong",
        "Kim,Tong My'ong",
        "Kim,Chin-Sok",
        "Kim,Chin So'k",
        "Kim,Jin-Sok",
        "Kim,Hyok-Chol",
        "Jin Sok Kim",
    ),
    (
        "김동명",
        "キム・トンミョン",
        "Дон Мён Ким",
        "キム・チンソク",
        "Чин Сок Ким",
        "{GN=Chin-So'k}{SN=Kim}",
        "{GN=Jin Sok}{SN=Kim}",
        "{GN=Dong Myong}{SN=Kim}",
        "{GN=Hyok Chol}{SN=Kim}",
        "Kim,",
        "Hyok-Chol,",
        "{GN=Kim}{SN=Tong My'ong}",
        "{GN=Kim}{SN=Jin-Sok}",
        "{GN=Kim}{SN=Tong-Myong}",
        "{GN=Kim}{SN=Chin-So'k}",
        "{GN=Tong Myong}{SN=Kim}",
        "{GN=Tong Myo'ng}{SN=Kim}",
        "{GN=Tong-Myong}{SN=Kim}",
        "{GN=Tong My'ong}{SN=Kim}",
        "{GN=Chin-Sok}{SN=Kim}",
        "{GN=Chin So'k}{SN=Kim}",
        "{GN=Jin-Sok}{SN=Kim}",
        "{GN=Hyok-Chol}{SN=Kim}",
        "Jin Sok Kim",
    ),
]
