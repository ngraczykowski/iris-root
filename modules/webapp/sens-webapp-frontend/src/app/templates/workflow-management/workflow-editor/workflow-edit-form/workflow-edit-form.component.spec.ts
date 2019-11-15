import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../test/test.module';
import { WorkflowDetails } from '../../../model/workflow.model';

import { WorkflowEditFormComponent } from './workflow-edit-form.component';
import { WorkflowEditFormModule } from './workflow-edit-form.module';
import { WorkflowEditFormService } from './workflow-edit-form.service';

describe('WorkflowEditFormComponent', () => {
  let component: WorkflowEditFormComponent;
  let fixture: ComponentFixture<WorkflowEditFormComponent>;

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
    fixture = TestBed.createComponent(WorkflowEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init form on change workflow', () => {
    spyOn(formService, 'init');
    const workflow = <WorkflowDetails> {};

    component.workflow = workflow;

    expect(formService.init).toHaveBeenCalledWith(workflow);
  });
});
