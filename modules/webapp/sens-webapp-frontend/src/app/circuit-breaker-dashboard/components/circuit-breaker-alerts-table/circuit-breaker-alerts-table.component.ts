import {
  Component,
  Input,
  Output,
  ViewChild,
  EventEmitter,
  SimpleChanges, OnChanges
} from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DiscrepanciesList } from '@app/circuit-breaker-dashboard/models/circuit-breaker';

@Component({
  selector: 'app-circuit-breaker-alerts-table',
  templateUrl: './circuit-breaker-alerts-table.component.html'
})
export class CircuitBreakerAlertsTableComponent implements OnChanges {

  @Input() discrepanciesList: DiscrepanciesList[] = [];

  @Output() page: EventEmitter<PageEvent> = new EventEmitter();

  displayedColumns: string[] = [
    'alertId',
    'aiComment',
    'aiCommentDate',
    'analystComment',
    'analystCommentDate'
  ];
  dataSource: MatTableDataSource<DiscrepanciesList>;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  translatePrefix = 'circuitBreakerDashboard.element.dataLabels.';

  constructor() { }

  ngOnChanges(changes: SimpleChanges) {
    this.dataSource = new MatTableDataSource(this.discrepanciesList);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}
