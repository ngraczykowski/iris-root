import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SolutionChangeComponent } from '@app/components/solution-change/solution-change.component';
import { TestModule } from '@app/test/test.module';


describe('SolutionChangeComponent', () => {
  let component: SolutionChangeComponent;
  let fixture: ComponentFixture<SolutionChangeComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SolutionChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
