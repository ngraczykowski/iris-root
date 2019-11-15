import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TableData } from '@app/components/dynamic-view-table/dynamic-view-table.component';
import {
  TablePage,
  TablePageLoader,
  TablePageMapper
} from '@app/components/pageable-dynamic-table/simple-table-data-provider';
import {
  ProviderConfiguration,
  SelectableItemDiscriminator
} from '@app/components/selectable-dynamic-table/selectable-dynamic-table.component';
import { SelectableItemStore } from '@app/components/selectable-dynamic-table/selectable-item-store';
import { ChangeRequest } from '@app/templates/approver/approver.model';
import { LABEL_VIEWS, mapItemToViews } from '../approver-table-shared/view-mappings';
import { OpenChangelogEvent } from '../approver.store';

@Component({
  selector: 'app-changes-selectable-table',
  templateUrl: './changes-selectable-table.component.html',
  styleUrls: ['../_table-approval-queue.scss']
})
export class ChangesSelectableTableComponent implements OnInit {

  constructor() {
  }

  @Input()
  tablePageLoader: TablePageLoader<ChangeRequest>;
  @Input()
  selectedApprovalsStore: SelectableItemStore<ChangeRequest>;
  @Output()
  openChangelog: EventEmitter<OpenChangelogEvent> = new EventEmitter();

  changelogProviderConfig: ProviderConfiguration<ChangeRequest>;

  private tableMapper: TablePageMapper<ChangeRequest> = {
    map: page => this.mapTablePageToTableData(page)
  };

  private discriminator: SelectableItemDiscriminator<ChangeRequest> = {
    getId: item => ChangesSelectableTableComponent.getChangeId(item)
  };

  private static getChangeId(item) {
    return `${item.changeId}`;
  }

  ngOnInit() {
    this.changelogProviderConfig = this.createProviderConfig();
  }

  private mapTablePageToTableData(tablePage: TablePage<ChangeRequest>): TableData {
    const rows = tablePage.items.map((item: ChangeRequest) => {
      return {
        views: mapItemToViews(item, this.openChangelog)
      };
    });

    return {
      total: tablePage.total,
      labels: LABEL_VIEWS,
      rows: rows
    };
  }

  private createProviderConfig(): ProviderConfiguration<ChangeRequest> {
    return {
      discriminator: this.discriminator,
      store: this.selectedApprovalsStore,
      loader: this.tablePageLoader,
      mapper: this.tableMapper
    };
  }
}

