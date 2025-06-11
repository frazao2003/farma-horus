import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { SectorService } from '../../../service/sector.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { SectorResponseDTO } from '../../../interface/dto/sector-response-dto';
import { HttpClientModule } from '@angular/common/http';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-sectors',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    TableModule, 
    ButtonModule, 
    InputTextModule, 
    TooltipModule,
    RouterModule
  ],
  templateUrl: './sectors.component.html',
  styleUrl: './sectors.component.css'
})
export class SectorsComponent {

  constructor(
    private route: ActivatedRoute,
    private sectorService: SectorService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }


  ecode: string | null = null;
  listSector: SectorResponseDTO[] = [];

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.getAllSector();
    }
    this.route.paramMap.subscribe((params) => {
      this.ecode = params.get('ecode');
    });
    
  }

  getAllSector() {
    this.sectorService.getAllSectors().subscribe({
      next: (data) => {
        this.listSector = data;
      },
      error: (err) => {
        console.error('Failed to fetch entities:', err);
      }
    });
  }

  filtrarGlobal(event: Event, tabela: any) {
    const valor = (event.target as HTMLInputElement).value;
    tabela.filterGlobal(valor, 'contains');
  }

}
