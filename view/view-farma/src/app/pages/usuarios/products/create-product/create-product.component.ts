import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { InProductService } from '../../../../service/in-product.service';
import { InProductDTO } from '../../../../interface/dto/in-product-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-product',
  standalone: true,
  imports: [CommonModule, HttpClientModule, ReactiveFormsModule],
  templateUrl: './create-product.component.html',
  styleUrl: './create-product.component.css'
})
export class CreateProductComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
    private inProductService: InProductService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  ecode: string | null = null;
  inProduct: InProductDTO | null = null;
  productForm!: FormGroup;
  editing = false;
  sectorId: string | null = null;




  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.route.paramMap.subscribe((params) => {
        this.ecode = params.get('ecode');
        const id = params.get('id');
        if (id) {
          this.editing = true;
          this.sectorId = id;
          this.loadProduct(id);
        }
      });
    }

    this.productForm = this.fb.group({
      description: ['', [Validators.required]],
      unit: ['', [Validators.required]],
      manufacturer: ['', [Validators.required]],
      composicao: ['', [Validators.required]],
    });
  }


  onSubmit() {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched(); // Força a exibição dos erros
      console.log(this.productForm.invalid)
      return;
    }

    const inProductDTO: InProductDTO = {
      description: this.f['description'].value,
      unit: this.f['unit'].value,
      manufacturer: this.f['manufacturer'].value,
      composicao: this.f['composicao'].value,
    }

    if(!this.editing){
      this.createProduct(inProductDTO);
    }
  }

  loadProduct(id: string){
    this.inProductService.getByID(id).subscribe((data) => {
      this.productForm.patchValue(data);
    });

  }


  createProduct(inProductDTO: InProductDTO) {
    this.inProductService.postInProduct(inProductDTO).subscribe({
        next: (res) => {
        this.toastr.success('Setor editado com sucesso');
        this.productForm.reset();
        this.router.navigate([`${this.ecode}/creat-product`])
      },
      error: (err) => {
        console.error('Erro ao salvar setor:', err);
        // Verifique o formato completo do erro
        if (err.error) {
          console.error('Detalhes do erro:', err.error);
        } else {
          console.error('Erro desconhecido:', err);
        }
        this.toastr.error(err.error);
      }
      });
  }

  filtrarGlobal(event: Event, tabela: any) {
    const valor = (event.target as HTMLInputElement).value;
    tabela.filterGlobal(valor, 'contains');
  }

  get f() {
    return this.productForm.controls;
  }





}
