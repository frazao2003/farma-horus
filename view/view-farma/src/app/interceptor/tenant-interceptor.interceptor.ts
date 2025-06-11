import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { TenantService } from '../service/tenant.service';

@Injectable()
export class TenantInterceptor implements HttpInterceptor {
  constructor(private tenantService: TenantService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const tenantId = this.tenantService.getTenantFromUrl();
    const apiPrefixWithTenant = '/api/';
    const adminPrefix = '/admin/';

    // Aplica a lógica do tenant apenas se a URL começa com /api/
    if (tenantId && request.url.startsWith(apiPrefixWithTenant)) {
      // Extrai a parte da URL após '/api/'
      const apiEndpoint = request.url.substring(apiPrefixWithTenant.length);
      // Constrói a nova URL com o tenant no meio
      const modifiedUrl = `/api/${tenantId}/${apiEndpoint}`;
      const modifiedRequest = request.clone({
        url: modifiedUrl,
      });
      return next.handle(modifiedRequest);
    }

    // Se a URL não começa com /api/ (por exemplo, começa com /admin/),
    // ou se não houver tenantId, a requisição segue sem modificação.
    return next.handle(request);
  }
}