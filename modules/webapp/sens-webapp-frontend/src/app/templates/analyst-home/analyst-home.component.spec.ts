import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { AnalystHomeComponent } from './analyst-home.component';
import { AnalystHomeModule } from './analyst-home.module';

describe('AnalystHomeComponent', () => {
  let component: AnalystHomeComponent;
  let fixture: ComponentFixture<AnalystHomeComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, AnalystHomeModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalystHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
