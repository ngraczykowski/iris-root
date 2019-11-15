import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { TablePage, TablePageLoader } from '../pageable-dynamic-table/simple-table-data-provider';
import { SelectableTablePageService } from './selectable-table-page-service';

export class SelectableTablePageLoader<T> implements TablePageLoader<T> {

  constructor(private innerLoader: TablePageLoader<T>, private selectionService: SelectableTablePageService<T>) {}

  load(page: number, size: number): Observable<TablePage<T>> {
    return this.innerLoader.load(page, size)
        .pipe(
            tap(p => this.selectionService.setVisibleItems(p.items))
        );
  }
}
