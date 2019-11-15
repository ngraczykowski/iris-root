import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-page-row-count-selector',
  templateUrl: './page-row-count-selector.component.html',
  styleUrls: ['./page-row-count-selector.component.scss']
})
export class PageRowCountSelectorComponent implements OnInit {

  @Input() rowCountPerPageOptions;
  @Input() rowCountPerPage;
  @Output() rowCountPerPageChange: EventEmitter<number> = new EventEmitter<number>();

  constructor() { }

  ngOnInit() {
  }

  shouldDisplayRowCountPerPageSelector() {
    return this.rowCountPerPageOptions && this.rowCountPerPageOptions.length > 1;
  }

  onOptionChange(rowCountPerPage: any) {
    this.rowCountPerPage = rowCountPerPage;
    this.rowCountPerPageChange.emit(rowCountPerPage);
  }
}
