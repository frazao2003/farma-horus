import { EntityPB } from './entity-pb'; // Importe a interface de EntityPB
import { CompetenceHorus } from './competence-horus';// Importe a interface de CompetenceHorus (se existir)
import { StockProduct } from './stock-product'; // Importe a interface de StockProduct (se existir)
import { Entry } from './entry';// Importe a interface de Entry (se existir)
import { OutPut } from './out-put';// Importe a interface de OutPut (se existir)

export interface Sector {
  id?: string;
  name?: string;
  cnes?: string;
  nameResponsible?: string;
  cpfResponsible?: string;
  crf?: string;
  address?: string;
  numberAddress?: string;
  estado?: string;
  cidade?: string;
  cep?: string;
  phoneNumber?: string;
  entityPB?: EntityPB; // Relacionamento ManyToOne com EntityPB
  competenceHorusList?: CompetenceHorus[]; // Relacionamento OneToMany com CompetenceHorus
  stockProducts?: StockProduct[]; // Relacionamento OneToMany com StockProduct
  entries?: Entry[]; // Relacionamento OneToMany com Entry
  outPuts?: OutPut[]; // Relacionamento OneToMany com OutPut
}
