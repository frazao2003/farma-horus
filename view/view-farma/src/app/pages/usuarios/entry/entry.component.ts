import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { StockProductDTO } from '../../../interface/dto/stock-product-dto';
import { InProduct } from '../../../interface/in-product';
import { ProductsComponent } from "../products/products.component";
import { HttpClientModule } from '@angular/common/http';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-entry',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    CommonModule, 
    ProductsComponent,
    HttpClientModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    TooltipModule,
    CalendarModule
  ],
  templateUrl: './entry.component.html',
  styleUrl: './entry.component.css'
})
export class EntryComponent {

  constructor(
    private route: ActivatedRoute,
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
  ) { }

  ecode: string | null = null;
  stockEntryForm!: FormGroup;
  isProductListModalOpen: boolean = false; // Controla a visibilidade do modal de produtos
  selectedProductForEntry: InProduct | null = null; // Armazena o produto selecionado do modal

  // Lista que armazenará os produtos que você quer adicionar ao estoque
  stockProductsList: StockProductDTO[] = [];

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.route.paramMap.subscribe((params) => {
        this.ecode = params.get('ecode');
      });

      this.stockEntryForm = this.fb.group({
        // Campos relacionados ao produto selecionado (do modal)
        idProduct: [null, Validators.required], // ID do produto (vindo do modal)
        productName: [{ value: '', disabled: false }, Validators.required], // Nome do produto para exibição

        // Campos específicos do lote/entrada de estoque
        codLot: ['', Validators.required],
        expiationDate: [null, Validators.required],
        amount: [null, [Validators.required, Validators.min(1)]]
      });
    }
  }

  /**
 * Abre o ProductsComponent como um modal para seleção de produto.
 */
  openProductSelectionModal(): void {
    this.isProductListModalOpen = true;
  }

  /**
   * Chamado quando um produto é selecionado no ProductsComponent modal.
   * @param product O produto InProduct selecionado.
   */
  onProductSelected(product: InProduct): void {
    this.selectedProductForEntry = product; // Armazena o produto completo
    this.stockEntryForm.patchValue({
      idProduct: product.id,
      productName: product.description // Preenche o nome para exibição
    });
    this.isProductListModalOpen = false; // Fecha o modal
    // Opcional: foca no próximo campo (código do lote)
    this.stockEntryForm.get('codLot')?.markAsTouched();
  }

  /**
   * Chamado quando o modal de produtos é fechado (pelo botão 'x' ou clique fora).
   */
  onModalClose(): void {
    this.isProductListModalOpen = false;
  }

  /**
   * Adiciona o produto e seus detalhes de lote à lista temporária.
   */
  addToList(): void {
    // Marca todos os campos como tocados para exibir erros antes de adicionar
    this.stockEntryForm.markAllAsTouched();

    if (this.stockEntryForm.valid) {
      const formValue = this.stockEntryForm.value;

      // Cria um novo objeto StockProductDTO
      const newStockItem: StockProductDTO = {
        codLot: formValue.codLot,
        expiationDate: formValue.expiationDate,
        idProduct: formValue.idProduct,
        amount: formValue.amount
      };

      this.stockProductsList.push(newStockItem); // Adiciona à lista

      console.log('Item adicionado à lista:', newStockItem);
      console.log('Lista atual:', this.stockProductsList);

      // Limpa os campos do formulário para adicionar o próximo item
      this.resetFormForNextEntry();
    } else {
      console.log('Formulário inválido. Não foi possível adicionar o item à lista.');
    }
  }

  /**
   * Limpa o formulário para a próxima entrada de produto.
   */
  resetFormForNextEntry(): void {
    // Reseta o formulário, mas mantém o estado 'pristine' e 'untouched' para alguns campos
    this.stockEntryForm.reset({
      idProduct: null,
      productName: '',
      codLot: '',
      expiationDate: null,
      amount: null
    });
    this.selectedProductForEntry = null; // Limpa o produto selecionado
    // Opcional: resetar validações específicas se necessário
    this.stockEntryForm.get('idProduct')?.setErrors(null);
    this.stockEntryForm.get('productName')?.setErrors(null);
  }

  /**
   * Remove um item da lista de produtos em estoque.
   * @param index O índice do item a ser removido.
   */
  removeItem(index: number): void {
    this.stockProductsList.splice(index, 1);
  }

  /**
   * Finaliza a entrada de estoque, enviando a lista completa.
   */
  finalizeStockEntry(): void {
    if (this.stockProductsList.length > 0) {
      console.log('Finalizando entrada de estoque. Lista completa:', this.stockProductsList);
      // Aqui você enviaria 'this.stockProductsList' para o seu serviço/backend
      alert('Entrada de estoque finalizada! Verifique o console para os dados.');
      this.stockProductsList = []; // Limpa a lista após o "envio"
    } else {
      alert('A lista de produtos para entrada de estoque está vazia.');
    }
  }


}
