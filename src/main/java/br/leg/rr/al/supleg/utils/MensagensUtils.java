package br.leg.rr.al.supleg.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Classe utilitária que facilita a realização de algumas funções quando se
 * trabalha com JSF, como, por exemplo, exibir mensagens nas páginas xhtml.
 * </p>
 *
 * @author Heliton Nasciemento
 * @since 2019-11-26
 * @version 1.0
 */
public class MensagensUtils {

    /**
     * Contrutor vazio
     */
    private MensagensUtils() {
    }

    //==========================================================================
    /**
     * Adiciona uma mensagem de erro sem especificar um cliente. Método usado
     * quando não é necessáirio mudar de página.
     *
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    /**
     * Adiciona uma mensagem de erro especificando um cliente. Método usado
     * quando não é necessáirio mudar de página.
     *
     * @param clientId cliente da mensagem.
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

    /**
     * <p>
     * Adiciona uma mensagem de erro sem especificar um cliente. Método usado
     * quando é necessáirio mudar de página. Quando se muda de página e não este
     * método a mensagem não será exibna na página seguinte.
     * </p>
     *
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addErrorMessageFlashScoped(String msg) {
        addErrorMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    //==========================================================================
    /**
     * Adiciona uma mensagem de informação sem especificar um cliente. Método
     * usado quando não é necessáirio mudar de página.
     *
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    /**
     * Adiciona uma mensagem de informação especificando um cliente. Método
     * usado quando não é necessáirio mudar de página.
     *
     * @param clientId cliente da mensagem.
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    /**
     * <p>
     * Adiciona uma mensagem de informação sem especificar um cliente. Método
     * usado quando é necessáirio mudar de página. Quando se muda de página e
     * não este método a mensagem não será exibna na página seguinte.
     * </p>
     *
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addInfoMessageFlashScoped(String msg) {
        addInfoMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    //==========================================================================
    /**
     * Adiciona uma mensagem de alerta sem especificar um cliente. Método usado
     * quando não é necessáirio mudar de página.
     *
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addWarnMessage(String msg) {
        addWarnMessage(null, msg);
    }

    /**
     * Adiciona uma mensagem de alerta especificando um cliente. Método usado
     * quando não é necessáirio mudar de página.
     *
     * @param clientId cliente da mensagem.
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addWarnMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }

    /**
     * <p>
     * Adiciona uma mensagem de alerta sem especificar um cliente. Método usado
     * quando é necessáirio mudar de página. Quando se muda de página e não este
     * método a mensagem não será exibna na página seguinte.
     * </p>
     *
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addWarnMessageFlashScoped(String msg) {
        addWarnMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    
}
