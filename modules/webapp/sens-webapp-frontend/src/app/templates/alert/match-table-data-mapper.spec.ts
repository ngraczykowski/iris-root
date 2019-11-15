import { Match, MatchesPage } from '../model/match.model';
import { MatchRow, MatchTableDataMapper, matchTableFields } from './match-table-data-mapper';
import arrayContaining = jasmine.arrayContaining;

const createMatchesPage = (matches: Match[]) => {
  return {
    content: matches,
    totalElements: matches.length
  } as MatchesPage;
};

const createMatch = (id, externalId, discriminator, matchGroupId, matchFields: string[]) => {
  return {id, externalId, discriminator, matchGroupId, matchFields} as Match;
};

describe('MatchTableDataMapper', () => {
  let underTest: MatchTableDataMapper;

  describe('given one MatchFieldName and fields: [id, externalId, discriminator]', () => {
    const matchFieldName = 'someMatchField';

    beforeEach(() => {
      underTest = new MatchTableDataMapper([matchFieldName],
          [matchTableFields.ID,
            matchTableFields.EXTERNAL_ID,
            matchTableFields.DISCRIMINATOR]);
    });

    it('should return TableData with one valid Row ' +
        'when map with Page containing one Match', () => {
      const matchFieldValue = 'matchField1';
      const match = createMatch(1, 'extId', 'disc', 1, [matchFieldValue]);
      const page = createMatchesPage([match]);

      const actual = underTest.map(page);

      expect(actual.total).toBe(1);
      expect(actual.labels).toContain(matchFieldName);
      const actualRow = actual.rows.pop();
      expect(actualRow.values).toEqual(
          arrayContaining([match.id, match.externalId, match.discriminator, matchFieldValue])
      );
    });

    it('should return TableData with two valid Rows ' +
        'when map with Page containing two Matches', () => {
      const matchFieldValue1 = 'matchField1';
      const matchFieldValue2 = 'matchField2';
      const match1 = createMatch(1, 'extId', 'disc', 1, [matchFieldValue1]);
      const match2 = createMatch(2, 'extId2', 'disc2', 1, [matchFieldValue2]);
      const page = createMatchesPage([match1, match2]);

      const actual = underTest.map(page);

      expect(actual.total).toBe(2);
      expect(actual.labels).toContain(matchFieldName);
      const actualRow1 = actual.rows[0];
      expect(actualRow1.values).toEqual(
          arrayContaining([match1.id, match1.externalId, match1.discriminator, matchFieldValue1])
      );
      const actualRow2 = actual.rows[1];
      expect(actualRow2.values).toEqual(
          arrayContaining([match2.id, match2.externalId, match2.discriminator, matchFieldValue2])
      );
    });

    it('should return TableData with valid Row ' +
        'when map with Page containing one Match, that has no discriminator', () => {
      const matchFieldValue = 'matchField1';
      const match = createMatch(1, 'extId', null, 1, [matchFieldValue]);
      const page = createMatchesPage([match]);

      const actual = underTest.map(page);

      expect(actual.total).toBe(1);
      expect(actual.labels).toContain(matchFieldName);
      const actualRow = actual.rows.pop();
      expect(actualRow.values).toEqual(
          arrayContaining([match.id, match.externalId, matchFieldValue])
      );
    });

    it('should return empty TableData with valid Labels and totalElements 0 ' +
        'when map with Page containing no Matches', () => {
      const page = {
        content: [],
        totalElements: 0
      } as MatchesPage;

      const actual = underTest.map(page);

      expect(actual.total).toBe(0);
      expect(actual.labels).toContain(matchFieldName);
    });

    it('should returned TableData contain Row with externalId defined ' +
        'when map with Page containing one Match', () => {
      const hasExternalIdDefined = (row) => (<MatchRow>row).externalId !== undefined;
      const matchFieldValue = 'matchField1';
      const match = createMatch(1, 'extId', 'disc', 1, [matchFieldValue]);
      const page = createMatchesPage([match]);

      const actual = underTest.map(page);

      const actualRow = actual.rows.pop();
      expect(hasExternalIdDefined(actualRow)).toBeTruthy();
    });
  });
});
