def create_agent_input_agg_col_config(agent_input_prepended_agent_name_config):
    """Create the source and target columns based on the standardized agent input config.

    Input:
    { 'name_agent': {'name_agent_ap': ['record_name', 'whatever_other_name'],
                     'name_agent_ap_aliases': [],
                     'name_agent_wl': ['name_hit'],
                     'name_agent_wl_aliases': []
                    }
    }

    Output:
    {'name_agent': {'ap_all_names_aggregated': ['name_agent_ap', 'name_agent_ap_aliases'],
                    'wl_all_names_aggregated': ['name_agent_wl', 'name_agent_wl_aliases']
                   }
    }
    """

    def _generate_simple_plural(word):
        if word.lower().endswith("s"):
            return word.lower() + "es"
        elif word.lower().endswith("y") and word.lower()[-2:] not in [
            "ay",
            "ey",
            "iy",
            "oy",
            "uy",
        ]:
            return word.lower()[:-1] + "ies"
        else:
            return word.lower() + "s"

    def _get_ap_or_wl_agg_source_cols(level_1_value, party):
        source_cols = []
        for col in level_1_value.keys():
            if (
                col.endswith(f"_{party}")
                or col.endswith(f"_{party}_aliases")
                or col.startswith("hit")
            ):
                source_cols.append(col)

        return source_cols

    agent_input_agg_col_config = {}

    for agent_name, config in agent_input_prepended_agent_name_config.items():
        agent_type = agent_name.split("_agent", 1)[0]
        agent_ap_agg_col = f"""ap_all_{_generate_simple_plural(agent_type)}_aggregated"""
        agent_wl_agg_col = f"""wl_all_{_generate_simple_plural(agent_type)}_aggregated"""
        agent_ap_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "ap")
        agent_wl_agg_source_cols = _get_ap_or_wl_agg_source_cols(config, "wl")

        agent_input_agg_col_config[agent_name] = {}
        agent_input_agg_col_config[agent_name][agent_ap_agg_col] = agent_ap_agg_source_cols
        agent_input_agg_col_config[agent_name][agent_wl_agg_col] = agent_wl_agg_source_cols

    return agent_input_agg_col_config


def prepend_agent_name_to_ap_or_wl_or_aliases_key(agent_input_config):
    """Prepend the agent name (level 1 key) to level 2 key. So the new level 2 key will be

    Input:
    { 'name_agent': {'ap': ['record_name'],
                    'ap_aliases': [],
                    'wl': ['name_hit'],
                    'wl_aliases': []
                    }
    }

    Output:
    { 'name_agent': {'name_agent_ap': ['record_name'],
                    'name_agent_ap_aliases': [],
                    'name_agent_wl': ['name_hit'],
                    'name_agent_wl_aliases': []
                    }
    }
    """
    result = {}
    for agent_name, config in agent_input_config.items():
        result[agent_name] = {}

        for ap_or_wl_or_aliases, source_cols in config.items():
            prepended_key_name = "_".join([agent_name, ap_or_wl_or_aliases])
            result[agent_name][prepended_key_name] = source_cols

    return result
