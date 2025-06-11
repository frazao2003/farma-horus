import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { SectorService } from '../../service/sector.service';
import { HttpClientModule } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';


@Component({
  selector: 'app-change-sector',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './change-sector.component.html',
  styleUrl: './change-sector.component.css'
})
export class ChangeSectorComponent implements OnInit {
  currentSectorName: string | null = null;

  constructor(private sectorService: SectorService, @Inject(PLATFORM_ID) private platformId: Object) { }



  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const cnes = sessionStorage.getItem('cnes');
      const cnesList = cnes ? JSON.parse(cnes) : [];

      
        for (const value of cnesList) {
          this.getSector(value)
        }

      

      this.currentSectorName = localStorage.getItem('nomeSetorAtual') || 'Nenhum setor';
    }
  }

  dropdownOpen = false;
  listNameSector: { name: string; cnes: string }[] = [];




  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onSelectSector(id: string, name: string) {
    localStorage.setItem('setorAtual', id)
    localStorage.setItem('nomeSetorAtual', name)
    window.location.reload();
  }

  getSector(id: string) {

    this.sectorService.getById(id).subscribe({
      next: (data) => {
        if (data?.name && data?.id) {
          this.listNameSector.push({ name: data.name, cnes: data.id });
        }
      },
      error: (err) => {
        console.error('Failed to fetch entities:', err);
      }
    });
  }

}
