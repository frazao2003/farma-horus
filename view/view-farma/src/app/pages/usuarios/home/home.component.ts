import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { EntityPB } from '../../../interface/entity-pb';
import { StockProduct } from '../../../interface/stock-product';
import { HttpClientModule } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { SectorResponseDTO } from '../../../interface/dto/sector-response-dto';
import { StockProductService } from '../../../service/stock-product.service';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule, 
    HttpClientModule, 
    TableModule, 
    ButtonModule, 
    InputTextModule, 
    TooltipModule,],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  onExcluir(_t22: any) {
    throw new Error('Method not implemented.');
  }

  ecode: string | null = null;
  entity: EntityPB | null = null;
  stock: StockProduct[] = [];
  sector: SectorResponseDTO | null = null;


  constructor(
    private route: ActivatedRoute, 
    private stockProductService: StockProductService, 
    @Inject(PLATFORM_ID) private platformId: Object) { }


  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.ecode = params.get('ecode');
    });

    if (isPlatformBrowser(this.platformId)) {
      this.getStockProducts();
    }
  }

  getStockProducts() {
    const sectorID = localStorage.getItem('setorAtual');

    if (sectorID) {
      this.stockProductService.getAllBySector(sectorID).subscribe({
        next: (data) => {
          this.stock = data;
        },
        error: (err) => {
          console.error('Failed to fetch entities:', err);
        }
      });
    }
  }


  filtrarGlobal(event: Event, tabela: any) {
    const valor = (event.target as HTMLInputElement).value;
    tabela.filterGlobal(valor, 'contains');
  }

}
