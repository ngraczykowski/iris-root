import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { asyncScheduler, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { TableData } from '../../../components/dynamic-view-table/dynamic-view-table.component';
import { TestModule } from '../../../test/test.module';
import { Workflow, WorkflowChange, WorkflowList } from '../../model/workflow.model';
import { WorkflowClient } from '../workflow-service/workflow-client';
import { WorkflowService } from '../workflow-service/workflow.service';
import { WorkflowTableDataFactory } from './workflow-table-data-factory';

import { WorkflowTableComponent } from './workflow-table.component';
import { WorkflowTableModule } from './workflow-table.module';
import Spy = jasmine.Spy;

describe('WorkflowTreesTableComponent', () => {
  let component: WorkflowTableComponent;
  let fixture: ComponentFixture<WorkflowTableComponent>;

  let service: WorkflowService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowTableModule
          ]
        })
        .compileComponents();

    service = TestBed.get(WorkflowService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowTableComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should set inProgress when loading', fakeAsync(() => {
    spyOn(service, 'getWorkflows')
        .and.returnValue(of([], asyncScheduler).pipe(tap(() => tick(100))));

    fixture.detectChanges();

    expect(component.inProgress).toBeTruthy();
    tick(50);
    expect(component.inProgress).toBeTruthy();
    tick(50);
    expect(component.inProgress).toBeFalsy();
  }));

  it('should set error when get data failed', () => {
    spyOn(service, 'getWorkflows').and.returnValue(throwError({}));

    fixture.detectChanges();

    expect(component.error).toBeTruthy();
  });

  describe('Load table data test', () => {
    let clientGetWorkflowsSpy: Spy;

    beforeEach(() => {
      spyOn(WorkflowTableDataFactory, 'create')
          .and.callFake((workflows: Workflow[]) => createWorkflowTestTableData(workflows));
      const client = TestBed.get(WorkflowClient);
      clientGetWorkflowsSpy = spyOn(client, 'getWorkflows');
      spyOn(client, 'save').and.returnValue(of({}));
    });

    it('should load table data on init', () => {
      const workflows = [<Workflow>{
        decisionTreeId: 1,
        decisionTreeName: 'dt1',
        approvalLevels: 2,
        makers: ['maker1']
      }];
      mockWorkflows(workflows);

      fixture.detectChanges();

      assertWorkflowData(workflows);
    });

    it('should refresh workflow on change', () => {
      mockWorkflows([]);
      fixture.detectChanges();

      const workflows = [<Workflow>{
        decisionTreeId: 1,
        decisionTreeName: 'dt1',
        approvalLevels: 2,
        makers: ['maker1']
      }];
      mockWorkflows(workflows);

      service.saveWorkflow(<WorkflowChange> {}).subscribe();

      assertWorkflowData(workflows);
    });

    function mockWorkflows(workflows: Workflow[]) {
      clientGetWorkflowsSpy.and.returnValue(of(<WorkflowList> {workflows: workflows}));
    }

    function assertWorkflowData(workflows: Workflow[]) {
      expect(component.tableData).toEqual(createWorkflowTestTableData(workflows));
    }

    function createWorkflowTestTableData(workflows: Workflow[]): TableData {
      return <TableData> {
        total: 0,
        labels: [],
        rows: [],
        workflows: workflows
      };
    }
  });
});
