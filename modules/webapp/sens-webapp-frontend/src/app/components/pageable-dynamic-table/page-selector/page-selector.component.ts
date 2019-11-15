import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-page-selector',
  templateUrl: './page-selector.component.html',
  styleUrls: ['./page-selector.component.scss'],
})
export class PageSelectorComponent implements OnInit {
  @Input() isVisible;

  @Input()
  set page(page: number) {
    this._page = page;
    this.refreshView();
  }

  get page() {
    return this._page;
  }

  private _page: number;

  @Output() pageChange: EventEmitter<number> = new EventEmitter<number>();

  @Input()
  set rowCountPerPage(rowCountPerPage: number) {
    this._rowCountPerPage = rowCountPerPage;
    this.refreshView();
  }

  get rowCountPerPage() {
    return this._rowCountPerPage;
  }

  @Input()
  set total(total: number) {
    this._total = total;
    this.refreshView();
  }

  get total() {
    return this._total;
  }

  visibleRowsText: string;
  lastPage: number;

  private _total: number;
  private _rowCountPerPage: number;

  constructor() { }

  ngOnInit() {
    this.refreshView();
  }

  onSelectPreviousPage() {
    if (this.page > 1) {
      this.onSelectPage(this.page - 1);
    }
  }

  onSelectNextPage() {
    if (this.page < this.lastPage) {
      this.onSelectPage(this.page + 1);
    }
  }

  onSelectPage(page) {
    this._page = page;
    this.pageChange.emit(page);
    this.refreshView();
  }

  private refreshView() {
    this.calculateLastPage();
    this.updateVisibleRowsText();
  }

  private calculateLastPage() {
    this.lastPage = Math.ceil(this.total / this.rowCountPerPage);
  }

  private updateVisibleRowsText() {
    const start = ((this.page - 1) * this.rowCountPerPage) + 1;
    const end = start + this.rowCountPerPage - 1;
    this.visibleRowsText = start + ' - ' + (end < this.total ? end : this.total);
  }
}
