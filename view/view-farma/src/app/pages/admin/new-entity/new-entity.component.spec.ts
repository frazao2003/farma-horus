import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewEntityComponent } from './new-entity.component';

describe('NewEntityComponent', () => {
  let component: NewEntityComponent;
  let fixture: ComponentFixture<NewEntityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewEntityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewEntityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
