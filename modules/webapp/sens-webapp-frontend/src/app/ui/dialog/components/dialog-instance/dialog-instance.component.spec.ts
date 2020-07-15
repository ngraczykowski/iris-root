import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogInstanceComponent } from './dialog-instance.component';

describe('DialogInstanceComponent', () => {
  let component: DialogInstanceComponent;
  let fixture: ComponentFixture<DialogInstanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogInstanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogInstanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
