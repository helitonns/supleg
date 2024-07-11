package br.leg.rr.al.supleg.enums;

import lombok.Getter;

/**
 *
 * @author heliton
 */
@Getter
public enum TurnoVotacao {

    TURNO_1("Votação em 1º Turno"), TURNO_2("Votação em 2º Turno"), TURNO_UNICO("Votação em Turno Único");
    
    private final String descricao;
    
    TurnoVotacao(String descricao){
        this.descricao = descricao;
    }

    public String value() {
        return name();
    }

    public String getValue() {
        return name();
    }

    public static TurnoVotacao fromValue(String v) {
        return valueOf(v);
    }

}
