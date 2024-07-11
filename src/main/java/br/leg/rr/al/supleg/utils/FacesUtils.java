/*
 * Copyright (C) 2020 - 2023, Assembleia Legislativa do Estado de Roraima.
 * Boa Vista, Roraima - Brasil
 * Todos os direitos reservados.
 * ===============================================================================================
 * Este programa é propriedade da Assembleia Legislativa do Estado de Roraima e não é permitida a
 * distribuição, alteração ou cópia da mesma sem prévia autoriazação.
 * ===============================================================================================
 */
 /*
Copyright (C) 2021 - Assembleia Legislativa do Estado de Roraima Licença.
Boa Vista, Roraima - Brasil
Todos os direitos reservados.

Este programa é propriedade da Assembleia Legislativa do Estado de Roraima e não é permitida a
distribuição, alteração ou cópia da mesma sem prévia autoriazação.
 */
package br.leg.rr.al.supleg.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Ednil Libanio da Costa Junior
 */
public class FacesUtils implements Serializable {

    private static final long serialVersionUID = -560525635343950446L;

    public static FacesContext getFacesContextInstance() {
        return FacesContext.getCurrentInstance();
    }

    public static ELContext getELContext() {
        return FacesContext.getCurrentInstance().getELContext();
    }

    public static Application getApplication() {
        return FacesContext.getCurrentInstance().getApplication();
    }

    public static ExpressionFactory getExpressionFactory() {
        return FacesUtils.getApplication().getExpressionFactory();
    }

    public static void navigateTo(String outcome) {
        getNavigationHandler().handleNavigation(getFacesContextInstance(), null, outcome);
    }

    public static HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static HttpServletResponse getServletResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public static MethodExpression createMethodExpression(String action) {
        return getExpressionFactory().createMethodExpression(FacesUtils.getELContext(), action, String.class,
                new Class[]{ActionEvent.class});
    }

    public static MethodExpressionActionListener createMethodExpressionActionListener(String actionListenerName) {
        return new MethodExpressionActionListener(createMethodExpression(actionListenerName));
    }

    public static String createId() {
        return FacesContext.getCurrentInstance().getViewRoot().createUniqueId();
    }

    public static UIViewRoot getViewRoot() {
        return FacesContext.getCurrentInstance().getViewRoot();
    }

    public static void setViewRoot(UIViewRoot viewRoot) {
        FacesContext.getCurrentInstance().setViewRoot(viewRoot);
    }

    public static ConfigurableNavigationHandler getNavigationHandler() {
        return (ConfigurableNavigationHandler) getApplication().getNavigationHandler();
    }

    public static NavigationCase getNavigationCase(String outcome) {
        return getNavigationHandler().getNavigationCase(getFacesContextInstance(), null, outcome);
    }

    public static String getToViewId(String outcome) {
        return getNavigationCase(outcome).getToViewId(getFacesContextInstance());
    }

    public static FaceletContext getFaceletContext(String outcome) {
        return (FaceletContext) getFacesContextInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
    }

    /**
     *
     * @return {@link Locale} da {@code ViewRoot}
     */
    public static Locale getLocale() {
        return getViewRoot().getLocale();
    }

    /**
     *
     * @return {@link Locale} do navegador do usuário.
     */
    public static Locale getRequestLocale() {
        return getServletRequest().getLocale();
    }

    public static void setLocale(String language, String country) {
        getViewRoot().setLocale(new Locale(language, country));
    }

    /**
     * Método que busca o resource bundle que está cadastrado no faces-config
     * pelo nome da variável "var"
     *
     * @param varname valor que deseja buscar, por exemplo, "estudo_enum".
     * @return o bundle encontrado ou {@code null}, caso contrário.
     */
    public static ResourceBundle getResourceBundleVarName(String varname) {
        /*
		 * String baseName =
		 * FacesConfigXml.instance().getResourceBundles().get(varname); return
		 * ResourceBundle.getBundle(baseName);
         */
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        return application.getResourceBundle(context, varname);

    }

    public static void includeFacelet(UIComponent parent, String outcome) {
        final String url = getToViewId(outcome);
        try {
            getFaceletContext(outcome).includeFacelet(parent, url);

        } catch (final IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    public static HttpSession getSession(Boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
    }

    public static String getFullContextPath() {
        return getServletRequest().getRequestURL().substring(0, getServletRequest().getRequestURL().indexOf(":"))
                + "://" + getServletRequest().getServerName() + ":" + getServletRequest().getServerPort()
                + getServletRequest().getContextPath();
    }

    public static boolean isUserInRole(String permission) {
        return FacesUtils.getServletRequest().isUserInRole(permission);
    }

    /**
     * <p>
     * Verifica se o usuário tem permissão de acessar uma View.
     * </p>
     *
     * @param outcome valor que encontra mapeado no faces-config.xml que aponta
     * para toViewId
     * @return {@code true} se usuário tem permissão.
     */
    public static boolean hasViewPermission(String outcome) {
        return FacesUtils.getServletRequest().isUserInRole(FacesUtils.getToViewId(outcome));
    }

    public static Flash getFlash() {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash();
    }

    public static UIComponent findComponent(String componentId) {

        final UIViewRoot root = getViewRoot();
        final UIComponent c = findComponent(root, componentId);
        return c;
    }

    public static ViewHandler getViewHandler() {
        return getApplication().getViewHandler();
    }

    public static Map<String, Object> getViewMap() {
        return getViewRoot().getViewMap();
    }

    public static UIViewRoot createView() {
        return getViewHandler().createView(getFacesContextInstance(), getViewRoot().getViewId());

    }

    private static UIComponent findComponent(UIComponent component, String id) {
        if (id.equals(component.getId())) {
            return component;
        }

        final Iterator<UIComponent> kids = component.getFacetsAndChildren();
        while (kids.hasNext()) {
            final UIComponent kid = kids.next();
            final UIComponent found = findComponent(kid, id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public static Map<String, Object> getRequestMap() {
        return getExternalContext().getRequestMap();
    }

    public static Map<String, String> getRequestParameterMap() {
        return getExternalContext().getRequestParameterMap();
    }

    /**
     * @param key chave do parametro a ser pesquisado.
     * @return parametro passado para o Controller.
     */
    public static String getParam(Object key) {
        return getRequestParameterMap().get(key);
    }

    public static String putParam(String key, String value) {
        return getRequestParameterMap().put(key, value);
    }

    /**
     * Limpa todos os campos de entrada e saída de dados.
     */
    public static void resetView() {
        final UIViewRoot viewRoot = createView();
        setViewRoot(viewRoot);
        getFacesContextInstance().renderResponse();
    }

    public static boolean isPostback() {
        return getFacesContextInstance().isPostback();
    }

    public static String getViewId() {
        return getViewRoot().getViewId();
    }

    /**
     * <p>
     * Método utilizado para limpar as mensagens da tela. Pode ser utilizado,
     * por exemplo, após chamar um dialog a partir de uma view que possui muitas
     * mensagens referentes a uma transação anterior. Isso evitará propagação de
     * mensagens que não condiz com a transação em execução.
     * </p>
     */
    public static void clearMessages() {
        getFacesContextInstance().getMessages().remove();
    }

    public static String getContextPath() {
        return getServletContext().getContextPath();
    }

    public static void redirect(String url) {
        try {
            getExternalContext().redirect(url);
        } catch (IOException e) {
        }
    }

    public static Map<String, Object> getAttributes(ActionEvent event) {
        return event.getComponent().getAttributes();
    }

    /**
     * Obtém o IP da máquina do cliente.
     *
     * @return IP do hostname do cliente
     */
    public static String getRemoteAddr() {

        return getServletRequest().getRemoteAddr();
    }

    public static String getRootPath() {

        return getServletRequest().getContextPath();
    }

    /**
     * Obtém o nome da máquina do cliente.
     *
     * @return nome do hostname do cliente.
     */
    public static String getRemoteHost() {

        return getServletRequest().getRemoteHost();
    }

    /**
     * Retorna a lista de locales suportados que foram informados no
     * faces-config.xml
     *
     * @return lista de locales suportados pelo sistema.
     */
    public static List<Locale> getSupportedLocales() {
        // Create an empty list
        final List<Locale> locales = new ArrayList<>();

        final Iterator<Locale> iterator = getApplication().getSupportedLocales();
        // locales.add(getApplication().getDefaultLocale());
        iterator.forEachRemaining(locales::add);
        return locales;
    }

    /**
     * Busca no arquivo web.xml o parametro de inicialização.
     *
     * @param initParam nome do parametro a ser buscado no web.xml
     * @return valor do initparam informado.
     */
    public static String getInitParameter(String initParam) {
        return getServletContext().getInitParameter(initParam);
    }

    /**
     * Busca o ResourceBundle que está definido no arquivo de configuração da
     * aplicação.
     *
     * @param bundleName nome do resource bundle
     * @return o resource bundle
     */
    public static ResourceBundle getFacesResourceBundle(String bundleName) {
        return getApplication().getResourceBundle(FacesContext.getCurrentInstance(), bundleName);
    }

    //==========================================================================
    /**
     * Método usado quando é necessário jogar um objeto na sessão.
     *
     * @param nome representa o nome pelo qual o obejto será acessado quando já
     * estiver na sessão.
     * @param o representa o objeto que será jogado na sessão.
     */
    public static void setBean(String nome, Object o) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nome, o);
    }

    /**
     * Método usado quando é necessário recuperar um obejto que está ne sessão.
     *
     * @param nome representa o nome do objeto na sessão.
     * @return retorna o objeto que estava na sessão.
     */
    public static Object getBean(String nome) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nome);
    }

    /**
     * Método usado quando é necessário retirar da sessão um objeto que lá
     * estava.
     *
     * @param nome representa o nome do objeto que deve ser retirado da sessão.
     */
    public static void removeBean(String nome) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(nome);
    }

    //==========================================================================
    /**
     * Método que retorna o IP da máquina que está acessando a aplicação.
     *
     * @return IP da máquina.
     */
    public static String getIP() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getRemoteAddr();
    }

    /**
     * Método que retorna a URL da página atual da aplicação.
     *
     * @return URL da página.
     */
    public static String getURL() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        return request.getRequestURI();
    }
    //==========================================================================
}
