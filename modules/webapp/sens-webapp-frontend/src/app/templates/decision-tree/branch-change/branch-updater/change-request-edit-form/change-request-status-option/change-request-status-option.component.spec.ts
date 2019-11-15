import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeRequestEditFormModule } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.module';
import { TestModule } from '@app/test/test.module';

import { ChangeRequestStatusOptionComponent } from './change-request-status-option.component';

describe('ChangeRequestStatusOptionComponent', () => {
  let component: ChangeRequestStatusOptionComponent;
  let fixture: ComponentFixture<ChangeRequestStatusOptionComponent>;

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
    fixture = TestBed.createComponent(ChangeRequestStatusOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
