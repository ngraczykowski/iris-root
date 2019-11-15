import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { TranslateService } from '@ngx-translate/core';
import { asyncScheduler, EMPTY, of, throwError } from 'rxjs';
import { finalize, tap } from 'rxjs/operators';
import { Event, EventKey } from '../../../shared/event/event.service.model';
import { LocalEventService } from '../../../shared/event/local-event.service';
import { TestModule } from '../../../test/test.module';
import { Workflow, WorkflowChange, WorkflowDetails } from '../../model/workflow.model';
import { WorkflowService } from '../workflow-service/workflow.service';
import { WorkflowEditFormService } from './workflow-edit-form/workflow-edit-form.service';
import { WorkflowEditorComponent } from './workflow-editor.component';
import { WorkflowEditorModule } from './workflow-editor.module';

describe('WorkflowEditorComponent', () => {
  let component: WorkflowEditorComponent;
  let fixture: ComponentFixture<WorkflowEditorComponent>;

  let formService: WorkflowEditFormService;
  let workflowService: WorkflowService;
  let translateService: TranslateService;

  function mockTranslateService(translate: TranslateService) {
    spyOn(translate, 'get').and.callFake((key, value) => {
      if (key === 'user-management.data.roles.ROLE_MAKER') {
        return of('Maker');
      }

      if (key === 'workflowManagement.workflowEditor.error.save.USER_ROLE_CHECK_FAILED' &&
          value.users[0] === 'test' &&
          value.role === 'Maker') {
        return of('Some users (test) do not have the Maker authority.');
      }

      if (key === 'workflowManagement.workflowEditor.error.save.USER_NOT_FOUND' &&
          value.users[0] === 'test') {
        return of('Users (test) were not found.');
      }

      return of(key);
    });
  }

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowEditorModule
          ]
        })
        .compileComponents();

    formService = TestBed.get(WorkflowEditFormService);
    workflowService = TestBed.get(WorkflowService);
    translateService = TestBed.get(TranslateService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    mockTranslateService(translateService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Show modal test', () => {

    it('should show = true on edit event', () => {
      spyOn(workflowService, 'getWorkflowDetails').and.returnValue(EMPTY);

      workflowService.edit(<Workflow> {});

      expect(component.show).toBeTruthy();
    });

    it('should show = false on close', () => {
      component.show = true;

      component.onClose();

      expect(component.show).toBeFalsy();
    });

    it('should show = false on success save', () => {
      spyOn(formService, 'createWorkflowChange');
      spyOn(workflowService, 'saveWorkflow').and.returnValue(of({}));

      component.onSave();

      expect(component.show).toBeFalsy();
    });
  });

  describe('Loading test', () => {

    it('should load workflow on edit event', () => {
      const workflow = <Workflow> {decisionTreeId: 1};
      const workflowDetails = <WorkflowDetails> {decisionTreeId: 1};
      spyOn(workflowService, 'getWorkflowDetails').and.returnValue(of(workflowDetails));

      workflowService.edit(workflow);

      expect(component.workflow).toEqual(workflowDetails);
    });

    it('should set loading inProgress while loading', fakeAsync(() => {
      spyOn(workflowService, 'getWorkflowDetails')
          .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));

      workflowService.edit(<Workflow>{});

      expect(component.loadInProgress).toBeTruthy();
      tick(50);
      expect(component.loadInProgress).toBeTruthy();
      tick(50);
      expect(component.loadInProgress).toBeFalsy();
    }));

    it('should set loading error when service got error', () => {
      spyOn(workflowService, 'getWorkflowDetails').and.returnValue(throwError({}));

      workflowService.edit(<Workflow>{});

      expect(component.loadErrorMessage)
          .toEqual('workflowManagement.workflowEditor.error.load.UNKNOWN');
    });

    it('should reset loading error when success', () => {
      component.loadErrorMessage = 'error';
      spyOn(workflowService, 'getWorkflowDetails').and.returnValue(of({}));

      workflowService.edit(<Workflow>{});

      expect(component.loadErrorMessage).toBeFalsy();
    });
  });

  describe('Saving test', () => {

    let eventService: LocalEventService;

    beforeEach(() => {
      eventService = TestBed.get(LocalEventService);

      const workflowDetails = <WorkflowDetails> {
        decisionTreeId: 1,
        makers: ['maker'],
        approvalLevels: [{
          approvers: ['approver']
        }]
      };
      spyOn(workflowService, 'getWorkflowDetails').and.returnValue(of(workflowDetails));
      workflowService.edit(<Workflow> {decisionTreeId: 1});
      fixture.detectChanges();
    });

    afterEach(() => {
      fixture.detectChanges();
    });

    it('should save workflow on save', done => {
      spyOn(workflowService, 'saveWorkflow').and.returnValue(
          of({}).pipe(finalize(() => done()))
      );

      component.onSave();

      expect(workflowService.saveWorkflow)
          .toHaveBeenCalledWith(<WorkflowChange> {
            decisionTreeId: component.workflow.decisionTreeId,
            makers: component.workflow.makers,
            approvalLevels: component.workflow.approvalLevels
          });
    });

    it('should send success notification on save success', () => {
      spyOn(eventService, 'sendEvent');
      spyOn(workflowService, 'saveWorkflow').and.returnValue(of({}));

      component.onSave();

      expect(eventService.sendEvent).toHaveBeenCalledWith(<Event>{
        key: EventKey.NOTIFICATION,
        data: {
          type: 'success',
          message: 'workflowManagement.workflowEditor.notification.save.success'
        }
      });
    });

    it('should set save inProgress while saving', fakeAsync(() => {
      spyOn(workflowService, 'saveWorkflow')
          .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));

      component.onSave();

      expect(component.saveInProgress).toBeTruthy();
      tick(50);
      expect(component.saveInProgress).toBeTruthy();
      tick(50);
      expect(component.saveInProgress).toBeFalsy();
    }));

    it('should set saving error when service got error', () => {
      spyOn(workflowService, 'saveWorkflow').and.returnValue(throwError({}));

      component.onSave();

      expect(component.saveErrorMessage)
          .toEqual('workflowManagement.workflowEditor.error.save.UNKNOWN');
    });

    it('should reset saving error when success', () => {
      component.saveErrorMessage = 'error';
      spyOn(workflowService, 'saveWorkflow').and.returnValue(of({}));

      component.onSave();

      expect(component.saveErrorMessage).toBeFalsy();
    });

    it('should set error message when UserRoleCheckFailedException', () => {
      component.saveErrorMessage = null;
      spyOn(workflowService, 'saveWorkflow').and.returnValue(throwError({
        error: {
          key: 'UserRoleCheckFailedException',
          extras: {
            'role': 'ROLE_MAKER',
            'users': ['test']
        }}}));

      component.onSave();

      expect(component.saveErrorMessage).toEqual(
          'Some users (test) do not have the Maker authority.');
    });

    it('should set error message when InvalidMappingResultCountException', () => {
      component.saveErrorMessage = null;
      spyOn(workflowService, 'saveWorkflow').and.returnValue(throwError({
        error: {
          key: 'InvalidMappingResultCountException',
          extras: {'users': ['test']}}
      }));

      component.onSave();

      expect(component.saveErrorMessage).toEqual('Users (test) were not found.');
    });
  });
});
