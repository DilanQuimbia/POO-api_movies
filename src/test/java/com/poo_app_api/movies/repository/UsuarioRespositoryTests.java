package com.poo_app_api.movies.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.poo_app_api.movies.models.Role;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//Comprobar componentes de la capa de peristencia (Clases con anotación @Entity;Entidades y Repositorios)
@DataJpaTest
@Import(TestConfig.class)
public class UsuarioRespositoryTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //@SpyBean //Permite instancias un mock parcial de un bean que posea las "implementaciones reales"
    private Usuario usuario;

    // Se ejecuta antes de cada método
    @BeforeEach
    void setup(){
        Role roleCliente = new Role();
        roleCliente.setId(1L); // Este ID debe coincidir con lo que tienes en la base de datos si ya están predefinidos
        roleCliente.setName("ROLE_Admin");

        Role roleAdmin = new Role();
        roleAdmin.setId(2L);
        roleAdmin.setName("ROLE_Cliente");


        usuario = new Usuario();
        usuario.setNombre("Dilan Flores");
        usuario.setUsername("DilanAC");
        usuario.setEmail("dilanflores.21@gmail.com");
        usuario.setPassword(passwordEncoder.encode("DF_1727d"));
        Set<Role> roles = new HashSet<>();
        roles.add(roleCliente);
        roles.add(roleAdmin);
        usuario.setRole(roles);
    }

    @DisplayName("Test para registrar Usuario")
    @Test
    void when_userIsCreated_expect_userToBeSavedSuccessfully(){
        // Metodología BDD(Estratgia de desarrollo; Cómo se implementa la funcionalidad)
        // Given(Dado):condición previa o configucarión
        // Given: Crear roles
        Role roleCliente = new Role();
        roleCliente.setId(1L); // Este ID debe coincidir con lo que tienes en la base de datos si ya están predefinidos
        roleCliente.setName("ROLE_Admin");

        Role roleAdmin = new Role();
        roleAdmin.setId(2L);
        roleAdmin.setName("ROLE_Cliente");

        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Dilan Flores");
        usuario1.setUsername("DilanAC");
        usuario1.setEmail("dilanflores.21@gmail.com");
        usuario.setPassword(passwordEncoder.encode("DF_1727d"));
        Set<Role> roles = new HashSet<>();
        roles.add(roleCliente);
        roles.add(roleAdmin);
        usuario1.setRole(roles);
        // When(Cuando): Acción o comportamiento que vamos a probar
        Usuario usuarioGuardado = usuarioRepository.save(usuario1);
        // Then(Entonces): Verificar la salida
        assertThat(usuarioGuardado).isNotNull();
        assertThat(usuarioGuardado.getId()).isGreaterThan(0);

//        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioGuardado.getId());
//        if (usuarioOptional.isPresent()) {
//            System.out.println(usuarioOptional.get().getNombre());
//            System.out.println(usuarioOptional.get().getUsername());
//            System.out.println(usuarioOptional.get().getEmail());
//            System.out.println(usuarioOptional.get().getPassword());
//            System.out.println(usuarioOptional.get().getRole());
//        } else {
//            System.out.println("Usuario no encontrado");
//        }
    }

    @DisplayName("Test para Iniciar sesión usuario")
    @Test
    void when_userIslogsin_expect_userToLogInSuccessfully(){
        //Given
        usuarioRepository.save(usuario);
        //When
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(usuario.getUsername());
        //Then
        assertThat(usuarioOptional).isPresent();
        assertThat(passwordEncoder.matches("DF_1727d", usuarioOptional.get().getPassword())).isTrue();
//        if (usuarioOptional.isPresent()) {
//            Usuario usuarioEncontrado = usuarioOptional.get();
//            assertThat(passwordEncoder.matches("DF_1727d", usuarioEncontrado.getPassword())).isTrue();
//            System.out.println("Usuario autenticado correctamente");
//        } else {
//            System.out.println("Usuario no encontrado o credenciales incorrectas");
//        }
    }

}
