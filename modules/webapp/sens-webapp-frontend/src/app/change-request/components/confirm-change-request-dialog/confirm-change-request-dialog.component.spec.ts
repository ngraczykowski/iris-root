import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmChangeRequestDialogComponent } from './confirm-change-request-dialog.component';

describe('ConfirmChangeRequestDialogComponent', () => {
  let component: ConfirmChangeRequestDialogComponent;
  let fixture: ComponentFixture<ConfirmChangeRequestDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmChangeRequestDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmChangeRequestDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
