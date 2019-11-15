import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { SimpleViewComponent, SimpleViewData } from './simple-view.component';

describe('SimpleViewComponent', () => {
  let component: SimpleViewComponent;
  let fixture: ComponentFixture<SimpleViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SimpleViewComponent],
      imports: [TestModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    component = new SimpleViewComponent();
    fixture = TestBed.createComponent(SimpleViewComponent);
    component = fixture.componentInstance;
    component.data = <SimpleViewData> {
      className: '',
      text: 'text'
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
