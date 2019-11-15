import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { WorkflowManagementComponent } from './workflow-management.component';
import { WorkflowManagementModule } from './workflow-management.module';

describe('WorkflowManagementComponent', () => {
  let component: WorkflowManagementComponent;
  let fixture: ComponentFixture<WorkflowManagementComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowManagementModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
