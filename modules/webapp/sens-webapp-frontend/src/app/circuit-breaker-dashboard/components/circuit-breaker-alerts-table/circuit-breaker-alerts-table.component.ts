import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DiscrepanciesList } from '@app/circuit-breaker-dashboard/models/circuit-breaker';
import { environment } from '@env/environment';

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

  translatePrefix = 'circuitBreakerDashboard.discrepancies.dataLabels.';

  dateFormatting = environment.dateFormatting;

  constructor() { }

  ngOnChanges(changes: SimpleChanges) {
    this.dataSource = new MatTableDataSource(this.discrepanciesList);
  }
}
