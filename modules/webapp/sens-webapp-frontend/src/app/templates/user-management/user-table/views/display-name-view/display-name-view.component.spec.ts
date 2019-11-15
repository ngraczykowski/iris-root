import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { DisplayNameViewComponent, DisplayNameViewData } from './display-name-view.component';


describe('TextViewComponent', () => {
  let component: DisplayNameViewComponent;
  let fixture: ComponentFixture<DisplayNameViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule],
          declarations: [DisplayNameViewComponent]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayNameViewComponent);
    component = fixture.componentInstance;
    component.data = <DisplayNameViewData> {};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
