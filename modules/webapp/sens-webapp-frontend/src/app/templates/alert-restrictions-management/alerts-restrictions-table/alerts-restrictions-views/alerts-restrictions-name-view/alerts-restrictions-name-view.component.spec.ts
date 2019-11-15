import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';

import { AlertsRestrictionsNameViewComponent } from './alerts-restrictions-name-view.component';

describe('AlertsRestrictionsNameViewComponent', () => {
  let component: AlertsRestrictionsNameViewComponent;
  let fixture: ComponentFixture<AlertsRestrictionsNameViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlertsRestrictionsNameViewComponent ],
      imports: [
        TestModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertsRestrictionsNameViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
