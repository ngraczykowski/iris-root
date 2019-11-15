import { Row } from '../../components/dynamic-table/dynamic-table.model';
import { Alert, AlertModel, AlertPageResponse } from '../model/alert.model';
import {
  AlertRow,
  FullAlertTableDataMapper,
  SolutionsAlertTableDataMapper
} from './alert-table-data-mapper';

class TestHelper {
  static createAlert(index: number): Alert {
    return <Alert>{
      id: index,
      externalId: 'externalId' + index,
      analystSolution: {
        comment: 'comment' + index,
        date: '2018-07-06T06:35:55.455Z',
        decision: 'decision' + index
      },
      matchFields: ['m1' + index, 'm2' + index],
      alertFields: ['a1' + index, 'a2' + index]
    };
  }

  static assertRows(rows: Row[], expectedRows: Row[]) {
    expect(rows.length).toEqual(expectedRows.length);
    rows.forEach((row, i) => {
      expect(JSON.stringify(row)).toEqual(JSON.stringify(expectedRows[i]));
    });
  }
}

describe('AlertTableDataMapper [all fields]', () => {
  let mapper: FullAlertTableDataMapper;

  beforeEach(() => {
    Date.prototype.toLocaleString = Date.prototype.toUTCString;
    mapper = new FullAlertTableDataMapper();
  });


  it('should create valid response when pass null alert page response', () => {
    const response = mapper.map(null);

    expect(response.total).toEqual(0);
    expect(response.labels).toEqual([]);
    TestHelper.assertRows(response.rows, []);
  });

  it('should create valid response when pass empty alert page response', () => {
    const response = mapper.map(new AlertPageResponse());

    expect(response.total).toEqual(0);
    expect(response.labels).toEqual([]);
    TestHelper.assertRows(response.rows, []);
  });

  it('should create valid labels when pass alert page with empty alert model', () => {
    const page = new AlertPageResponse();
    page.alertModel = new AlertModel();

    const response = mapper.map(page);

    expect(response.labels)
        .toEqual(['Alert ID', 'analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate']);
  });

  it('should create valid labels when pass alert page with valid model', () => {
    const page = new AlertPageResponse();
    page.alertModel = new AlertModel();
    page.alertModel.alertFieldNames = ['alertField1', 'alertField2'];
    page.alertModel.matchFieldNames = ['matchField1', 'matchField2'];

    const response = mapper.map(page);

    expect(response.labels).toEqual([
      'Alert ID', 'alertField1', 'alertField2', 'matchField1', 'matchField2',
      'analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate'
    ]);
  });

  it('should create valid response when pass valid alert page', () => {
    const page = new AlertPageResponse();
    page.total = 5;
    page.alertModel = new AlertModel();
    page.alertModel.alertFieldNames = ['alertField1', 'alertField2'];
    page.alertModel.matchFieldNames = ['matchField1', 'matchField2'];
    page.alerts = [];
    page.alerts.push(TestHelper.createAlert(1), TestHelper.createAlert(2), TestHelper.createAlert(3));

    const response = mapper.map(page);

    expect(response.total).toEqual(5);
    expect(response.labels).toEqual([
      'Alert ID', 'alertField1', 'alertField2', 'matchField1', 'matchField2',
      'analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate'
    ]);
    TestHelper.assertRows(response.rows, [
      <AlertRow>{
        id: 1,
        externalId: 'externalId1',
        values: [
          'externalId1', 'a11', 'a21', 'm11', 'm21', 'decision1', 'comment1', 'Fri, 06 Jul 2018 06:35:55 GMT',
        ]
      },
      <AlertRow>{
        id: 2,
        externalId: 'externalId2',
        values: [
          'externalId2', 'a12', 'a22', 'm12', 'm22', 'decision2', 'comment2', 'Fri, 06 Jul 2018 06:35:55 GMT',
        ]
      },
      <AlertRow>{
        id: 3,
        externalId: 'externalId3',
        values: [
          'externalId3', 'a13', 'a23', 'm13', 'm23', 'decision3', 'comment3', 'Fri, 06 Jul 2018 06:35:55 GMT',
        ]
      }
    ]);
  });
});
describe('AlertTableDataMapper [only solution]', () => {
  let mapper: SolutionsAlertTableDataMapper;

  beforeEach(() => {
    Date.prototype.toLocaleString = Date.prototype.toUTCString;
    mapper = new SolutionsAlertTableDataMapper();
  });


  it('should create valid response when pass null alert page response', () => {
    const response = mapper.map(null);

    expect(response.total).toEqual(0);
    expect(response.labels).toEqual([]);
    TestHelper.assertRows(response.rows, []);
  });

  it('should create valid response when pass empty alert page response', () => {
    const response = mapper.map(new AlertPageResponse());

    expect(response.total).toEqual(0);
    expect(response.labels).toEqual([]);
    TestHelper.assertRows(response.rows, []);
  });

  it('should create valid labels when pass alert page with empty alert model', () => {
    const page = new AlertPageResponse();
    page.alertModel = new AlertModel();

    const response = mapper.map(page);

    expect(response.labels)
        .toEqual(['Alert ID', 'analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate']);
  });

  it('should create valid labels when pass alert page with valid model', () => {
    const page = new AlertPageResponse();
    page.alertModel = new AlertModel();
    page.alertModel.alertFieldNames = ['alertField1', 'alertField2'];
    page.alertModel.matchFieldNames = ['matchField1', 'matchField2'];

    const response = mapper.map(page);

    expect(response.labels).toEqual([
      'Alert ID', 'analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate'
    ]);
  });

  it('should create valid response when pass valid alert page', () => {
    const page = new AlertPageResponse();
    page.total = 5;
    page.alertModel = new AlertModel();
    page.alertModel.alertFieldNames = ['alertField1', 'alertField2'];
    page.alertModel.matchFieldNames = ['matchField1', 'matchField2'];
    page.alerts = [];
    page.alerts.push(TestHelper.createAlert(1), TestHelper.createAlert(2), TestHelper.createAlert(3));

    const response = mapper.map(page);

    expect(response.total).toEqual(5);
    expect(response.labels).toEqual([
      'Alert ID', 'analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate'
    ]);
    TestHelper.assertRows(response.rows, [
      <AlertRow>{
        id: 1,
        externalId: 'externalId1',
        values: [
          'externalId1', 'decision1', 'comment1', 'Fri, 06 Jul 2018 06:35:55 GMT',
        ]
      },
      <AlertRow>{
        id: 2,
        externalId: 'externalId2',
        values: [
          'externalId2', 'decision2', 'comment2', 'Fri, 06 Jul 2018 06:35:55 GMT',
        ]
      },
      <AlertRow>{
        id: 3,
        externalId: 'externalId3',
        values: [
          'externalId3', 'decision3', 'comment3', 'Fri, 06 Jul 2018 06:35:55 GMT',
        ]
      }
    ]);
  });
});
