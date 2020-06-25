import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasoningBranchBrowserComponent } from './reasoning-branch-browser.component';

describe('ReasoningBranchBrowserComponent', () => {
  let component: ReasoningBranchBrowserComponent;
  let fixture: ComponentFixture<ReasoningBranchBrowserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ReasoningBranchBrowserComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasoningBranchBrowserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
