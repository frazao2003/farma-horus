import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouteUtils } from '../../../../Utils/utils';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { EntityPbDTO } from '../../../interface/dto/entity-pb-dto';
import { EntityPbService } from '../../../service/entity-pb.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';


@Component({
  selector: 'app-new-entity',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgxMaskDirective,],
  providers: [provideNgxMask()],
  templateUrl: './new-entity.component.html',
  styleUrl: './new-entity.component.css'
})
export class NewEntityComponent implements OnInit {

  entityForm!: FormGroup;
  entityPbDto!: EntityPbDTO;



  constructor(private fb: FormBuilder, private utils: RouteUtils, private entityPbService: EntityPbService, private toastr: ToastrService
    , private router: Router
  ) { }

  ngOnInit(): void {
    this.entityForm = this.fb.group({
      ecode: ['', Validators.required],
      name: ['', [Validators.required, Validators.minLength(3)]],
      address: ['', Validators.required],
      numberAddress: ['', Validators.required],
      estado: ['', Validators.required],
      cidade: ['', Validators.required],
      bairro: [''],
      cep: ['', [Validators.required], [this.utils.cepValidator()],],
      phoneNumber: ['', Validators.pattern(/^\(?\d{2}\)?\s?\d{4,5}-?\d{4}$/)],
      cnpj: ['',
        [Validators.required, Validators.pattern(/^\d{14}$/), this.utils.cnpjValidator()]
      ],
      email: ['', [Validators.required, Validators.email]]
    });

    this.handleCepAutoFill();


  }

  onSubmit() {
    if (this.entityForm.invalid) {
      this.entityForm.markAllAsTouched(); // força exibição dos erros
      return;
    }
    // Monta o DTO sem o id
    this.entityPbDto = {
      ecode: this.f['ecode'].value,
      name: this.f['name'].value,
      address: this.f['address'].value,
      numberAddress: this.f['numberAddress'].value,
      estado: this.f['estado'].value,
      cidade: this.f['cidade'].value,
      bairro: this.f['bairro'].value,
      cep: this.f['cep'].value,
      phoneNumber: this.f['phoneNumber'].value,
      cnpj: this.f['cnpj'].value,
      email: this.f['email'].value
    };

    this.postEntity();
  }

  handleCepAutoFill() {
    this.entityForm.get('cep')?.valueChanges.subscribe(async (cepRaw: string) => {
      const result = await this.utils.autoFillAddressByCep(cepRaw);
      if (result) {
        this.entityForm.patchValue(result);
        console.log("testes")
      }
    });
  }

  postEntity() {
    this.entityPbService.postEntity(this.entityPbDto).subscribe({
      next: (res) => {
        this.toastr.success('Entidade criada com sucesso!');
        this.entityForm.reset();
        this.router.navigate(['999025/home/admin'])
      },
      error: (err) => {
        console.error('Erro ao salvar entidade:', err);
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


  get f() {
    return this.entityForm.controls;
  }

}
