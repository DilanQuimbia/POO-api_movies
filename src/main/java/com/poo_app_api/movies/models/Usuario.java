package com.poo_app_api.movies.models;

//import com.poo_app_api.movies.repositories.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
// En la tabla usuario no se puede repetir: "username"
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"username_User"})})
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id_User")
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 5, max = 60, message = "El nombre debe tener de 5 a 60 caracteres")
    @Column(name = "nombre_User", nullable = false)
    private String nombre;

    @NotNull(message = "El email no puede ser nulo")
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico debe tener un formato válido")
    @Column(name = "email_User", nullable = false)
    private String email;

    @NotNull(message = "El userName no puede ser nulo")
    @NotEmpty(message = "El userName no puede estar vacío")
    @Size(min = 5, max = 60, message = "El userName debe tener de 5 a 60 caracteres")
    @Column(name = "username_User", nullable = false)
    private String username;

    @NotNull(message = "El password no puede ser nulo")
    @NotEmpty(message = "El password no puede estar vacío")
    @Size(min = 8, max = 16, message = "La contraseña debe tener de 8 a 16 caracteres")
    @Column(name = "password_User", nullable = false)
    private String password;


    //Se accede a un usuario de la BDD, incluido sus roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "id_User")
            ,inverseJoinColumns = @JoinColumn(name = "id_Role"))
    private Set<Role> role = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_"+ r.getName()))
                .collect(Collectors.toList());
////        // Se asigna un solo rol
////        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //@Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}

//import jakarta.validation.constraints.*;
//
//@Entity
//@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"username_User"})})
//public class Usuario implements UserDetails {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_User")
//    private Long id;
//
//    @NotNull(message = "El nombre no puede ser nulo")
//    @NotEmpty(message = "El nombre no puede estar vacío")
//    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
//    @Column(name = "nombre_User")
//    private String nombre;
//
//    @NotNull(message = "El correo electrónico no puede ser nulo")
//    @NotEmpty(message = "El correo electrónico no puede estar vacío")
//    @Email(message = "El correo electrónico debe tener un formato válido")
//    @Column(name = "email_User")
//    private String email;
//
//    @NotNull(message = "El nombre de usuario no puede ser nulo")
//    @NotEmpty(message = "El nombre de usuario no puede estar vacío")
//    @Size(min = 5, max = 50, message = "El nombre de usuario debe tener entre 5 y 50 caracteres")
//    @Column(name = "username_User")
//    private String username;
//
//    @NotNull(message = "La contraseña no puede ser nula")
//    @NotEmpty(message = "La contraseña no puede estar vacía")
//    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
//    @Column(name = "password_User", nullable = false)
//    private String password;
//
//    // Resto de tu código
//}

