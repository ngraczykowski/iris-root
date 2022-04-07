import re
import shutil
from pathlib import Path

TAB = " " * 4


TOKENS = (("alerted_party", "ap"), ("watchlist", "wl"))

NON_STANDARD_PLURALS = {"country": "countries"}

FEATURE_INPUTS_TO_IGNORE = ("VerifySanctions",)
FEATURE_INPUTS_PREFIX = {"HistoricalDecisions": "HistoricalDecisionsInput."}


class Name:
    """
    A class for storing name, lowercase name and plural; might be an overkill.
    """

    def __init__(self, name):
        self.name = name
        self.lower_case = name.lower()
        self.plural = NON_STANDARD_PLURALS.get(name.lower(), name.lower() + "s")

    def __str__(self):
        return self.name


def find_enums(content):
    """
    Identify enums in a (part of) proto file.
    """
    indices = (j for j in range(len(content)) if "enum" in content[j])

    enums = []

    for index in indices:
        match_name = re.search("enum ([A-Za-z]+) {", content[index])
        match_value = re.search("([A-Z_]+) =", content[index + 1])

        if match_name is not None and match_value is not None:
            enums.append((match_name.group(1), match_value.group(1)))

    return enums


def process_proto_file(proto_file, output_details):  # noqa: C901
    """
    Process a single proto file.
    """
    with open(proto_file, "r") as f:
        content = f.readlines()

    # identify the relevant part of the proto_file
    temp_list = [j for j in range(len(content)) if "FeatureInput {" in content[j]]
    assert len(temp_list) > 0, "No FeatureInput found in the file"
    start = temp_list[0]

    temp_list = [j for j in range(start + 1, len(content)) if content[j].startswith("message")]

    if len(temp_list) > 0:
        stop = temp_list[0]
    else:
        stop = len(content)

    enums = find_enums(content[start:stop])

    original_entry = "".join(content[start:stop]) + "\n"
    output = ""

    match = re.search("message ([A-Za-z]+)FeatureInput", content[start])

    if match is not None:
        name = Name(match.group(1))

        if str(name) not in FEATURE_INPUTS_TO_IGNORE:
            prefix = FEATURE_INPUTS_PREFIX.get(str(name), "")
            output = f"\n\nclass {name}FeatureInputProducer(Producer):\n"
            output += f'{TAB}feature_name = "features/{name.lower_case}"\n\n'
            output += f"{TAB}def produce_feature_input(self, payload):\n"
            output += f"{TAB}{TAB}return {prefix}{name}FeatureInput(\n"
            output += f"{TAB}{TAB}{TAB}feature=self.feature_name,\n"

            for line in content[start:stop]:
                processed = False

                # check if the field is an enum
                for enum in enums:
                    if enum[0] in line:
                        match = re.search(f"{enum[0]} ([a-z_]+) =", line)
                        if match is not None:
                            output += f"{TAB}{TAB}{TAB}{match.group(1)}={prefix}{name}FeatureInput.{enum[0]}.{enum[1]},\n"
                            processed = True

                # check if the field matches one of the tokens
                if not processed:
                    for token in TOKENS:
                        if token[0] in line:
                            match = re.search(f"string {token[0]}_([a-z_]+) =", line)

                            if match is not None:
                                if "repeated" in line:
                                    rhs = f'[element for element in payload.get("{token[1]}_all_{match.group(1)}_aggregated", [])]'
                                else:
                                    rhs = f'payload.get("{token[1]}_all_{match.group(1)}_aggregated", "")'

                                output += f"{TAB}{TAB}{TAB}{token[0]}_{match.group(1)}={rhs},\n"
                                processed = True

                # if a field is a string or repeated string which doesn't match any tokens, then pass empty value
                if not processed:
                    match = re.search(r"([a-z0-9]+) ([a-z_]+) =", line)

                    default_values = {"string": '""', "uint32": 0}

                    if (
                        match is not None
                        and match.group(1) in default_values.keys()
                        and match.group(2) != "feature"
                    ):
                        if "repeated" in line:
                            rhs = "[]"
                        else:
                            rhs = default_values[match.group(1)]

                        output += f"{TAB}{TAB}{TAB}{match.group(2)}={rhs},\n"

            output += f"{TAB}{TAB})"

        if output_details["output_separate_files"]:
            output_folder = output_details["separate_files_output_folder"]
            if output_folder.exists():
                with open(output_folder / proto_file.name, "w") as f:
                    f.write(original_entry + output)
            else:
                print(f"Output folder {output_folder} does not exist.")

    return output


def process_folder(input_folder, output_details):
    """
    Process an entire folder. The expected folder structure is the one I saw in datasource/api.
    This functions supports two ways of producing output (which can be used simultaneously).
    If 'output_separate_files' is True, then for every proto file an output file in output_folder
    is created which contains both the original proto file (the relevant part) and the resulting Python class.
    If append_to_producers is True, then we copy a template file and append all these classes at the end.
    """
    dirs_to_ignore = ("contextuallearning",)
    output = ""

    for x in input_folder.iterdir():
        # select directories which are not on the to-be-ignored list and have a 'v1' subdirectory
        if x.is_dir() and x.name not in dirs_to_ignore and (x / "v1").exists():
            proto_file = next((x / "v1").iterdir())
            new_class = process_proto_file(proto_file, output_details)

            if len(new_class) > 0:
                output += new_class + "\n"

    if output_details["append_to_producers"]:
        shutil.copy(output_details["producers_template"], output_details["producers_target"])

        with open(output_details["producers_target"], "a") as f:
            f.write(
                f"\n\n# The following entries have been generated automatically by {Path(__file__).name}\n"
            )
            f.write(output)


def extract_producers(producers_file):
    with open(producers_file, "r") as f:
        content = f.readlines()

    output = ""

    for line in content:
        match = re.search("class ([A-Za-z]+)\\(Producer\\)", line)

        if match is not None:
            output += f"{match.group(1)}(), "

    print(output)


if __name__ == "__main__":
    # The following works under the assumption that repos_folder is a folder in which both
    # data-source-api and etl-pipeline repos reside
    repos_folder = Path(__file__).parent.parent.parent
    input_folder = (
        repos_folder
        / "data-source-api"
        / "data-source-api"
        / "src"
        / "main"
        / "proto"
        / "silenteight"
        / "datasource"
        / "api"
    )
    output_details = {
        "output_separate_files": True,
        "separate_files_output_folder": repos_folder / "output_folder",
        "append_to_producers": True,
        "producers_template": repos_folder
        / "etl-pipeline"
        / "etl_pipeline"
        / "service"
        / "agent_router"
        / "producers.template",
        "producers_target": repos_folder
        / "etl-pipeline"
        / "etl_pipeline"
        / "service"
        / "agent_router"
        / "producers-new.py",
    }

    process_folder(input_folder, output_details)
    extract_producers(output_details["producers_target"])
