import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { WorkflowTableModule } from '@app/templates/workflow-management/workflow-table/workflow-table.module';
import { TestModule } from '@app/test/test.module';
import { WorkflowMakersViewComponent } from './workflow-makers-view.component';

describe('WorkflowMakersViewComponent', () => {
  let component: WorkflowMakersViewComponent;
  let fixture: ComponentFixture<WorkflowMakersViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowTableModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowMakersViewComponent);
    component = fixture.componentInstance;
    component.data = {makers: []};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show empty state when no makers were assigned to the tree ', () => {
    fixture.detectChanges();

    const emptyStateForMakersListInWorkflowManagement = fixture.debugElement.query(By.css('.empty-state-data'));
    expect(emptyStateForMakersListInWorkflowManagement).toBeTruthy();
  });

  it('should hide empty state when makers were assigned to the tree ', () => {
    component.data = {makers: ['maker1']};
    fixture.detectChanges();

    const emptyStateForMakersListInWorkflowManagement = fixture.debugElement.query(By.css('.empty-state-data'));
    expect(emptyStateForMakersListInWorkflowManagement).toBeFalsy();
  });
});
