import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EntityPbDTO } from '../interface/dto/entity-pb-dto'; // Importe seus modelos
import { EntityPB } from '../interface/entity-pb';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root',
})
export class EntityPbService {
  private baseUrl = `${environment.apiUrl}/admin` // Prefixo base da API para este service

  constructor(private http: HttpClient) {}

  postEntity(entityPbDTO: EntityPbDTO): Observable<void> {

    const token = sessionStorage.getItem("authToken") || '';
    console.log(token)
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    const url = `${this.baseUrl}/post/entity`;
    return this.http.post<void>(url, entityPbDTO, {headers});
  }

  getEntity(ecode: string): Observable<EntityPB> {
    const url = `${this.baseUrl}/get/entity/${ecode}`;
    return this.http.get<EntityPB>(url);
  }

  deleteEntity(ecode: string): Observable<void> {
    const url = `${this.baseUrl}/delete/entity/${ecode}`; 
    return this.http.delete<void>(url);
  }

  putEntity(entityPbDTO: EntityPbDTO, ecodeAnt: string): Observable<void> {
    const url = `${this.baseUrl}/put/entity?ecodeAnt=${ecodeAnt}`;
    return this.http.put<void>(url, entityPbDTO);
  }

  getAllEntity(): Observable<Array<EntityPB>>{

    let headers = new HttpHeaders

    if (typeof window !== 'undefined') {
      const token = sessionStorage.getItem("authToken");
      if (token) {
        headers = headers.set('Authorization', 'Bearer ' + token);
      }
    }
    const url = `${this.baseUrl}/get/entities`;
    return this.http.get<Array<EntityPB>>(url, {headers});

  }
}
