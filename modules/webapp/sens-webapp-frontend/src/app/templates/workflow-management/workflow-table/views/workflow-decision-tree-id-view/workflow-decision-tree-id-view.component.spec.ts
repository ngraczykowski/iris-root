import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { WorkflowDecisionTreeIdViewComponent } from './workflow-decision-tree-id-view.component';

describe('WorkflowDecisionTreeIdViewComponent', () => {
  let component: WorkflowDecisionTreeIdViewComponent;
  let fixture: ComponentFixture<WorkflowDecisionTreeIdViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [WorkflowDecisionTreeIdViewComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowDecisionTreeIdViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
