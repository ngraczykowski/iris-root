import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CircuitBreakerListComponent } from './circuit-breaker-list.component';

describe('CircuitBreakerListComponent', () => {
  let component: CircuitBreakerListComponent;
  let fixture: ComponentFixture<CircuitBreakerListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CircuitBreakerListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CircuitBreakerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
