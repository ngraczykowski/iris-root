import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DynamicHeightComponent } from './dynamic-height.component';

describe('DynamicHeightComponent', () => {
  let component: DynamicHeightComponent;
  let fixture: ComponentFixture<DynamicHeightComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DynamicHeightComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DynamicHeightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
