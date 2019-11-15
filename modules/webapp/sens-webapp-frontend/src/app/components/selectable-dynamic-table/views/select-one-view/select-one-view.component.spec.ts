import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectOneViewComponent } from './select-one-view.component';

describe('BranchSelectOneViewComponent', () => {
  let component: SelectOneViewComponent;
  let fixture: ComponentFixture<SelectOneViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          declarations: [SelectOneViewComponent]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectOneViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
