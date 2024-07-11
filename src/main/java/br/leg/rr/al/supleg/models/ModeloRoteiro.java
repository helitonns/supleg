package br.leg.rr.al.supleg.models;

import br.leg.rr.al.supleg.enums.PossuiOrdem;
import br.leg.rr.al.supleg.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author heliton.nascimento
 */
@Entity
@Table(name = "modelo_roteiro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
@RequiredArgsConstructor
public class ModeloRoteiro implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @Column(nullable = false)
    @ToString.Include
    @NonNull
    private String nome;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "abertura_id",nullable = true)
    private Abertura abertura;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fechamento_id",nullable = true)
    private Fechamento fechamento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "possui_ordem_dia")
    @ToString.Include
    @NonNull
    private PossuiOrdem possuiOrdemDia;
    
    @Column(nullable = false)
    @ToString.Include
    @NonNull
    private Boolean status;
    
    public Boolean getPossuiOrdemDiaBoolean(){
        return possuiOrdemDia.equals(PossuiOrdem.ORDEM_AUTOMATICA) || possuiOrdemDia.equals(PossuiOrdem.ORDEM_MANUAL);
    }
    
}
