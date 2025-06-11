import { OutPut } from "./out-put"; // Importe a interface de OutPut

export interface OutputProduct {
  id?: number; // Em TypeScript, 'number' corresponde a Long e outras formas de números inteiros
  output?: OutPut; // Relacionamento ManyToOne com OutPut
  codLot?: string;
  description?: string;
  unit?: string;
  amount?: number; // 'int' em Java corresponde a 'number' em TypeScript
}
