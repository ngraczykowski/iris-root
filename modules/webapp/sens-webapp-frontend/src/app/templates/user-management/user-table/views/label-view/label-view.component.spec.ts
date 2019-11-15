import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { LabelViewComponent, LabelViewData } from './label-view.component';

describe('LabelViewComponent', () => {
  let component: LabelViewComponent;
  let fixture: ComponentFixture<LabelViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule],
          declarations: [LabelViewComponent]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LabelViewComponent);
    component = fixture.componentInstance;
    component.data = <LabelViewData> {};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
