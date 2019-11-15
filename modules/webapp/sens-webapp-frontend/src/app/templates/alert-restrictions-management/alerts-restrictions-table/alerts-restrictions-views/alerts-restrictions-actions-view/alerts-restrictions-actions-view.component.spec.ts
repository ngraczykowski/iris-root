import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';

import { AlertsRestrictionsActionsViewComponent } from './alerts-restrictions-actions-view.component';

describe('AlertsRestrictionsActionsViewComponent', () => {
  let component: AlertsRestrictionsActionsViewComponent;
  let fixture: ComponentFixture<AlertsRestrictionsActionsViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlertsRestrictionsActionsViewComponent ],
      imports: [
        TestModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertsRestrictionsActionsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
