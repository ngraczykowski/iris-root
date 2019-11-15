import { NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { WorkflowEditFormService } from '@app/templates/workflow-management/workflow-editor/workflow-edit-form/workflow-edit-form.service';
import { WorkflowEditMakersComponent } from './workflow-edit-makers.component';

class WorkflowEditFormServiceMock {
  getMakersControl() {
    return ['userName'];
  }
  updateMakers(newMakersList) {}
}

describe('WorkflowEditMakersComponent', () => {
  let component: WorkflowEditMakersComponent;
  let fixture: ComponentFixture<WorkflowEditMakersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkflowEditMakersComponent],
      providers: [{
        provide: WorkflowEditFormService,
        useClass: WorkflowEditFormServiceMock,
      }],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowEditMakersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
