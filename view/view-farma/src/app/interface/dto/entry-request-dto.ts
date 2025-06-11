import { StockProductDTO } from "./stock-product-dto";

export interface EntryRequestDTO {
  stockProductDTOList: StockProductDTO[];
  entryDate: Date;
}