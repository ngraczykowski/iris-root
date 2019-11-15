import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectAllViewComponent } from './select-all-view.component';

describe('BranchSelectAllViewComponent', () => {
  let component: SelectAllViewComponent;
  let fixture: ComponentFixture<SelectAllViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          declarations: [SelectAllViewComponent]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectAllViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
