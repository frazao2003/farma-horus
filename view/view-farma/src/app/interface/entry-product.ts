import { Entry } from "./entry"; // Importe a interface de Entry

export interface EntryProduct {
  id?: number; // Em TypeScript, 'number' corresponde a Long e outras formas de números inteiros
  entry?: Entry; // Relacionamento ManyToOne com Entry
  codLot?: string;
  description?: string;
  unit?: string;
  amount?: number; // 'int' em Java corresponde a 'number' em TypeScript
}
