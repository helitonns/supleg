package br.leg.rr.al.supleg.enums;

import lombok.Getter;

/**
 *
 * @author heliton
 */
@Getter
public enum PossuiOrdem {

    ORDEM_AUTOMATICA("Ordem do dia automática"), ORDEM_MANUAL("Ordem do dia manual"), NAO("Não");
    
    private final String descricao;
    
    PossuiOrdem(String descricao){
        this.descricao = descricao;
    }

    public String value() {
        return name();
    }

    public String getValue() {
        return name();
    }

    public static PossuiOrdem fromValue(String v) {
        return valueOf(v);
    }

}
