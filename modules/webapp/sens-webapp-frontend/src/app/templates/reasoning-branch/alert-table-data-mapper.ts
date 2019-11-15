import { Row, TableData, TableDataMapper } from '@app/components/dynamic-table/dynamic-table.model';
import { DateFormatter } from '@app/shared/date/date-formatter';
import { AlertPageResponse } from '../model/alert.model';

export class AlertRow extends Row {
  constructor(public id, public externalId) {
    super();
  }
}

export class SolutionsAlertTableDataMapper implements TableDataMapper<AlertPageResponse> {

  map(response: AlertPageResponse): TableData {
    const data = new TableData();
    data.total = this.getTotal(response);
    data.labels = this.buildLabels(response);
    data.rows = this.buildRows(response);
    return data;
  }

  private getTotal(response: AlertPageResponse): number {
    return response && response.total ? response.total : 0;
  }

  private buildLabels(response: AlertPageResponse): string[] {
    const labels = [];
    if (response && response.alertModel) {
      labels.push('Alert ID', 'analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate');
    }
    return labels;
  }

  private buildRows(response: AlertPageResponse): AlertRow[] {
    const rows = [];
    if (response && response.alerts) {
      for (const alert of response.alerts) {
        const row = new AlertRow(alert.id, alert.externalId);
        row.values = [];
        row.values.push(
          alert.externalId,
          alert.analystSolution ? alert.analystSolution.decision : null,
          alert.analystSolution ? alert.analystSolution.comment : null,
          alert.analystSolution ? DateFormatter.format(alert.analystSolution.date) : null);
        rows.push(row);
      }
    }
    return rows;
  }
}


export class FullAlertTableDataMapper implements TableDataMapper<AlertPageResponse> {

  constructor() { }

  map(response: AlertPageResponse): TableData {
    const data = new TableData();
    data.total = this.getTotal(response);
    data.labels = this.buildLabels(response);
    data.rows = this.buildRows(response);
    return data;
  }

  private getTotal(response: AlertPageResponse): number {
    return response && response.total ? response.total : 0;
  }

  private buildLabels(response: AlertPageResponse): string[] {
    const labels = [];
    if (response && response.alertModel) {
      labels.push('Alert ID');
      if (response.alertModel.alertFieldNames) {
        response.alertModel.alertFieldNames.forEach(n => labels.push(n));
      }
      if (response.alertModel.matchFieldNames) {
        response.alertModel.matchFieldNames.forEach(n => labels.push(n));
      }
      labels.push('analystSolutionDecision', 'analystSolutionComment', 'analystSolutionDate');
    }
    return labels;
  }

  private buildRows(response: AlertPageResponse): AlertRow[] {
    const rows = [];
    if (response && response.alerts) {
      for (const alert of response.alerts) {
        const row = new AlertRow(alert.id, alert.externalId);
        row.values = [];
        row.values.push(alert.externalId);
        if (alert.alertFields) {
          alert.alertFields.forEach(v => row.values.push(this.htmlDecode(v)));
        }
        if (alert.matchFields) {
          alert.matchFields.forEach(v => row.values.push(v));
        }
        row.values.push(
          alert.analystSolution ? alert.analystSolution.decision : null,
          alert.analystSolution ? alert.analystSolution.comment : null,
          alert.analystSolution ? DateFormatter.format(alert.analystSolution.date) : null
        );
        rows.push(row);
      }
    }
    return rows;
  }

  /**
   * TODO (iwnek)- this is temporary solution, we should migrate to new dynamic table component and use [innerHtml] directive
   */
  private htmlDecode(input) {
    const element = document.createElement('div');
    element.innerHTML = input;
    return element.childNodes.length === 0 ? '' : element.childNodes[0].nodeValue;
  }
}
