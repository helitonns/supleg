package br.leg.rr.al.supleg.models;

import java.io.Serializable;
import br.leg.rr.al.supleg.enums.TipoUsuario;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    @Column(length = 20, nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @ToString.Include
    @Column(nullable = false)
    private boolean status;
    
    @ToString.Include
    @Column(length = 40, nullable = false)
    private String nome;
    
    @ToString.Include
    @Column(length = 10, nullable = false)
    private String matricula;
    
    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

}
