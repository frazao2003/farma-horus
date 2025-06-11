package com.horus.formataArquivoHorus.controller.rest;


import com.horus.formataArquivoHorus.infra.security.TokenService;
import com.horus.formataArquivoHorus.model.entity.EntityPB;
import com.horus.formataArquivoHorus.model.entity.Sector;
import com.horus.formataArquivoHorus.model.entity.dto.AuthenticationDTO;
import com.horus.formataArquivoHorus.model.entity.dto.LoginResponseDto;
import com.horus.formataArquivoHorus.model.entity.dto.RegisterDTO;
import com.horus.formataArquivoHorus.model.entity.user.User;
import com.horus.formataArquivoHorus.model.entity.user.UserRole;
import com.horus.formataArquivoHorus.model.repository.UserRepository;
import com.horus.formataArquivoHorus.model.service.EntityPbService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/{tenant}/auth")
public class AuthenticationRest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EntityPbService entityPbService;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO,  @PathVariable  String tenant){

        entityManager.createNativeQuery("SET search_path TO \"" + tenant + "\"").executeUpdate();

        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(), authenticationDTO.getPassword());

        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User)auth.getPrincipal());

        List<String> listSectorID= ((User)auth.getPrincipal()).getSectorID();

        LoginResponseDto loginResponseDto = new LoginResponseDto();

        loginResponseDto.setToken(token);
        loginResponseDto.setSectorID(listSectorID);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/{cnes}/register")
    @Transactional
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO, @PathVariable String tenant){


        if(userRepository.findByLogin(registerDTO.getLogin()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.getPassword());

        User user = new User();

        user.setLogin(registerDTO.getLogin());
        user.setPassword(encryptedPassword);
        user.setRole(registerDTO.getRole());

        if (user.getRole().equals(UserRole.ADMIN)){
            user.setSectorID(registerDTO.getSectorIDList());
        }

        userRepository.save(user);



        return ResponseEntity.ok().build();

    }


}
