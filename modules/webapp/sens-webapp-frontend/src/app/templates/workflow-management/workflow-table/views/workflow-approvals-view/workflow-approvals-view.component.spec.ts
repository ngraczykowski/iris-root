import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';
import { WorkflowViewModule } from '../workflow-view.module';
import { WorkflowApprovalsViewComponent } from './workflow-approvals-view.component';

describe('WorkflowApprovalsViewComponent', () => {
  let component: WorkflowApprovalsViewComponent;
  let fixture: ComponentFixture<WorkflowApprovalsViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowViewModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowApprovalsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
