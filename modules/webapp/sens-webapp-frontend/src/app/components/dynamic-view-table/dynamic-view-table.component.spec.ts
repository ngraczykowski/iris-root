import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';
import { DynamicViewTableComponent } from './dynamic-view-table.component';

describe('DynamicViewTableComponent', () => {
  let component: DynamicViewTableComponent;
  let fixture: ComponentFixture<DynamicViewTableComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DynamicViewTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
