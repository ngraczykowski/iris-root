from pathlib import Path

REPLACEMENT_DICT = {
    "COUNTRYCODE": "ALL_PRTY_RSDNC_CNTRY_CD",
    "PARTY1_ADDRESS1_COUNTRY": "ALL_PARTY_ADDRESS1_COUNTRY",
    "PARTY1_COUNTRY1": "ALL_PARTY_COUNTRY1",
    "PARTY1_COUNTRY_CITIZENSHIP1": "ALL_PARTY_COUNTRY1_CITIZENSHIP",
    "PARTY1_COUNTRY_DOMICILE1": "ALL_PARTY_COUNTRY_DOMICILE1",
    "PARTY1_COUNTRY_FORMATION1": "ALL_PARTY_COUNTRY_FORMATION1",
    "PARTY1_DATE_OF_BIRTH": "ALL_PARTY_DOBS",
    "PARTY1_NAME_FULL": "ALL_PARTY_NAMES",
    "PRTY_TYP": "ALL_PARTY_TYPES",
    "WL_ALIASES": "watchlistParty.matchRecords.WL_ALIASES",
    "WL_CITIZENSHIP": "watchlistParty.matchRecords.WL_CITIZENSHIP",
    "WL_CITY": "watchlistParty.matchRecords.WL_CITY",
    "WL_COUNTRY": "watchlistParty.matchRecords.WL_COUNTRY",
    "WL_COUNTRYNAME": "watchlistParty.matchRecords.WL_COUNTRYNAME",
    "WL_DOB": "watchlistParty.matchRecords.WL_DOB",
    "WL_DOCUMENT_NUMBER": "watchlistParty.matchRecords.WL_DOCUMENT_NUMBER",
    "WL_NAME": "watchlistParty.matchRecords.WL_NAME",
    "WL_NATIONALITY": "watchlistParty.matchRecords.WL_NATIONALITY",
    "WL_POB": "watchlistParty.matchRecords.WL_POB",
    "WLP_TYPE": "watchlistParty.matchRecords.WLP_TYPE",
}


def is_file_relevant(filename):
    """
    Determine whether a specific file should be processed.
    """
    if filename.endswith(".yaml") and "ISG" in filename and "modified" not in filename:
        return True
    else:
        return False


def replace(line, match):
    return line.replace(match, REPLACEMENT_DICT[match])


def process_yaml_file(file, replacement_dict, keep_original_name, inplace, output_folder):
    """
    Process an individual yaml file line-by-line. We assume that in each line there will be at most 1 replacement.
    """
    with open(file, "r") as f:
        content = f.readlines()

    content_modified = ""

    for line in content:
        matches = [x for x in REPLACEMENT_DICT.keys() if x in line]

        if matches:
            content_modified += replace(line, matches[0])[:-1]

            if keep_original_name:
                content_modified += f" # was: {matches[0]}\n"
            else:
                content_modified += "\n"
        else:
            content_modified += line

    if inplace:
        file_modified = file
    else:
        file_modified = output_folder / file.name

    with open(file_modified, "w") as f:
        f.write(str(content_modified))


def process_folder(input_folder, replacement_dict, keep_original_name, inplace, output_folder):
    """
    Process the input_folder using the replacement_dictionary. If keep_original_name is True, then the original
    variable name is kept as a comment. If inplace, the input files are overwritten. Otherwise, the output is
    written to the output_folder.
    """
    files = [x for x in input_folder.iterdir() if is_file_relevant(str(x))]

    for file in files:
        process_yaml_file(file, replacement_dict, keep_original_name, inplace, output_folder)


if __name__ == "__main__":
    # the code below works fine in the ms-dumping-ground repo
    # note that the output folder must be created manually
    main_folder = Path(__file__).parent
    input_folder = main_folder / "agents_inputs"
    output_folder = main_folder / "agents_inputs_modified"

    if output_folder.exists():
        process_folder(input_folder, REPLACEMENT_DICT, True, False, output_folder)
    else:
        print(f"Output folder {output_folder} does not exist.")
