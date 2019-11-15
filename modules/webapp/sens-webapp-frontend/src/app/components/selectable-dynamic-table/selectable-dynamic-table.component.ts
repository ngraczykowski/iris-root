import { Component, Input, OnInit } from '@angular/core';
import {
  SimpleTableDataProvider,
  TablePageLoader,
  TablePageMapper
} from '../pageable-dynamic-table/simple-table-data-provider';
import { SelectableItemStore } from './selectable-item-store';
import { SelectableTablePageLoader } from './selectable-table-page-loader';
import { SelectableTablePageMapper } from './selectable-table-page-mapper';
import { SelectableTablePageService } from './selectable-table-page-service';

export interface SelectableItemDiscriminator<T> {
  getId(item: T): any;
}

export interface ProviderConfiguration<T> {
  discriminator: SelectableItemDiscriminator<T>;
  store: SelectableItemStore<T>;
  loader: TablePageLoader<T>;
  mapper: TablePageMapper<T>;
}

@Component({
  selector: 'app-selectable-dynamic-table',
  templateUrl: './selectable-dynamic-table.component.html',
  styleUrls: ['./selectable-dynamic-table.component.scss']
})
export class SelectableDynamicTableComponent<T> implements OnInit {

  @Input()
  set providerConfig(providerConfig: ProviderConfiguration<T>) {
    this._providerConfig = providerConfig;
    this.refreshProvider();
  }

  @Input() tableHints: string[];
  @Input() rowCountPerPage: number;
  @Input() rowCountPerPageOptions: number[];
  @Input() translatePrefix: string;
  @Input() tableName: string;
  @Input() previewElements: boolean;

  private _providerConfig: ProviderConfiguration<T>;

  provider: SimpleTableDataProvider<T>;

  constructor() { }

  ngOnInit() {
  }

  refreshProvider() {
    const selectionService = new SelectableTablePageService(
        this._providerConfig.discriminator, this._providerConfig.store);

    this.provider = new SimpleTableDataProvider<T>(
        new SelectableTablePageLoader(this._providerConfig.loader, selectionService),
        new SelectableTablePageMapper(this._providerConfig.mapper, selectionService)
    );
  }
}
