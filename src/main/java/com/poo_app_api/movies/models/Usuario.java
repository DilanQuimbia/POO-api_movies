package com.poo_app_api.movies.models;

//import com.poo_app_api.movies.repositories.Token;
import jakarta.persistence.*;
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
    @Column(name = "nombre_User")
    private String nombre;
    @Column(name = "email_User")
    private String email;
    @Column(name = "username_User")
    private String username;
    @Column(name = "password_User")
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
