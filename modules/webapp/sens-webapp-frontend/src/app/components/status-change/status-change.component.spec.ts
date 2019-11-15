import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { StatusChangeComponent } from '@app/components/status-change/status-change.component';
import { TestModule } from '@app/test/test.module';

describe('StatusChangeComponent', () => {
  let component: StatusChangeComponent;
  let fixture: ComponentFixture<StatusChangeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
