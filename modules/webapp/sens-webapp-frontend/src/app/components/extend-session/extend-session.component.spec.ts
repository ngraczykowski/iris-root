import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LocalEventService } from '../../shared/event/local-event.service';
import { TestModule } from '../../test/test.module';
import { ExtendSessionComponent } from './extend-session.component';


describe('ExtendSessionComponent', () => {
  let component: ExtendSessionComponent;
  let fixture: ComponentFixture<ExtendSessionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      declarations: [ExtendSessionComponent],
      providers: [LocalEventService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtendSessionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
