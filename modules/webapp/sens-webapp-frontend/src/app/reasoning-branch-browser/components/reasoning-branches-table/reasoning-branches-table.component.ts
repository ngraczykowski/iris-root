import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';

export interface ReasoningBranch {
  solution: string;
  id: string;
  status: string;
  changeRequest: string;
  lastUpdate: string;
}

const reasoningBranches: ReasoningBranch[] = [
  {
    id: '1-1',
    solution: 'False Positive',
    status: 'Active',
    changeRequest: 'No',
    lastUpdate: '0000-00-00 00:00:00'
  },
  {
    id: '1-2',
    solution: 'Potential True Positive',
    status: 'Disabled',
    changeRequest: 'CR-123',
    lastUpdate: '0000-00-00 00:00:00'
  },
  {
    id: '1-3',
    solution: 'No Decision',
    status: 'Active',
    changeRequest: 'No',
    lastUpdate: '0000-00-00 00:00:00'
  }
];

@Component({
  selector: 'app-reasoning-branches-table',
  templateUrl: './reasoning-branches-table.component.html',
  styleUrls: ['./reasoning-branches-table.component.scss']
})
export class ReasoningBranchesTableComponent implements OnInit {

  translatePrefix = 'reasoningBranchBrowser.labels.';

  displayedColumns: string[] = ['select', 'id', 'solution', 'status', 'changeRequest', 'lastUpdate'];
  dataSource = new MatTableDataSource<ReasoningBranch>(reasoningBranches);
  selection = new SelectionModel<ReasoningBranch>(true, []);

  constructor() { }

  ngOnInit() {
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
  checkboxLabel(row?: ReasoningBranch): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }
}
