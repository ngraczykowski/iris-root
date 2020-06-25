import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { PendingChange } from '@app/pending-changes/models/pending-changes';
import { environment } from '@env/environment';

@Component({
  selector: 'app-pending-changes-table',
  templateUrl: './pending-changes-table.component.html'
})
export class PendingChangesTableComponent implements OnInit {

  @Input() pendingChangesList: PendingChange[] = [];
  @Output() selectedCR = new EventEmitter<any>();

  highlightRow: number;

  displayedColumns: string[] = ['id', 'aiSolution', 'active', 'createdAt', 'createdBy'];
  dataSource: MatTableDataSource<PendingChange>;

  translatePrefix = 'pendingChanges.';
  translateTablePrefix = this.translatePrefix + 'dataLabels.';

  dateFormatting = environment.dateFormatting;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor() { }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.pendingChangesList);
    this.dataSource.sort = this.sort;
  }

  selectRow(row) {
    this.selectedCR.emit(row);
    this.highlightRow = row.id;
  }
}
