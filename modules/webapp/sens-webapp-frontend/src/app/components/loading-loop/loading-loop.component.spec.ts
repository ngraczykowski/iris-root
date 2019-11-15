import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadingLoopComponent } from './loading-loop.component';

describe('LoadingLoopComponent', () => {
  let component: LoadingLoopComponent;
  let fixture: ComponentFixture<LoadingLoopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LoadingLoopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadingLoopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
