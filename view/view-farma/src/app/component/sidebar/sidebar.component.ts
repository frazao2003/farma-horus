import { Component, Inject, Input, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';


@Component({
  selector: 'app-simple-sidebar',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {

  @Input() ecode: string | null = null;
  entityName: string | null = null;

  constructor(private route: ActivatedRoute, @Inject(PLATFORM_ID) private platformId: Object) { }

  ngOnInit(

  ) {
    if (isPlatformBrowser(this.platformId)) {
      this.entityName = sessionStorage.getItem('entityName')
    }
  }





}
