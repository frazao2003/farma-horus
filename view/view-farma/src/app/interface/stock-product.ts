import { InProduct } from "./in-product"; // Importe a interface de InProduct
import { Sector } from './sector';// Importe a interface de Sector

export interface StockProduct {
  codLot?: string;
  expiationDate?: Date | string; // Use Date ou string dependendo de como você quer trabalhar com datas no frontend
  createdAt?: Date | string; // Use Date ou string dependendo de como você quer trabalhar com datas no frontend
  inProduct?: InProduct; // Relacionamento OneToOne com InProduct
  amount?: number;
  sector?: Sector; // Relacionamento ManyToOne com Sector
}