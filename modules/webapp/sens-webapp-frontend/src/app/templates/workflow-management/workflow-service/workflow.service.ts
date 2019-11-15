import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Workflow, WorkflowChange, WorkflowDetails } from '../../model/workflow.model';
import { WorkflowClient } from './workflow-client';

export interface WorkflowChangeEvent {
  workflowChange: WorkflowChange;
}

export interface WorkflowEditEvent {
  workflow: Workflow;
}

@Injectable()
export class WorkflowService {

  private workflowChangeSubject: Subject<WorkflowChangeEvent> = new Subject();
  private workflowEditSubject: Subject<WorkflowEditEvent> = new Subject();

  constructor(private client: WorkflowClient) {}

  getWorkflows(): Observable<Workflow[]> {
    return this.client.getWorkflows()
        .pipe(map(response => response.workflows));
  }

  getWorkflowDetails(decisionTreeId: number): Observable<WorkflowDetails> {
    return this.client.getWorkflow(decisionTreeId);
  }

  saveWorkflow(workflowChange: WorkflowChange): Observable<any> {
    return this.client.save(workflowChange)
        .pipe(
            tap(() => this.workflowChangeSubject.next(<WorkflowChangeEvent> {workflowChange: workflowChange}))
        );
  }

  edit(workflow: Workflow) {
    this.workflowEditSubject.next(<WorkflowEditEvent> {workflow: workflow});
  }

  observeWorkflowChangeEvents(): Observable<WorkflowChangeEvent> {
    return this.workflowChangeSubject.asObservable();
  }

  observeWorkflowEditEvents(): Observable<WorkflowEditEvent> {
    return this.workflowEditSubject.asObservable();
  }
}
