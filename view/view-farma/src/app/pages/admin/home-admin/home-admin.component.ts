import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { EntityPbService } from '../../../service/entity-pb.service';
import { HttpClientModule } from '@angular/common/http';


@Component({
  selector: 'app-home-admin',
  standalone: true,
  imports: [
    CommonModule,
    TableModule, ButtonModule, InputTextModule, TooltipModule,
    HttpClientModule
  ],
  providers: [MessageService],
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.css']
})
export class HomeAdminComponent implements OnInit {
  sidebarTitle: string = 'ADMINISTRADOR';
  sidebarContent: string[] = [
    '<i class="fa-solid fa-pen-to-square"></i> Entidades',
  ];
  constructor( private enittyService: EntityPbService) {}
  
  
  
  ngOnInit(): void {
    this.getAllEntities()
  }


  entities: any[] = [];



  filtrarCampo(event: Event, tabela: any, campo: string) {
    const valor = (event.target as HTMLInputElement).value;
    tabela.filter(valor, campo, 'contains');
  }

  filtrarGlobal(event: Event, tabela: any) {
    const valor = (event.target as HTMLInputElement).value;
    tabela.filterGlobal(valor, 'contains');
  }


  onExcluir(produto: any): void {
    
  }

  getAllEntities(): void {
    this.enittyService.getAllEntity().subscribe({
      next: (data) => {
        this.entities = data;
      },
      error: (err) => {
        console.error('Failed to fetch entities:', err);
      }
    });
  }

} 
