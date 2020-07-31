import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FullScreenLayoutComponent } from './full-screen-layout.component';

describe('FullScreenLayoutComponent', () => {
  let component: FullScreenLayoutComponent;
  let fixture: ComponentFixture<FullScreenLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FullScreenLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FullScreenLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
