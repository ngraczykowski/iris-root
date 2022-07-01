from typing import Any, Dict, List

from business_layer.api import FieldMeasure
from business_layer.config.config import Config
from business_layer.config.datatypes import Comment


class CommentGenerator:
    def __init__(self, config: Config):
        self.requested_mappings: Dict[str, Any] = config.requested_mappings
        self.comment_intro: str = config.comment_intro
        self.comments: Dict[str, Comment] = config.comments

    def _generate_comment_for_feature(
        self, feature: str, comment_type: str, measures: Dict[str, FieldMeasure]
    ) -> str:
        comment_template = self.requested_mappings[feature].get_comment(comment_type)
        field_measure = measures[feature]
        matching_ap_values, matching_wl_values = [], []
        for value_measure in field_measure.results:
            if value_measure.evaluation == field_measure.recommendation:
                matching_ap_values.append(value_measure.ap_value)
                matching_wl_values.append(value_measure.wl_value)
        if not all((matching_ap_values, matching_wl_values)):
            return ""
        feature_comment = comment_template.format(
            ap_value=" | ".join(sorted(set(matching_ap_values))),
            wl_value=" | ".join(sorted(set(matching_wl_values))),
            context=field_measure.context,
        )

        return feature_comment

    def generate(
        self, decision: str, condition: Dict[str, List[str]], measures: Dict[str, FieldMeasure]
    ):
        decisions_comment = self.comments[decision]
        comment_intro = self.comment_intro.format(decisions_comment.verbal)
        field_comments = [
            self._generate_comment_for_feature(feature, decisions_comment.type, measures)
            for feature in condition.keys()
        ]
        comment_entries = "\n".join(field_comments)
        comment = comment_intro + comment_entries
        return comment.strip()
