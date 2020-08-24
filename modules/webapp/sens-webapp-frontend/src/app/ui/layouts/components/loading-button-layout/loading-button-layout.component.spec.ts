import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadingButtonLayoutComponent } from './loading-button-layout.component';

describe('LoadingButtonLayoutComponent', () => {
  let component: LoadingButtonLayoutComponent;
  let fixture: ComponentFixture<LoadingButtonLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadingButtonLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadingButtonLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
