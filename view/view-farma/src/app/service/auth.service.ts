import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthenticationDTO} from '../interface/dto/authentication-dto'; // Importe seus DTOs de autenticação
import { environment } from '../../environments/environments';
import { LoginResponseDto } from '../interface/dto/login-response-dto';
import { RegisterDTO } from '../interface/dto/register-dto';
import { TenantService } from './tenant.service';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private baseUrl = `${environment.apiUrl}/api`; // Prefixo base da API (sem o tenant diretamente aqui)


  constructor(private http: HttpClient,private tenantService: TenantService) {}

  login(authenticationDTO: AuthenticationDTO): Observable<LoginResponseDto> {
    // O interceptor cuidará de adicionar o tenant na URL
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/auth/login`;
    return this.http.post<LoginResponseDto>(url, authenticationDTO).pipe(
      tap((response: LoginResponseDto) => {
        // Salve o token no localStorage ou sessionStorage
        if (response.token && response.sectorID) { // Supondo que o token está na propriedade 'token'
          sessionStorage.setItem('authToken', response.token);
          sessionStorage.setItem('cnes', JSON.stringify(response.sectorID));

          if (!localStorage.getItem('setorAtual')) {
            if (response?.sectorID?.length > 0) {
              localStorage.setItem('setorAtual', response.sectorID[0]);
            }
          }
        }
      })
    );
  }

  register(registerDTO: RegisterDTO): Observable<void> {
    // O interceptor cuidará de adicionar o tenant na URL
    const tenant = this.tenantService.getTenantFromUrl()
    const url = `${this.baseUrl}/${tenant}/auth/register`;
    return this.http.post<void>(url, registerDTO);
  }
}
