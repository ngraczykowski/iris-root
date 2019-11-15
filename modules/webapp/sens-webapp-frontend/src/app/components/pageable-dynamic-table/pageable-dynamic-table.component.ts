import { Component, Input, OnDestroy, OnInit, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Subscription } from 'rxjs/Subscription';
import { TableData } from '../dynamic-view-table/dynamic-view-table.component';

export interface TableDataProvider {
  getPage(page: number, size: number, userNameFilter?: string): Observable<TableData>;
}

export interface TablePagingQueryParams {
  rowSize: number;
  page: number;
}

@Component({
  selector: 'app-pageable-dynamic-table',
  templateUrl: './pageable-dynamic-table.component.html',
  styleUrls: ['./pageable-dynamic-table.component.scss']
})
export class PageableDynamicTableComponent implements OnInit, OnDestroy {

  private readonly defaultRowCountPerPageOptions = [5, 10, 15, 20];
  private readonly defaultRowCountPerPage = 10;
  private readonly defaultPage = 1;

  @Output() loading = new EventEmitter<boolean>(true);

  @Input()
  set provider(provider: TableDataProvider) {
    this._provider = provider;
    if (this.initialized) {
      this.resetToFirstPage();
      this.loadPage();
    }
  }

  @Input() tableHints: string[];
  @Input() previewElements: boolean;

  private _provider: TableDataProvider;

  @Input()
  set page(page: number) {
    this._page = page;
    if (this.initialized) {
      this.loadPage();
    }
  }

  get page(): number {
    return this._page;
  }

  private _page: number = this.defaultPage;

  @Input()
  set rowCountPerPage(rowCountPerPage: number) {
    this._rowCountPerPage = rowCountPerPage;
    if (this.initialized) {
      this.resetToFirstPage();
      this.loadPage();
    }
  }

  get rowCountPerPage() {
    return this._rowCountPerPage;
  }

  private _rowCountPerPage = this.defaultRowCountPerPage;

  @Input() rowCountPerPageOptions = this.defaultRowCountPerPageOptions;
  @Input() translatePrefix;
  @Input() tableName: string;

  currentPageTableData: TableData;
  inProgress;
  error;

  private subscriptions: Array<Subscription> = [];
  private initialized: boolean;
  isFiltering = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.initialized = true;
    this.subscriptions.push(
      this.route.queryParams.subscribe((data: TablePagingQueryParams) => this.handlePageQuery(Number(data.page), Number(data.rowSize)))
    );
  }

  ngOnDestroy() {
    this.cancelSubscription();
  }

  handlePageQuery(page: number, rowSize: number) {
    if (rowSize) {
      this.rowCountPerPage = rowSize;
    }
    if (page) {
      this.page = page;
    }
    this.loadPage();
  }

  filterData(query) {
    this.resetToFirstPage();
    this.page = 1;
    this.isFiltering = true;
    this.loadPage(query);
  }

  resetData() {
    this.isFiltering = false;
    this.loadPage();
  }

  private loadPage(filter?: string) {
    this.inProgress = true;
    this.loading.emit(true);
    this.cancelSubscription();
    this.subscriptions.push(this._provider.getPage(this.page, this.rowCountPerPage, filter)
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(
          data => this.onLoadSuccess(data),
          error => this.onLoadError(error)
        ));
  }

  private onLoadSuccess(data) {
    this.error = null;
    this.currentPageTableData = data;
    this.loading.emit(false);
    this.setQueryParamsInUrl();
  }

  private onLoadError(error) {
    this.error = error;
  }

  private cancelSubscription() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  private resetToFirstPage() {
    this._page = 1;
  }

  private setQueryParamsInUrl() {
    this.router.navigate([], { queryParamsHandling: 'merge', queryParams: { page: this._page, rowSize: this._rowCountPerPage } });
  }

  shouldSetStaticHeightStyle() {
    if (this.currentPageTableData.total > this.rowCountPerPage) {
      return 'pageable-dynamic-table-row-count-' + this.rowCountPerPage;
    }
  }
}
