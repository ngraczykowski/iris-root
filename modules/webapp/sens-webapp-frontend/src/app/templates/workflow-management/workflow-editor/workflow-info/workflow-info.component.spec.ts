import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../test/test.module';
import { WorkflowEditorModule } from '../workflow-editor.module';
import { WorkflowInfoComponent } from './workflow-info.component';

describe('WorkflowInfoComponent', () => {
  let component: WorkflowInfoComponent;
  let fixture: ComponentFixture<WorkflowInfoComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowEditorModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
