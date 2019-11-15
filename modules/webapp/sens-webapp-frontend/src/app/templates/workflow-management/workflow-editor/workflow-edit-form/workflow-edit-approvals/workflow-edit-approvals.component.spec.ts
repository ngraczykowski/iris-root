import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { WorkflowDetails } from '@model/workflow.model';
import { WorkflowEditFormModule } from '../workflow-edit-form.module';
import { WorkflowEditFormService } from '../workflow-edit-form.service';
import { WorkflowEditApprovalsComponent } from './workflow-edit-approvals.component';

describe('WorkflowEditApprovalsComponent', () => {
  let component: WorkflowEditApprovalsComponent;
  let fixture: ComponentFixture<WorkflowEditApprovalsComponent>;

  let formService: WorkflowEditFormService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowEditFormModule
          ]
        })
        .compileComponents();

    formService = TestBed.get(WorkflowEditFormService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowEditApprovalsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    formService.init(<WorkflowDetails> {
      decisionTreeId: 1,
      makers: [],
      approvalLevels: []
    });

    fixture.detectChanges();

    expect(component).toBeTruthy();
  });
});
