import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TenantService } from './tenant.service';
import { environment } from '../../environments/environments';
import { Sector } from '../interface/sector';
import { SectorResponseDTO } from '../interface/dto/sector-response-dto';
import { SectorDTO } from '../interface/dto/sector-dto';

@Injectable({
  providedIn: 'root'
})
export class SectorService {

  private baseUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient, private tenantService: TenantService) { }

  getById(id: string) {
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/sector/${id}/get`
    const token = sessionStorage.getItem("authToken") || '';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<SectorResponseDTO>(url, { headers })
  }

  getAllSectors(){
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/sector/getAll`
    const token = sessionStorage.getItem("authToken") || '';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<SectorResponseDTO[]>(url, { headers })
  }

  create(sectorDto: SectorDTO){
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/sector/post`
    const token = sessionStorage.getItem("authToken") || '';
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    console.log(headers)
    return this.http.post<void>(url,sectorDto, { headers })
  }

  update(id: string, newSector: SectorDTO){
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/sector/${id}/put`
    const token = sessionStorage.getItem("authToken") || '';

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.put<void>(url,newSector, { headers })

  }
}
