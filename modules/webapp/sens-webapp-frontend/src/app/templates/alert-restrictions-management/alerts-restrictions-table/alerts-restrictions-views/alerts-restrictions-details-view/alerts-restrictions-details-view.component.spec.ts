import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';

import { AlertsRestrictionsDetailsViewComponent } from './alerts-restrictions-details-view.component';

describe('AlertsRestrictionsBatchTypesViewComponent', () => {
  let component: AlertsRestrictionsDetailsViewComponent;
  let fixture: ComponentFixture<AlertsRestrictionsDetailsViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlertsRestrictionsDetailsViewComponent ],
      imports: [
        TestModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertsRestrictionsDetailsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
