import { SelectionModel } from '@angular/cdk/collections';
import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ReasoningBranchesList } from '@app/reasoning-branches-browser/model/branches-list';
import { environment } from '@env/environment';

@Component({
  selector: 'app-reasoning-branches-table',
  templateUrl: './reasoning-branches-table.component.html',
  styleUrls: ['./reasoning-branches-table.component.scss']
})
export class ReasoningBranchesTableComponent implements OnChanges {

  @Input() branchesTableData: ReasoningBranchesList[] = [];

  @Output() branchDetailsToPreview = new EventEmitter<any>();
  @Output() selectedReasoningBranches = new EventEmitter<any>();

  translatePrefix = 'reasoningBranchesBrowser.';
  labelsTranslatePrefix = this.translatePrefix + 'labels.';
  valuesTranslatePrefix = this.translatePrefix + 'values.';

  highlightRow: string;

  displayedColumns: string[] = [
    'select',
    'id',
    'solution',
    'status',
    'pendingChanges',
    'lastUpdate'
  ];
  dataSource: MatTableDataSource<ReasoningBranchesList>;
  selection: SelectionModel<ReasoningBranchesList>;
  dateFormatting = environment.dateFormatting;

  constructor() { }

  ngOnChanges() {
    this.dataSource = new MatTableDataSource(this.branchesTableData);
    this.selection = new SelectionModel<ReasoningBranchesList>(true, []);
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
        this.selection.clear() :
        this.dataSource.data.forEach(row => this.selection.select(row));
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: ReasoningBranchesList): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row}`;
  }

  sendBranchDetailsToPreview(row) {
    this.branchDetailsToPreview.emit(row);
    this.highlightRow = row;
  }

  sendSelectedBranchesList() {
    this.selectedReasoningBranches.emit(this.selection.selected);
  }
}
