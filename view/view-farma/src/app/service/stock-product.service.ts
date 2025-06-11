import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TenantService } from './tenant.service';
import { StockProduct } from '../interface/stock-product';
import { EntryRequestDTO } from '../interface/dto/entry-request-dto';
import { OutPutRequestDTO } from '../interface/dto/output-request-dto';

@Injectable({
  providedIn: 'root'
})
export class StockProductService {

  private baseUrl = `${environment.apiUrl}/api`;
  
  constructor(private http: HttpClient, private tenantService: TenantService) { }


  getAllBySector(id: String){
    console.log(id)
        const tenant = this.tenantService.getTenantFromUrl()
        const url = `${this.baseUrl}/${tenant}/stock/products/stock/${id}`
        const token = sessionStorage.getItem("authToken") || '';
            const headers = new HttpHeaders({
              'Authorization': `Bearer ${token}`
            });
        return this.http.get<StockProduct[]>(url, {headers})
  }

  entry(entryRequest: EntryRequestDTO){
    const tenant = this.tenantService.getTenantFromUrl()
        const id = sessionStorage.getItem('setorAtual')
        const url = `${this.baseUrl}/${tenant}/stock/products/post/${id}`
        const token = sessionStorage.getItem("authToken") || '';
            const headers = new HttpHeaders({
              'Authorization': `Bearer ${token}`
            });
        return this.http.post<StockProduct[]>(url,entryRequest, {headers})
  }

  output(outputRequest: OutPutRequestDTO){
    const tenant = this.tenantService.getTenantFromUrl()
        const id = sessionStorage.getItem('setorAtual')
        const url = `${this.baseUrl}/${tenant}/stock/products/post/${id}`
        const token = sessionStorage.getItem("authToken") || '';
            const headers = new HttpHeaders({
              'Authorization': `Bearer ${token}`
            });
        return this.http.post<StockProduct[]>(url,outputRequest, {headers})
  }
}

