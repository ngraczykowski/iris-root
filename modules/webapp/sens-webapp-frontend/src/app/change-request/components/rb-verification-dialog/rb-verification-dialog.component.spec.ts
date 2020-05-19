import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RbVerificationDialogComponent } from './rb-verification-dialog.component';

describe('RbVerificationDialogComponent', () => {
  let component: RbVerificationDialogComponent;
  let fixture: ComponentFixture<RbVerificationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RbVerificationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RbVerificationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
