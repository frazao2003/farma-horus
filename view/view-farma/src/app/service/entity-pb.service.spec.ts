import { TestBed } from '@angular/core/testing';

import { EntityPbService } from './entity-pb.service';

describe('AdminService', () => {
  let service: EntityPbService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EntityPbService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
