import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TenantService } from './tenant.service';
import { InProduct } from '../interface/in-product';
import { InProductDTO } from '../interface/dto/in-product-dto';

@Injectable({
  providedIn: 'root'
})
export class InProductService {

  private baseUrl = `${environment.apiUrl}/api` // Prefixo base da API para este service

  constructor(private http: HttpClient, private tenantService: TenantService) { }


  getAll() {
    const tenant = this.tenantService.getTenantFromUrl()
    console.log("ecode 2" + tenant)
    const url = `${this.baseUrl}/${tenant}/products/get/all`
    const token = sessionStorage.getItem("authToken") || '';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<InProduct[]>(url, { headers })
  }

  getByID(id: string) {
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/products/get/${id}`
    const token = sessionStorage.getItem("authToken") || '';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<InProduct>(url, { headers })
  }

  postInProduct(inProduct: InProductDTO) {
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/products/post`
    const token = sessionStorage.getItem("authToken") || '';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.post<void>(url,inProduct, { headers })

  }
}
