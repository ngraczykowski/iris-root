import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WarningsPageComponent } from './warnings-page.component';

describe('WarningsPageComponent', () => {
  let component: WarningsPageComponent;
  let fixture: ComponentFixture<WarningsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WarningsPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WarningsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
