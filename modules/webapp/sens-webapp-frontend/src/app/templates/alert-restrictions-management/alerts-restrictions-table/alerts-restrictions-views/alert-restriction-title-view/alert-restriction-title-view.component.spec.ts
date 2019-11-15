import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertRestrictionTitleViewComponent } from './alert-restriction-title-view.component';

describe('AlertRestrictionTitleViewComponent', () => {
  let component: AlertRestrictionTitleViewComponent;
  let fixture: ComponentFixture<AlertRestrictionTitleViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlertRestrictionTitleViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertRestrictionTitleViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
