import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Workflow } from '../../../../model/workflow.model';
import { WorkflowService } from '../../../workflow-service/workflow.service';
import { WorkflowViewModule } from '../workflow-view.module';
import { WorkflowEditViewComponent, WorkflowEditViewData } from './workflow-edit-view.component';

describe('WorkflowEditViewComponent', () => {
  let component: WorkflowEditViewComponent;
  let fixture: ComponentFixture<WorkflowEditViewComponent>;

  let service: WorkflowService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            WorkflowViewModule
          ]
        })
        .compileComponents();

    service = TestBed.get(WorkflowService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowEditViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should edit workflow when invoke onEdit', () => {
    component.data = <WorkflowEditViewData> {
      workflow: <Workflow> {}
    };
    spyOn(service, 'edit');

    component.onEdit();

    expect(service.edit).toHaveBeenCalledWith(component.data.workflow);
  });
});
