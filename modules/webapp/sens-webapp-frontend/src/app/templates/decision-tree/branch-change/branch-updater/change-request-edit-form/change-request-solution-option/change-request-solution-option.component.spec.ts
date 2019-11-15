import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeRequestEditFormModule } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.module';
import { TestModule } from '@app/test/test.module';

import { ChangeRequestSolutionOptionComponent } from './change-request-solution-option.component';

describe('ChangeRequestSolutionOptionComponent', () => {
  let component: ChangeRequestSolutionOptionComponent;
  let fixture: ComponentFixture<ChangeRequestSolutionOptionComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            ChangeRequestEditFormModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeRequestSolutionOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
