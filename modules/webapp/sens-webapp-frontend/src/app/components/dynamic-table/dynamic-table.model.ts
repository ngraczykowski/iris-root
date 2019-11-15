export class TableData {
  total: number;
  labels: string[];
  rows: Row[];
}

export class Row {
  values: any[];
}

export interface TableDataMapper<T> {
  map(response: T): TableData;
}
