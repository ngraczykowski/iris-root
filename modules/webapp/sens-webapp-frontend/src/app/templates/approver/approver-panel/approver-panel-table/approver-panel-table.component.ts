import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Row, TableData } from '@app/components/dynamic-view-table/dynamic-view-table.component';
import { TableDataProvider } from '@app/components/pageable-dynamic-table/pageable-dynamic-table.component';
import {
  SimpleTableDataProvider,
  TablePage,
  TablePageLoader,
  TablePageMapper
} from '@app/components/pageable-dynamic-table/simple-table-data-provider';
import { ChangeRequest } from '@app/templates/approver/approver.model';
import { LABEL_VIEWS, mapItemToViews } from '../../approver-table-shared/view-mappings';
import { OpenChangelogEvent } from '../../approver.store';

@Component({
  selector: 'app-approve-changes-table',
  templateUrl: './approver-panel-table.component.html',
  styleUrls: ['../../_table-approval-queue.scss']
})
export class ApproverPanelTableComponent implements OnInit {

  provider: TableDataProvider;

  @Input()
  tablePageLoader: TablePageLoader<ChangeRequest>;
  @Output()
  openChangelog: EventEmitter<OpenChangelogEvent> = new EventEmitter();

  private tablePageMapper: TablePageMapper<ChangeRequest> = {
    map: (page) => this.mapToTableData(page)
  };

  constructor() { }

  ngOnInit() {
    this.provider = this.createTableProvider();
  }

  private createTableProvider(): TableDataProvider {
    return new SimpleTableDataProvider(this.tablePageLoader, this.tablePageMapper);
  }

  private mapToTableData(changesPage: TablePage<ChangeRequest>): TableData {
    return {
      total: changesPage.total,
      labels: LABEL_VIEWS,
      rows: this.mapItemsToRows(changesPage.items)
    };
  }

  private mapItemsToRows(items: ChangeRequest[]): Row[] {
    return items.map(item => {
      return {
        views: mapItemToViews(item, this.openChangelog)
      };
    });
  }
}
