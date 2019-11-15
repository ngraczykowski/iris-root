import {
  Component,
  ContentChild,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  TemplateRef
} from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { TableHelper } from '@app/shared/helpers/table-helper';
import 'rxjs/add/operator/finally';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { TableData } from './dynamic-table.model';

export type DynamicComponentProvider = (page: number, size: number) => Observable<TableData>;

@Component({
  selector: 'app-dynamic-table',
  templateUrl: './dynamic-table.component.html',
  styleUrls: ['./dynamic-table.component.scss'],
  providers: [TableHelper]
})
export class DynamicTableComponent implements OnInit, OnDestroy {

  minRowsPerPage = 5;
  defaultRowPerPage = 10;

  @Input() page: number;
  @Input() defaultRowCountPerPage = this.defaultRowPerPage;
  @Input() possibleRowCountsPerPage = [this.minRowsPerPage, this.defaultRowPerPage, 15, 20];

  @Input() title = 'dynamicTable.default.title';
  @Input() tableDataPrefix = 'dynamicTable.default.data';
  @Input() linksInRows;
  @Input() tableHints: string[];

  @ContentChild(TemplateRef, {static: true}) extraButtonTemplate: TemplateRef<ElementRef>;

  private _provider: DynamicComponentProvider;

  @Input()
  set provider(provider: DynamicComponentProvider) {
    this._provider = provider;
    if (this.initialized) {
      this.selectPage(1);
    }
  }

  @Output() itemClick: EventEmitter<any> = new EventEmitter();
  @Output() pageChange: EventEmitter<number> = new EventEmitter();
  @Output() previewElement: EventEmitter<any> = new EventEmitter();

  tableData;

  rowsPerPage = this.defaultRowCountPerPage;

  lastPage;
  visibleRowsText;
  showRowsPerPageDropdown;

  inProgress;
  error;

  perfectScrollbarCustom = {
    suppressScrollY: true,
    wheelPropagation: true,
  };

  previewStatus = false;
  previewData = [];

  private providerSubscription: Subscription;
  private initialized: boolean;

  constructor(
      public tableHelper: TableHelper,
      private eventService: LocalEventService
  ) { }

  ngOnInit() {
    this.selectPage(this.page);
    this.initialized = true;
  }

  ngOnDestroy() {
    this.cancelSubscription();
  }

  shouldDisableNextPageButton() {
    return this.page === this.lastPage;
  }

  shouldDisablePreviousPageButton() {
    return this.page === 1;
  }

  hasNoElements() {
    return !this.tableData || this.tableData.total === 0;
  }

  isPartiallyFull() {
    return this.tableData.total < this.minRowsPerPage + 1 || this.tableData.total < 2;
  }

  hasOnlyOneRow() {
    return this.tableData.total === 1;
  }

  onSelectRowsPerPage(rowsPerPage: number) {
    this.rowsPerPage = rowsPerPage;
    this.selectPage(1);
    this.pageChange.emit(1);
  }

  onSelectItem(index) {
    this.itemClick.emit({item: this.tableData.rows[index]});
  }

  onNextPage() {
    this.selectPage(this.page + 1);
    this.pageChange.emit(this.page + 1);
  }

  onPreviousPage() {
    this.selectPage(this.page - 1);
    this.pageChange.emit(this.page - 1);
  }

  getLabelPrefix() {
    return this.tableDataPrefix + '.labels.';
  }

  getValuePrefix(label) {
    return this.tableDataPrefix + '.values.' + label + '.';
  }

  copyToClipboard(text) {
    const txtArea = document.createElement('textarea');
    txtArea.id = 'txt';
    txtArea.style.position = 'fixed';
    txtArea.style.top = '0';
    txtArea.style.left = '0';
    txtArea.style.opacity = '0';
    txtArea.value = text;
    document.body.appendChild(txtArea);
    txtArea.select();

    try {
      const successful = document.execCommand('copy');
      if (successful) {
        this.eventService.sendEvent({
          key: EventKey.NOTIFICATION,
          data: {
            type: 'success',
            message: 'copyToClipboard.success'
          }
        });
      }
    } catch (err) {
      this.eventService.sendEvent({
        key: EventKey.NOTIFICATION,
        data: {
          type: 'error',
          message: 'copyToClipboard.error'
        }
      });
    } finally {
      document.body.removeChild(txtArea);
    }
    return false;
  }

  private selectPage(page) {
    this.error = false;
    this.inProgress = true;
    this.cancelSubscription();
    this.providerSubscription = this._provider(page, this.rowsPerPage)
        .finally(() => this.inProgress = false)
        .subscribe(data => {
          this.tableData = data;
          this.page = page;
          this.updatePaginationInfo();
        }, e => {
          this.error = true;
          console.error(e);
        });
  }

  private updatePaginationInfo() {
    if (this.tableData.total > 0) {
      this.updateLastPage();
      this.updateVisibleRowsText();
    }
  }

  private updateLastPage() {
    this.lastPage = Math.ceil(this.tableData.total / this.rowsPerPage);
  }

  private updateVisibleRowsText() {
    const start = ((this.page - 1) * this.rowsPerPage) + 1;
    const end = start + this.rowsPerPage - 1;
    this.visibleRowsText = start + ' - ' + (end < this.tableData.total ? end : this.tableData.total);
  }

  private cancelSubscription() {
    if (this.providerSubscription) {
      this.providerSubscription.unsubscribe();
    }
  }

  elementPreview(rowData) {
    this.previewData = [];
    this.makePreviewValues(this.tableData.labels, rowData.values);
    this.previewStatus = true;
  }

  makePreviewValues(labels, values) {
    labels.forEach((key, index) => {
      this.previewData.push({
        'name': key,
        'value': values[index]
      });
    });
  }
}
