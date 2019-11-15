import {
  Row,
  TableData,
  TableDataMapper
} from '../../components/dynamic-table/dynamic-table.model';
import { Match, MatchesPage } from '../model/match.model';

export interface MatchRow extends Row {
  externalId: string;
  matchGroupId: number;
}

export class MatchTableField {
  dictionaryKey: string;
  valueMapping: (match: Match) => any;

  constructor(dictionaryKey: string, valueMapping: (match: Match) => any) {
    this.dictionaryKey = dictionaryKey;
    this.valueMapping = valueMapping;
  }
}

export const matchTableFields: { [key: string]: MatchTableField } = {
  ID: new MatchTableField('id', match => match.id),
  DISCRIMINATOR: new MatchTableField('discriminator', match => match.discriminator),
  EXTERNAL_ID: new MatchTableField('externalId', match => match.externalId),
  MATCH_GROUP_ID: new MatchTableField('matchGroupId', match => match.matchGroupId)
};

export class MatchTableDataMapper implements TableDataMapper<MatchesPage> {

  constructor(private matchFieldsNames: string[], private additionalFields: MatchTableField[]) {
  }

  public map(matches: MatchesPage): TableData {
    const rows = matches.content.map(this.mapMatchToRow());
    const additionalFieldLabels = this.additionalFields.map(field => field.dictionaryKey);
    const labels = additionalFieldLabels.concat(this.matchFieldsNames);
    const total = matches.totalElements;

    return {total, rows, labels} as TableData;
  }

  private mapMatchToRow() {
    return (match: Match) => {
      const additionalFieldValues = this.additionalFields.map(field => field.valueMapping(match));
      const values = additionalFieldValues.concat(match.matchFields);

      return {values, externalId: match.externalId, matchGroupId: match.matchGroupId} as MatchRow;
    };
  }
}
