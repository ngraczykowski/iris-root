from etl_pipeline.custom.ms.datatypes.status.analyst_solution import AnalystSolution


class NnsStatus:
    def __init__(self, status: str):
        self.status = status

    def to_analyst_solution(self) -> AnalystSolution:
        value = self.status.lower()

        if "case created" in value:
            return AnalystSolution.TRUE_POSITIVE
        elif "risk accepted" in value:
            return AnalystSolution.RISK_ACCEPTED
        elif "false positive" in value or "could have been closed" in value:
            return AnalystSolution.FALSE_POSITIVE

        return AnalystSolution.OTHER
