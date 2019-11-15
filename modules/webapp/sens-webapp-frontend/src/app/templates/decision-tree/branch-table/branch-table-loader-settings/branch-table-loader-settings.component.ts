import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { BranchPageLoader } from '@app/templates/decision-tree/branch-table/branch-page-loader';
import {
  ProviderConfig,
  SearchBranchPageProvider
} from '@app/templates/decision-tree/branch-table/search-branch-page-provider';
import { BranchPage } from '../branch-page';
import { BranchPageClient } from '../branch-page-client';
import { BranchLoaderListener } from '../branch-page-loader';

@Component({
  selector: 'app-branch-table-loader-settings',
  templateUrl: './branch-table-loader-settings.component.html',
  styleUrls: ['./branch-table-loader-settings.component.scss']
})
export class BranchTableLoaderSettingsComponent implements OnInit, OnDestroy, BranchLoaderListener {

  private readonly defaultQuery = '';

  @Input() decisionTreeId: number;
  @Output() loaderChange: EventEmitter<BranchPageLoader> = new EventEmitter();

  set query(query: string) {
    this._query = query;
    this.resetLoader();
  }

  get query() {
    return this._query;
  }

  private _query: string;
  private loader: BranchPageLoader;

  total: number;
  error;

  constructor(private client: BranchPageClient) { }

  ngOnInit() {
    this.query = this.defaultQuery;
  }

  ngOnDestroy() {
    this.unregisterListener();
  }

  onLoadSuccess(page: BranchPage) {
    this.total = page.total;
    this.error = null;
  }

  onLoadError(error) {
    this.total = null;
    this.error = error;
  }

  private resetLoader() {
    this.unregisterListener();
    this.loader = new BranchPageLoader(this.createProvider());
    this.registerListener();
    this.loaderChange.emit(this.loader);
  }

  private createProvider() {
    return new SearchBranchPageProvider(this.client, <ProviderConfig>  {
      decisionTreeId: this.decisionTreeId,
      query: this.query
    });
  }

  private registerListener() {
    this.loader.registerListener(this);
  }

  private unregisterListener() {
    if (this.loader) {
      this.loader.unregisterListener(this);
    }
  }
}
