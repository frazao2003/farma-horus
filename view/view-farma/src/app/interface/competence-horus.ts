import { Product } from "./product"; // Importe a interface de Product
import { Sector } from './sector'; // Importe a interface de Sector

export interface CompetenceHorus {
  id?: number; // Em TypeScript, 'number' corresponde a Long e outras formas de números inteiros
  competence?: string;
  products?: Product[]; // Relacionamento OneToMany com Product
  sector?: Sector; // Relacionamento ManyToOne com Sector
}