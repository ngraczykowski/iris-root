import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { WorkflowLabelViewComponent } from './workflow-label-view.component';

describe('WorkflowLabelViewComponent', () => {
  let component: WorkflowLabelViewComponent;
  let fixture: ComponentFixture<WorkflowLabelViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkflowLabelViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowLabelViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
