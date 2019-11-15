import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AlertRestrictionsManagementModule } from '@app/templates/alert-restrictions-management/alert-restrictions-management.module';
import { TestModule } from '@app/test/test.module';

import { AlertRestrictionsManagementComponent } from './alert-restrictions-management.component';

describe('AlertRestrictionsManagementComponent', () => {
  let component: AlertRestrictionsManagementComponent;
  let fixture: ComponentFixture<AlertRestrictionsManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TestModule,
        AlertRestrictionsManagementModule
      ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertRestrictionsManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
