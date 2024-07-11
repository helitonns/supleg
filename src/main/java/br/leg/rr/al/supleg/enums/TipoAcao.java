package br.leg.rr.al.supleg.enums;

/**
 *
 * @author heliton
 */
public enum TipoAcao {

      ACESSOU, APAGOU, ATUALIZOU, ENTROU, EXECUTOU, SAIU, SALVOU;

    public String value() {
        return name();
    }

    public String getValue() {
        return name();
    }

    public static TipoAcao fromValue(String v) {
        return valueOf(v);
    }

}
