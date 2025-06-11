import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { HomeAdminComponent } from './pages/admin/home-admin/home-admin.component';
import { NewEntityComponent } from './pages/admin/new-entity/new-entity.component'; 
import { HomeComponent } from './pages/usuarios/home/home.component';
import { SectorsComponent } from './pages/usuarios/sectors/sectors.component';
import { CreateSectorComponent } from './pages/usuarios/sectors/create-sector/create-sector.component';
import { ProductsComponent } from './pages/usuarios/products/products.component';
import { CreateProductComponent } from './pages/usuarios/products/create-product/create-product.component';
import { EntryComponent } from './pages/usuarios/entry/entry.component';

export const routes: Routes = [

    { path: ':ecode/login', component: LoginComponent },
    {path: ':ecode/home', component: HomeComponent},
    {path: '999025/home/admin', component: HomeAdminComponent},
    {path: '999025/new-entity', component: NewEntityComponent},
    {path: ':ecode/sectors', component: SectorsComponent},
    {path: ':ecode/create-sector', component: CreateSectorComponent},
    {path: ':ecode/:id/update-sector', component: CreateSectorComponent},
    {path: ':ecode/products', component: ProductsComponent},
    {path: ':ecode/:id/update-product', component: CreateProductComponent},
    {path: ':ecode/create-product', component: CreateProductComponent},
    {path: ':ecode/entry', component: EntryComponent},
];
