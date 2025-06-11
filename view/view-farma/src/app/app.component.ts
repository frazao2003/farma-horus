import { Component, Inject, OnInit, OnDestroy, PLATFORM_ID } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  RouterOutlet,
  Router,
  Event as RouterEvent // Importar Event como RouterEvent para evitar conflito
} from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { filter, Subscription, map } from 'rxjs'; // Importar 'map'

// Seus outros componentes e módulos importados
import { SidebarComponent } from "./component/sidebar/sidebar.component";
import { ChangeSectorComponent } from "./component/change-sector/change-sector.component";
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, ChangeSectorComponent, SidebarComponent, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  showHeader: boolean = true;
  private routerSubscription: Subscription | undefined;
  ecode: string | null = null; // Variável para armazenar o ecode capturado

  constructor(
    private router: Router,
    private route: ActivatedRoute, // ActivatedRoute do AppComponent
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      // 1. Inicia a escuta de eventos do roteador para capturar o 'ecode'
      this.routerSubscription = this.router.events.pipe(
        // Filtra para garantir que estamos lidando apenas com o fim de uma navegação
        filter((event: RouterEvent): event is NavigationEnd => event instanceof NavigationEnd),
        // Mapeia para a rota mais específica que está ativa na URL
        map(() => {
          let child = this.route.firstChild; // Começa pelo primeiro filho da rota do AppComponent
          while (child) {
            // Continua descendo na árvore de rotas se houver mais filhos
            if (child.firstChild) {
              child = child.firstChild;
            } else {
              // Quando não há mais filhos, esta é a rota "folha" mais específica
              return child;
            }
          }
          // Se não houver filhos (por exemplo, na rota raiz se ela não tiver filhos), retorna a própria rota do AppComponent
          return this.route;
        }),
        // Garante que estamos lidando com a rota da saída do roteador principal ('primary')
        filter(route => route.outlet === 'primary'),
        // Extrai o valor do parâmetro 'ecode' do snapshot da rota encontrada.
        // Se 'ecode' não for encontrado, retorna 'null'.
        map(route => route.snapshot.paramMap.get('ecode') || null)
      ).subscribe((ecodeValue: string | null) => {
        // Verifica se o 'ecode' mudou para evitar atualizações desnecessárias
        if (ecodeValue !== this.ecode) {
          this.ecode = ecodeValue;
          console.log('AppComponent: Ecode capturado da URL:', this.ecode);
        }
        // Sempre reavalia a visibilidade do cabeçalho em cada navegação finalizada
        this.updateHeaderVisibility(this.router.url);
      });

      // Chama a atualização da visibilidade do cabeçalho uma vez na inicialização
      // para garantir o estado correto na carga inicial da página.
      this.updateHeaderVisibility(this.router.url);
    }
  }

  // Método auxiliar para determinar a visibilidade do cabeçalho/navbar
  private updateHeaderVisibility(currentUrl: string) {
    // Lista das rotas onde o cabeçalho/navbar deve ser escondido.
    // Usamos o 'ecode' capturado dinamicamente para construir a rota de login.
    const routesToHideHeader = [
      `/${this.ecode ?? ''}/login`, 
      '/login',`/${this.ecode ?? ''}/products`
      // Adicione aqui outros caminhos que você quer esconder o cabeçalho (ex: '/cadastro', '/esqueci-senha')
      // Lembre-se: o 'currentUrl.includes(route)' verifica se a URL *contém* o segmento.
      // Se precisar de uma correspondência exata, use '===' ou regex.
    ];

    // O cabeçalho é mostrado (showHeader = true) se a URL atual NÃO inclui nenhuma das rotas para esconder.
    this.showHeader = !routesToHideHeader.some(route => currentUrl.includes(route));

    console.log(`DEBUG VISIBILIDADE: URL: ${currentUrl}, Ecode: ${this.ecode}, Rotas para esconder: ${routesToHideHeader.join(', ')}, Deve esconder? ${!this.showHeader}, showHeader: ${this.showHeader}`);
  }

  ngOnDestroy() {
    // É essencial cancelar a inscrição para evitar vazamentos de memória
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }
}