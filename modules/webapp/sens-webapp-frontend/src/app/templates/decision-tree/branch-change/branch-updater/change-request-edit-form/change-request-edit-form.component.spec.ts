import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeRequestEditFormModule } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-edit-form.module';
import { TestModule } from '@app/test/test.module';

import { ChangeRequestEditFormComponent } from './change-request-edit-form.component';

describe('ChangeRequestEditFormComponent', () => {
  let component: ChangeRequestEditFormComponent;
  let fixture: ComponentFixture<ChangeRequestEditFormComponent>;

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
    fixture = TestBed.createComponent(ChangeRequestEditFormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should not be initialized before init', () => {
    expect(component.isInitialized()).toBeFalsy();
  });

  it('should be initialized after init', () => {
    fixture.detectChanges();

    expect(component.isInitialized()).toBeTruthy();
  });
});
