import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AlertsRestrictionsTableModule } from '@app/templates/alert-restrictions-management/alerts-restrictions-table/alerts-restrictions-table.module';
import { TestModule } from '@app/test/test.module';

import { AlertsRestrictionsTableComponent } from './alerts-restrictions-table.component';

describe('AlertsRestrictionsTableComponent', () => {
  let component: AlertsRestrictionsTableComponent;
  let fixture: ComponentFixture<AlertsRestrictionsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TestModule,
        AlertsRestrictionsTableModule
      ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertsRestrictionsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
