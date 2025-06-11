import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, EventEmitter, Inject, Input, OnInit, Output, PLATFORM_ID } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Table, TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { ChangeSectorComponent } from '../../../component/change-sector/change-sector.component';
import { SidebarComponent } from '../../../component/sidebar/sidebar.component';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { InProductService } from '../../../service/in-product.service';
import { InProduct } from '../../../interface/in-product';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    SidebarComponent,
    CommonModule,
    HttpClientModule,
    ChangeSectorComponent,
    TableModule,
    ButtonModule,
    InputTextModule,
    TooltipModule,
    RouterModule
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private inProductsService: InProductService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }
  @Input() isModal: boolean = false; // Input para determinar se o componente está agindo como modal
  @Output() productSelected = new EventEmitter<InProduct>();
  @Output() modalClose = new EventEmitter<void>();
  ecode: string | null = null;
  listInProduct: InProduct[] = [];




  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.route.paramMap.subscribe((params) => {
        if (!this.isModal) {
          this.route.paramMap.subscribe((params) => {
            this.ecode = params.get('ecode');
            console.log("ecode" + this.ecode)
          });
        }
      });
    }
    this.getAll();


  }

  getAll() {
    this.inProductsService.getAll().subscribe({
      next: (data) => {
        this.listInProduct = data;
        console.log(data)
      },
      error: (err) => {
        console.error('Failed to fetch entities:', err);
      }
    });
  }

  filtrarGlobal(event: Event, tabela: Table) {
    const valor = (event.target as HTMLInputElement).value;
    tabela.filterGlobal(valor, 'contains');
  }

  // Novo método para selecionar um produto no modo modal
  selectProduct(product: InProduct): void {
    if (this.isModal) {
      this.productSelected.emit(product); // Emite o produto selecionado
      this.closeModal(); // Fecha o modal após a seleção
    }
  }

  // Novo método para fechar o modal
  closeModal(): void {
    if (this.isModal) {
      this.modalClose.emit(); // Emite um evento de fechamento
    }
  }

}
