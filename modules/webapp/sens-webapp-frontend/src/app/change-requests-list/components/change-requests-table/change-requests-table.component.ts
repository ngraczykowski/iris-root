import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

export interface ChangeRequest {
  id: string;
  aiSolution: string;
  aiStatus: string;
  requestDate: string;
  author: string;
}

const DATA: ChangeRequest[] = [
  {
    id: 'CR-123',
    aiSolution: 'Potential True Positive',
    aiStatus: 'Active',
    requestDate: '2020-04-12 15:43',
    author: 'Admin'
  },
  {
    id: 'CR-456',
    aiSolution: 'False Positive',
    aiStatus: 'Active',
    requestDate: '2020-04-12 15:43',
    author: 'Admin'
  },
  {
    id: 'CR-789',
    aiSolution: 'No Decision',
    aiStatus: 'Active',
    requestDate: '2020-04-12 15:43',
    author: 'Admin'
  },
  {
    id: 'CR-234',
    aiSolution: 'False Positive',
    aiStatus: 'Active',
    requestDate: '2020-04-12 15:43',
    author: 'Admin'
  },
  {
    id: 'CR-345',
    aiSolution: 'False Positive',
    aiStatus: 'Disabled',
    requestDate: '2020-04-12 15:43',
    author: 'Admin'
  },
];

@Component({
  selector: 'app-change-requests-table',
  templateUrl: './change-requests-table.component.html'
})
export class ChangeRequestsTableComponent implements OnInit {

  displayedColumns: string[] = ['id', 'aiSolution', 'aiStatus', 'requestDate', 'author'];
  dataSource = new MatTableDataSource(DATA);

  translatePrefix = 'changeRequestsList.';
  translateTablePrefix = this.translatePrefix + 'dataLabels.';

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor() { }

  ngOnInit() {
    this.dataSource.sort = this.sort;
  }
}
