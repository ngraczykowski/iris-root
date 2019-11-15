import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TableData } from '../../components/dynamic-view-table/dynamic-view-table.component';
import { TableDataMapper } from '../../components/dynamic-view-table/table-data-mapper';
import { TableDataProvider } from '../../components/pageable-dynamic-table/pageable-dynamic-table.component';
import { CollectionResponse } from '../model/collection-response.model';
import { InboxMessage } from '../model/inbox.model';

export type InboxMessageProvider = (page: number, size: number) => Observable<CollectionResponse<InboxMessage>>;

export class InboxMessageTableDataProvider implements TableDataProvider {

  constructor(
      private messageProvider: InboxMessageProvider,
      private mapper: TableDataMapper<InboxMessage>
  ) {}

  getPage(page: number, size: number): Observable<TableData> {
    return this.messageProvider(page, size)
        .pipe(map(data => this.mapper.map(data.total, data.results)));
  }
}
