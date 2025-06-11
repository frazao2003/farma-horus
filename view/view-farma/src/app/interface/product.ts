import { CompetenceHorus } from "./competence-horus"; // Importe a interface de CompetenceHorus

export interface Product {
  id?: number; // Em TypeScript, 'number' corresponde a Long e outras formas de números inteiros
  description?: string;
  lotCode?: string;
  validationDate?: string; // Ou Date, dependendo de como você quer trabalhar com datas
  manufacturer?: string;
  unit?: string;
  amount?: number;
  codGtin?: string;
  competenceHorus?: CompetenceHorus; // Relacionamento ManyToOne com CompetenceHorus
}
