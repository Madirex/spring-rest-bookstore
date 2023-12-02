package com.nullers.restbookstore.rest.auth.services.authentication;

import com.nullers.restbookstore.rest.auth.dto.JwtAuthResponse;
import com.nullers.restbookstore.rest.auth.dto.UserSignInRequest;
import com.nullers.restbookstore.rest.auth.dto.UserSignUpRequest;
import com.nullers.restbookstore.rest.auth.exceptions.AuthSingInInvalid;
import com.nullers.restbookstore.rest.auth.exceptions.UserAuthNameOrEmailExisten;
import com.nullers.restbookstore.rest.auth.exceptions.UserDiferentePasswords;
import com.nullers.restbookstore.rest.auth.repositories.AuthUsersRepository;
import com.nullers.restbookstore.rest.auth.services.jwt.JwtService;
import com.nullers.restbookstore.rest.user.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication service implementation
 *
 * @Author Binwei Wang
 */
@Service
@Slf4j
public class AuthenticationServiceImp implements AuthenticationService {
    /**
     * Atributos de la clase
     */
    private final AuthUsersRepository authUsersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor de la clase con los atributos
     * @param authUsersRepository autenticacion de usuarios
     * @param passwordEncoder encriptacion de contraseñas
     * @param jwtService servicio de jwt
     * @param authenticationManager autenticacion de usuarios
     */
    @Autowired
    public AuthenticationServiceImp(AuthUsersRepository authUsersRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.authUsersRepository = authUsersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Metodo para registrar un usuario en la base de datos
     * @param request UserSignUpRequest con los datos del usuario
     * @return JwtAuthResponse con el token del usuario
     */
    @Override
    public JwtAuthResponse signUp(UserSignUpRequest request) {
        log.info("Creando usuario: {}", request);
        if(request.getPassword().contentEquals(request.getPasswordComprobacion())){
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .surnames(request.getApellidos())
                    .name(request.getNombre())
                    .build();

            try{
                var userStored = authUsersRepository.save(user);
                return JwtAuthResponse.builder()
                        .token(jwtService.generateToken(userStored))
                        .build();
            } catch (DataIntegrityViolationException e){
                throw new UserAuthNameOrEmailExisten("El usuario con username " + request.getUsername() + " o email " + request.getEmail() + " ya existe");
            }
        }else{
            throw new UserDiferentePasswords("Las contraseñas no coinciden");
        }
    }

    /**
     * Metodo para iniciar sesion en la aplicacion
     * @param request UserSignInRequest con los datos del usuario
     * @return JwtAuthResponse con el token del usuario
     */
    @Override
    public JwtAuthResponse signIn(UserSignInRequest request) {
        log.info("Autenticando usuario: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = authUsersRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow(() -> new AuthSingInInvalid("Usuario no encontrado"));
        var jwt =jwtService.generateToken(user);
        return JwtAuthResponse.builder()
                .token(jwt)
                .build();
    }
}
