import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TableData } from '../dynamic-view-table/dynamic-view-table.component';
import { TableDataProvider } from './pageable-dynamic-table.component';

export interface TablePage<T> {
  total: number;
  items: T[];
}

export interface TablePageLoader<T> {
  load(page: number, size: number): Observable<TablePage<T>>;
}

export interface TablePageMapper<T> {
  map(page: TablePage<T>): TableData;
}

export class SimpleTableDataProvider<T> implements TableDataProvider {

  constructor(private loader: TablePageLoader<T>, private mapper: TablePageMapper<T>) { }

  getPage(page: number, size: number): Observable<TableData> {
    return this.loader.load(page, size).pipe(map(p => this.mapper.map(p)));
  }
}
