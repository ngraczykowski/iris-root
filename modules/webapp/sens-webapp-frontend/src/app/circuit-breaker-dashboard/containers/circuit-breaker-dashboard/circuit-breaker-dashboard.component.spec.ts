import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CircuitBreakerDashboardComponent } from './circuit-breaker-dashboard.component';

describe('CircuitBreakerDashboardComponent', () => {
  let component: CircuitBreakerDashboardComponent;
  let fixture: ComponentFixture<CircuitBreakerDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CircuitBreakerDashboardComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CircuitBreakerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
