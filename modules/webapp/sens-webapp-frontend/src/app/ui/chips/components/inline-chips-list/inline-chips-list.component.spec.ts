import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InlineChipsListComponent } from './inline-chips-list.component';

describe('InlineChipsListComponent', () => {
  let component: InlineChipsListComponent;
  let fixture: ComponentFixture<InlineChipsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InlineChipsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InlineChipsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
