import { Injectable } from '@angular/core';

import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class TenantService {
  constructor(private route: ActivatedRoute, private router: Router) {}

  getTenantFromUrl(): string | null {
    // Exemplo: Tenant como o primeiro segmento do path
    const urlParts = this.router.url.split('/');
    if (urlParts.length > 1 && urlParts[1] !== '') {
      return urlParts[1];
    }
    return null;
  }
}
