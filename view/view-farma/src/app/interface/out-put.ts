import { OutputProduct } from "./output-product"; // Importe a interface de OutputProduct
import { Sector } from './sector';// Importe a interface de Sector

export interface OutPut {
  id?: number; // Em TypeScript, 'number' corresponde a Long e outras formas de números inteiros
  outputProducts?: OutputProduct[]; // Relacionamento OneToMany com OutputProduct
  createdAt?: Date | string; // Use Date ou string dependendo de como você quer trabalhar com datas
  sector?: Sector; // Relacionamento ManyToOne com Sector
}
