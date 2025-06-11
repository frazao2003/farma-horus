import { TestBed } from '@angular/core/testing';

import { InProductService } from './in-product.service';

describe('InProductService', () => {
  let service: InProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InProductService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
