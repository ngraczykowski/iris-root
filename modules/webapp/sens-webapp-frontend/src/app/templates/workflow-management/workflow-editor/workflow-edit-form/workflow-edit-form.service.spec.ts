import { async, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { ApprovalLevel, WorkflowChange, WorkflowDetails } from '@model/workflow.model';
import { WorkflowEditFormModule } from './workflow-edit-form.module';
import { WorkflowEditFormService } from './workflow-edit-form.service';

describe('WorkflowEditFormService', () => {
  let service: WorkflowEditFormService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowEditFormModule
          ]
        })
        .compileComponents();

    service = TestBed.get(WorkflowEditFormService);
  }));

  describe('createWorflowChange() test', () => {

    it('should create valid change', () => {
      service.init(<WorkflowDetails> {
        decisionTreeId: 1,
        decisionTreeName: 'name',
        makers: ['maker1', 'maker2'],
        approvalLevels: [{
          approvers: ['approver1', 'approver2']
        }]
      });

      const change = service.createWorkflowChange();

      expect(change).toEqual(<WorkflowChange> {
        decisionTreeId: 1,
        makers: ['maker1', 'maker2'],
        approvalLevels: [{
          approvers: ['approver1', 'approver2']
        }]
      });
    });
  });

  describe('isInitialized() test', () => {

    it('should isInitialized() return false before init', () => {
      expect(service.isInitialized()).toBeFalsy();
    });

    it('should isInitialized() return true after init', () => {
      service.init(<WorkflowDetails> {
        decisionTreeId: 1,
        decisionTreeName: 'name',
        makers: [],
        approvalLevels: []
      });

      expect(service.isInitialized()).toBeTruthy();
    });
  });

  describe('areApprovalLevelsEnabled() test', () => {

    it('should approval levels be disabled when init without approval levels', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: []
      });

      expect(service.areApprovalLevelsEnabled()).toBeFalsy();
    });

    it('should approval levels be enabled when init with approval levels', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1']}
        ]
      });

      expect(service.areApprovalLevelsEnabled()).toBeTruthy();
    });
  });

  describe('hasApprovalLevels() test', () => {

    it('should service.hasApprovalLevels() return false when there is no approval levels', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: []
      });

      expect(service.hasApprovalLevels()).toBeFalsy();
    });

    it('should service.hasApprovalLevels() return true when there are approval levels', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [<ApprovalLevel> {approvers: ['approver']}]
      });

      expect(service.hasApprovalLevels()).toBeTruthy();
    });
  });

  describe('Edit approval levels test', () => {

    it('should create valid approval level values in input', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1', 'approver2']},
          {approvers: ['approver3', 'approver4']}
        ]
      });

      expect(service.getApprovalLevelForm().at(0).value).toEqual(['approver1', 'approver2']);
      expect(service.getApprovalLevelForm().at(1).value).toEqual(['approver3', 'approver4']);
    });

    it('should create valid approval levels change after edit approvers', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1', 'approver2']},
          {approvers: ['approver3', 'approver4']}
        ]
      });

      service.getApprovalLevelForm().at(1).setValue(['approver5', 'approver6', 'approver7']);
      const change = service.createWorkflowChange();

      expect(service.createWorkflowChange().approvalLevels).toEqual([
        <ApprovalLevel> {approvers: ['approver1', 'approver2']},
        <ApprovalLevel> {approvers: ['approver5', 'approver6', 'approver7']}
      ]);
    });

    it('should remove approval levels after disable approvals', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1']},
          {approvers: ['approver2']}
        ]
      });

      service.getEnableApprovalsControl().setValue(false);

      expect(service.getApprovalLevelForm().length).toEqual(0);
    });

    it('should add approval level after enable approvals', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: []
      });

      service.getEnableApprovalsControl().setValue(true);

      expect(service.getApprovalLevelForm().length).toEqual(1);
    });

    it('should create valid approval levels change after add approval level', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1']}
        ]
      });

      service.addApprovalLevel();
      service.getApprovalLevelForm().at(1).setValue(['approver2']);
      const change = service.createWorkflowChange();

      expect(change.approvalLevels).toEqual([
        {approvers: ['approver1']},
        {approvers: ['approver2']}
      ]);
    });

    it('should create valid approval levels change after remove approval level', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1']},
          {approvers: ['approver2']},
          {approvers: ['approver3']}
        ]
      });

      service.removeApprovalLevel(1);
      const change = service.createWorkflowChange();

      expect(change.approvalLevels).toEqual([
        {approvers: ['approver1']},
        {approvers: ['approver3']}
      ]);
    });
  });

  describe('Dirty test', () => {

    it('should isDirty() return false when init', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1']}
        ]
      });

      expect(service.isDirty()).toBeFalsy();
    });

    it('should isDirty() return true when removed approval level', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1']}
        ]
      });

      service.removeApprovalLevel(0);

      expect(service.isDirty()).toBeTruthy();
    });

    it('should isDirty() return true when add approval level', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver1']},
        ]
      });

      service.addApprovalLevel();

      expect(service.isDirty()).toBeTruthy();
    });
  });

  describe('Validation test', () => {

    it('should makers control allow empty', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: []
      });

      expect(service.getMakersControl().errors).toBeNull();
    });

    it('should approval level control have required error when empty', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver']},
          {approvers: []}
        ]
      });

      expect(service.getApprovalLevelForm().at(1).errors).toEqual({required: true});
    });

    it('should approval level control have error when no name is provided', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: [' , ']}
        ]
      });

      expect(service.getApprovalLevelForm().at(0).errors).not.toEqual(null);
    });

    it('should update errors after change makers value', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: []
      });

      const control = service.getMakersControl();
      control.setValue('maker');

      expect(control.errors).toBeFalsy();
    });

    it('should update errors after change approvers value', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: []}
        ]
      });

      const control = service.getApprovalLevelForm().at(0);
      control.setValue('approver');

      expect(control.errors).toBeFalsy();
    });

    it('should isValid() return true when there is no empty makers and approvers', () => {
      service.init(<WorkflowDetails> {
        makers: ['maker'],
        approvalLevels: [
          {approvers: ['approver']}
        ]
      });

      expect(service.isValid()).toBeTruthy();
    });

    it('should isValid() return true when there is empty makers property', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver']}
        ]
      });

      expect(service.isValid()).toBeTruthy();
    });

    it('should isValid() return false when there is empty one approvers property', () => {
      service.init(<WorkflowDetails> {
        makers: [],
        approvalLevels: [
          {approvers: ['approver']},
          {approvers: []}
        ]
      });

      expect(service.isValid()).toBeFalsy();
    });
  });
});
