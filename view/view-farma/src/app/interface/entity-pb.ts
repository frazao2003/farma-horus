import { Sector } from "./sector";

export interface EntityPB {
    ecode?: string;
    name?: string;
    address?: string;
    numberAddress?: string;
    estado?: string;
    cidade?: string;
    bairro?: string;
    cep?: string;
    phoneNumber?: string;
    cnpj?: string;
    email?: string;
    sectors?: Sector[]; // Assuming you have a Sector interface
  }
