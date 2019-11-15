import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeRequestRejectFormModule } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-reject-form.module';
import { TestModule } from '@app/test/test.module';

import { ChangeRequestRejectFormComponent } from './change-request-reject-form.component';

describe('ChangeRequestRejectFormComponent', () => {
  let component: ChangeRequestRejectFormComponent;
  let fixture: ComponentFixture<ChangeRequestRejectFormComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            ChangeRequestRejectFormModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeRequestRejectFormComponent);
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
