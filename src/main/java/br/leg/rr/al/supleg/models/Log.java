package br.leg.rr.al.supleg.models;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.utils.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
@RequiredArgsConstructor
public class Log implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    
    @NonNull
    @Column(nullable = false, name = "data_acao")
    @ToString.Include
    private LocalDateTime dataAcao;
    
    @Column(nullable = true)
    private String ip;
    
    @NonNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    
    @NonNull
    @Column(nullable = false, name = "tipo_acao")
    @Enumerated(EnumType.STRING)
    private TipoAcao tipoAcao;
    
    @NonNull
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;
    
    @Column(nullable = true, name = "estado_inicial", columnDefinition = "TEXT")
    private String estadoInicial;
    
    @Column(nullable = true, name = "estado_final", columnDefinition = "TEXT")
    private String estadoFinal;
    
    
}
