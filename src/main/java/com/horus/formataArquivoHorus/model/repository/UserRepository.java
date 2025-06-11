package com.horus.formataArquivoHorus.model.repository;

import com.horus.formataArquivoHorus.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository  extends JpaRepository<User, Long> {


   UserDetails findByLogin(String email);
}
