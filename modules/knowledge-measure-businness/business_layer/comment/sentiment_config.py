from dataclasses import dataclass


@dataclass
class SentimentCommentConfig:
    negative_comment: str
    positive_comment: str
