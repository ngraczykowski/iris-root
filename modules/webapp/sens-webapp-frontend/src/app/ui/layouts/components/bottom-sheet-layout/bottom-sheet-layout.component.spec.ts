import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BottomSheetLayoutComponent } from './bottom-sheet-layout.component';

describe('BottomSheetLayoutComponent', () => {
  let component: BottomSheetLayoutComponent;
  let fixture: ComponentFixture<BottomSheetLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BottomSheetLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BottomSheetLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
