import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { SidebarComponent } from "../../../../component/sidebar/sidebar.component";
import { ChangeSectorComponent } from "../../../../component/change-sector/change-sector.component";
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SectorDTO } from '../../../../interface/dto/sector-dto';
import { RouteUtils } from '../../../../../Utils/utils';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { SectorService } from '../../../../service/sector.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-sector',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective],
  providers: [provideNgxMask()],
  templateUrl: './create-sector.component.html',
  styleUrl: './create-sector.component.css'
})
export class CreateSectorComponent implements OnInit {

  sectorForm!: FormGroup;
  ecode: string | null = null;
  editing = false;
  sectorId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private utils: RouteUtils,
    private sectorService: SectorService,
    @Inject(PLATFORM_ID) private platformId: Object,
    private toastr: ToastrService,
    private router: Router

  ) {
    
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.route.paramMap.subscribe((params) => {
      this.ecode = params.get('ecode');
      const id = params.get('id');

      if (id) {
        this.editing = true;
        this.sectorId = id;
        this.loadSector(id);
      }
    });

    this.sectorForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      cnes: ['', Validators.required],
      nameResponsible: ['', [Validators.required, Validators.minLength(3)]],
      cpfResponsible: ['', [Validators.required, this.utils.cpfValidator()],],
      crf: ['', [Validators.required]],
      address: ['', [Validators.required]],
      numberAddress: ['', [Validators.required]],
      estado: ['', [Validators.required]],
      cidade: ['', Validators.required],
      cep: ['', [Validators.required], [this.utils.cepValidator()]],
      phoneNumber: ['', [Validators.required, Validators.minLength(11),]]
    });



    this.handleCepAutoFill();
    }
    
  }

  loadSector(id: string): void {
    this.sectorService.getById(id).subscribe(data => {
      this.sectorForm.patchValue(data);
    });
  }

  onSubmit() {
    if (this.sectorForm.invalid) {
      this.sectorForm.markAllAsTouched(); // Força a exibição dos erros
      console.log(this.sectorForm.invalid)
      return;
    }

    // Monta o DTO com os dados do formulário
    const sectorDto: SectorDTO = {
      name: this.f['name'].value,
      cnes: this.f['cnes'].value,
      nameResponsible: this.f['nameResponsible'].value,
      cpfResponsible: this.f['cpfResponsible'].value,
      crf: this.f['crf'].value,
      address: this.f['address'].value,
      numberAddress: this.f['numberAddress'].value,
      estado: this.f['estado'].value,
      cidade: this.f['cidade'].value,
      cep: this.f['cep'].value,
      phoneNumber: this.f['phoneNumber'].value
    };


    if (this.editing && this.sectorId) {
      this.sectorService.update(this.sectorId, sectorDto).subscribe({
        next: (res) => {
        this.toastr.success('Setor editado com sucesso');
        this.sectorForm.reset();
        this.router.navigate([`${this.ecode}/sectors`])
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
    } else {
      this.sectorService.create(sectorDto).subscribe({
        next: (res) => {
        this.toastr.success('Setor criado com sucesso');
        this.sectorForm.reset();
        this.router.navigate([`${this.ecode}/sectors`])
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


    // Envia para o backend
  }

  handleCepAutoFill() {
    this.sectorForm.get('cep')?.valueChanges.subscribe(async (cepRaw: string) => {
      const result = await this.utils.autoFillAddressByCep(cepRaw);
      if (result) {
        this.sectorForm.patchValue(result);
      }
    });
  }

  get f() {
    return this.sectorForm.controls;
  }

}
