import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MaintenanceComponent } from '@app/pages/maintenance/maintenance.component';

/**
 * Test excluded as was causing malfunctions in Karma test runner.
 * The tested component is creating 60s timeout in onInitMethod that reloads current page
 * which, as a result, caused Karma main page to reload and run tests multiple times.
 */
xdescribe('MaintenanceComponent', () => {
  let component: MaintenanceComponent;
  let fixture: ComponentFixture<MaintenanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MaintenanceComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
