// src/app/utils/route-utils.ts
import { ActivatedRoute } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AbstractControl, AsyncValidatorFn, ValidationErrors, ValidatorFn } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class RouteUtils {

  private apiUrlCorreios = 'https://viacep.com.br/ws/';

  constructor(private route: ActivatedRoute, private http: HttpClient) { }

  getEntityCodeFromRoute(): Observable<string | null> {
    return this.route.paramMap.pipe(
      map((params) => {
        const ecode = params.get('ecode');
        console.log('Código da Entidade da URL:', ecode);
        return ecode;
      })
    );
  }

  cnpjValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const cnpj = control.value?.replace(/[^\d]+/g, '');

      if (!cnpj || cnpj.length !== 14) {
        return { cnpj: true };
      }

      // Elimina CNPJs inválidos conhecidos
      if (/^(\d)\1{13}$/.test(cnpj)) {
        return { cnpj: true };
      }

      // Validação dos dígitos verificadores
      const calcCheckDigit = (cnpj: string, length: number): number => {
        const weights = length === 12
          ? [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]
          : [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];

        const sum = cnpj
          .slice(0, length)
          .split('')
          .reduce((acc, digit, index) => acc + Number(digit) * weights[index], 0);

        const rest = sum % 11;
        return rest < 2 ? 0 : 11 - rest;
      };

      const digit1 = calcCheckDigit(cnpj, 12);
      const digit2 = calcCheckDigit(cnpj, 13);

      if (digit1 !== Number(cnpj[12]) || digit2 !== Number(cnpj[13])) {
        return { cnpjValidate: true };
      }

      return null; // válido
    };
  }

  // Validador assíncrono para o CEP
  cepValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      const cep = control.value?.replace(/[^\d]+/g, ''); // Limpa os caracteres não numéricos

      if (!cep || cep.length !== 8) {
        return of(null); // Não faz validação se não for um CEP de 8 dígitos
      }

      // Validação assíncrona (consulta à API de CEP)
      return this.http.get<any>(`${this.apiUrlCorreios}${cep}/json/`).pipe(
        map((data) => {
          // Verifica se o campo "erro" existe, indicando erro na consulta
          return data && !data.erro ? null : { cepValidate: true }; // Retorna erro se CEP não encontrado
        }),
        catchError(() => of({ cepValidate: true })) // Se der erro na API, considera o CEP inválido
      );
    };
  }

  async autoFillAddressByCep(cep: string): Promise<any> {
    const cleanCep = cep.replace(/\D/g, '');

    if (cleanCep.length !== 8) return null;

    try {
      const response = await fetch(`https://viacep.com.br/ws/${cleanCep}/json/`);
      const data = await response.json();

      if (data.erro) return null;

      return {
        address: data.logradouro || '',
        bairro: data.bairro || '',
        cidade: data.localidade || '',
        estado: data.uf || ''
      };
    } catch (error) {
      console.error('Erro ao buscar CEP:', error);
      return null;
    }
  }

  cpfValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const cpf = control.value?.replace(/[^\d]+/g, '');

      if (!cpf || cpf.length !== 11) {
        return { cpfValidate: true };

      }

      // Elimina CPFs inválidos conhecidos (ex: 00000000000, 11111111111, ...)
      if (/^(\d)\1{10}$/.test(cpf)) {
        return { cpfValidate: true };
      }

      const calcCheckDigit = (cpf: string, length: number): number => {
        const sum = cpf
          .slice(0, length)
          .split('')
          .reduce((acc, digit, index) => acc + Number(digit) * ((length + 1) - index), 0);

        const rest = (sum * 10) % 11;
        return rest === 10 ? 0 : rest;
      };

      const digit1 = calcCheckDigit(cpf, 9);
      const digit2 = calcCheckDigit(cpf, 10);

      if (digit1 !== Number(cpf[9]) || digit2 !== Number(cpf[10])) {
        return { cpfValidate: true };
      }


      return null; // CPF válido
    };
  }
}