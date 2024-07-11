package br.leg.rr.al.supleg.models;

import br.leg.rr.al.supleg.utils.BaseEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author heliton.nascimento
 */
@Entity
@Table(name = "documento_acessorio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
public class DocumentoAcessorio implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "id_ordem")
    @ToString.Include
    private Ordem ordem;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_acessorio_id", nullable = true)
    @ToString.Include
    private List<Link> links;
    
    @ManyToOne
    @JoinColumn(name = "roteiro_secretario")
    @ToString.Include
    private RoteiroSecretario roteiroSecretario;
    
}
