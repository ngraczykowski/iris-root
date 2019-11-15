import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { BriefMessageComponent } from './brief-message.component';

describe('BriefMessageComponent', () => {
  let component: BriefMessageComponent;
  let fixture: ComponentFixture<BriefMessageComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          declarations: [BriefMessageComponent],
          imports: [TestModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BriefMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
