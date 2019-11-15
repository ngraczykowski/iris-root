import { async, TestBed } from '@angular/core/testing';
import { EMPTY, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { TestModule } from '../../../test/test.module';
import {
  Workflow,
  WorkflowChange,
  WorkflowDetails,
  WorkflowList
} from '../../model/workflow.model';
import { WorkflowClient } from './workflow-client';
import { WorkflowServiceModule } from './workflow-service.module';
import { WorkflowChangeEvent, WorkflowEditEvent, WorkflowService } from './workflow.service';

describe('WorkflowService', () => {
  let client: WorkflowClient;
  let service: WorkflowService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowServiceModule
          ]
        })
        .compileComponents();

    client = TestBed.get(WorkflowClient);
    service = TestBed.get(WorkflowService);
  }));

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('should getWorkflowDetails correctly', done => {
    spyOn(client, 'getWorkflow')
        .and.callFake(id => of(<WorkflowDetails> {decisionTreeId: id}));

    service.getWorkflowDetails(2)
        .subscribe(data => {
          expect(data).toEqual(<WorkflowDetails>{decisionTreeId: 2});
          done();
        });
  });

  it('should save workflow correctly', done => {
    const change = <WorkflowChange> {decisionTreeId: 2};
    const obs = of({}).pipe(tap(() => {
      expect(client.save).toHaveBeenCalledWith(change);
      done();
    }));
    spyOn(client, 'save').and.returnValue(obs);

    service.saveWorkflow(change).subscribe();
  });

  it('should send edit event on edit', done => {
    const workflow = <Workflow> {decisionTreeId: 1};
    service.observeWorkflowEditEvents().subscribe(e => {
      expect(e).toEqual(<WorkflowEditEvent> {workflow: workflow});
      done();
    });

    service.edit(workflow);
  });

  it('should send change event on success save', done => {
    const change = <WorkflowChange> {decisionTreeId: 2};
    service.observeWorkflowChangeEvents().subscribe(e => {
      expect(e).toEqual(<WorkflowChangeEvent> {workflowChange: change});
      done();
    });
    spyOn(client, 'save').and.returnValue(of({}));

    service.saveWorkflow(change).subscribe();
  });

  it('should not send event on error save', done => {
    const change = <WorkflowChange> {decisionTreeId: 2};
    let event: WorkflowChangeEvent;
    service.observeWorkflowChangeEvents().subscribe(e => event = e);
    spyOn(client, 'save').and.returnValue(throwError({}));

    service.saveWorkflow(change)
        .pipe(catchError(() => {
          expect(event).toBeUndefined();
          done();
          return EMPTY;
        }))
        .subscribe();
  });

  it('should load valid workflows', done => {
    const workflows = [<Workflow>{decisionTreeId: 1}, <Workflow>{decisionTreeId: 2}];
    spyOn(client, 'getWorkflows').and.returnValue(of(<WorkflowList> {workflows: workflows}));

    service.getWorkflows()
        .pipe(
            tap(data => {
              expect(data).toEqual(workflows);
              done();
            })
        ).subscribe();
  });

  it('should throw error when client throws error', done => {
    const error = {key: 'key'};
    spyOn(client, 'getWorkflows').and.returnValue(throwError(error));

    service.getWorkflows()
        .pipe(
            catchError(e => {
              expect(e).toEqual(error);
              done();
              return EMPTY;
            })
        ).subscribe();
  });
});
