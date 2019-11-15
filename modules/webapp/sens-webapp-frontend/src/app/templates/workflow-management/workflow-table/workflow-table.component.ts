import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { TableData } from '../../../components/dynamic-view-table/dynamic-view-table.component';
import { WorkflowService } from '../workflow-service/workflow.service';
import { WorkflowTableDataFactory } from './workflow-table-data-factory';

@Component({
  selector: 'app-workflow-table',
  templateUrl: './workflow-table.component.html',
  styleUrls: ['./workflow-table.component.scss']
})
export class WorkflowTableComponent implements OnInit, OnDestroy {
  error: boolean;
  inProgress: boolean;
  tableData: TableData;

  private changesSubscription: Subscription;
  private loadSubscription: Subscription;

  constructor(private workflowService: WorkflowService) { }

  ngOnInit() {
    this.load();
    this.changesSubscription = this.workflowService.observeWorkflowChangeEvents()
        .subscribe(() => this.load());
  }

  ngOnDestroy() {
    this.cancelLoadTask();
    this.cancelListenChangesTask();
  }

  private load() {
    this.cancelLoadTask();
    this.inProgress = true;
    this.loadSubscription = this.workflowService.getWorkflows()
        .pipe(
            map(workflows => WorkflowTableDataFactory.create(workflows)),
            finalize(() => this.inProgress = false)
        )
        .subscribe(
            tableData => this.onLoadSuccess(tableData),
            error => this.onLoadError(error)
        );
  }

  private onLoadSuccess(tableData) {
    this.error = null;
    this.tableData = tableData;
  }

  private onLoadError(error) {
    this.error = error;
  }

  private cancelListenChangesTask() {
    if (this.changesSubscription) {
      this.changesSubscription.unsubscribe();
    }
  }

  private cancelLoadTask() {
    if (this.loadSubscription) {
      this.loadSubscription.unsubscribe();
    }
  }
}
