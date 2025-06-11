import { EntryProduct } from "./entry-product"; // Importe a interface de EntryProduct
import { Sector } from './sector';// Importe a interface de Sector

export interface Entry {
  id?: number; // Em TypeScript, 'number' corresponde a Long e outras formas de números inteiros
  entryProducts?: EntryProduct[]; // Relacionamento OneToMany com EntryProduct
  createdAt?: Date | string; // Use Date ou string dependendo de como você quer trabalhar com datas
  sector?: Sector; // Relacionamento ManyToOne com Sector
}
