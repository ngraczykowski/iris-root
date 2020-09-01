import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldCopySuffixComponent } from './field-copy-suffix.component';

describe('FieldCopySuffixComponent', () => {
  let component: FieldCopySuffixComponent;
  let fixture: ComponentFixture<FieldCopySuffixComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FieldCopySuffixComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldCopySuffixComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
