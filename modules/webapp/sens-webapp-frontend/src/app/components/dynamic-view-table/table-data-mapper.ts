import { View } from '../dynamic-view/dynamic-view.component';
import { Label, Row, TableData } from './dynamic-view-table.component';

export interface LabelViewFactory {
  create(): View;
}

export interface CellViewFactory<T> {
  create(entry: T): View;
}

export interface TableDataColumnDefinition<T> {
  labelFactory: LabelViewFactory;
  cellFactory: CellViewFactory<T>;
}

export interface TableDataMapperConfiguration<T> {
  columnDefinitions: TableDataColumnDefinition<T>[];
}

export class TableDataMapper<T> {

  private readonly labels: Label[];

  constructor(private config: TableDataMapperConfiguration<T>) {
    this.labels = this.buildLabels();
  }

  map(total: number, results: T[], filterQuery?: string): TableData {
    return {
      total: total,
      labels: this.labels,
      rows: this.buildRows(results, filterQuery)
    };
  }

  private buildLabels(): Label[] {
    return this.config.columnDefinitions
      .map(d => d.labelFactory.create())
      .map(v => <Label>{view: v});
  }

  private buildRows(results: T[], filterQuery?: string) {
    return results.map(r => <Row> {
      views: this.config.columnDefinitions
        .map(d => d.cellFactory)
        .map(f => f.create({...r, filterQuery: filterQuery}))
    });
  }
}
