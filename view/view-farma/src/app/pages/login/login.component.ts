import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EntityPbService } from '../../service/entity-pb.service';
import { EntityPB } from '../../interface/entity-pb';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../../service/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { AuthenticationDTO } from '../../interface/dto/authentication-dto';
import { Router } from '@angular/router';
import { RouteUtils } from '../../../Utils/utils';
import { isPlatformBrowser } from '@angular/common';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  providers: [EntityPbService, AuthenticationService],
})
export class LoginComponent implements OnInit {
  entity: EntityPB | null = null;
  ecode: string | null = null;
  authenticationDTO: AuthenticationDTO = { login: '', password: '' }; // Inicialize o objeto

  constructor(
    private entityPbService: EntityPbService,
    private route: ActivatedRoute,
    private authService: AuthenticationService,
    private router: Router,
    private utils:RouteUtils,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.ecode = params.get('ecode');
    });
    if (isPlatformBrowser(this.platformId)) {
      this.loadEntity();
    }
  }

  loadEntity(): void {
    if(this.ecode){
      this.entityPbService.getEntity(this.ecode).subscribe(
        (entityRecebida: EntityPB | null) => {
          this.entity = entityRecebida;
          if (this.entity) {
            console.log('Entidade Carregada', this.entity);
            if(this.entity.name){
              sessionStorage.setItem('entityName', this.entity.name)
            }
            
          } else {
            console.log('Nenhuma entidade encontrada.');
          }
        },
        (error) => {
          console.log('Error ao carregar a entidade: ', error);
        }
      );
    }else{
      alert('ECODE INVALID');
    }
    
  }

  login(): void {
    if (this.authenticationDTO) {
      this.authService.login(this.authenticationDTO).subscribe(
        (response) => {
          console.log('Login bem-sucedido:');
          if(this.ecode == '999025'){
            this.router.navigate(['999025/home/admin'])
          }else{
            this.router.navigate([`${this.ecode}/home`]);
          }

          // Redirecionar o usuário e/ou salvar o token
        },
        (error) => {
          console.error('Erro no login:', error);
          // Exibir mensagem de erro ao usuário
        }
      );
    } else {
      console.warn('Objeto de autenticação não inicializado.');
    }
  }
}