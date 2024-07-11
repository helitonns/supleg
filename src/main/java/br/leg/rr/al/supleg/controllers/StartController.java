package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Usuario;
import br.leg.rr.al.supleg.repositories.UsuarioRepository;
import br.leg.rr.al.supleg.utils.Criptografia;
import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import br.leg.rr.al.supleg.enums.TipoUsuario;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author heliton
 */
@Named
@SessionScoped
public class StartController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsuarioRepository usuarioDAO;
    
    @Inject
    private LogService logService;

    @Getter
    @Setter
    private Usuario usuario;

    @Getter
    @Setter
    private String login = "";

    @Getter
    @Setter
    private String senha = "";

    @Getter
    @Setter
    private String senha1 = "";

    @Getter
    @Setter
    private String senha2 = "";

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        usuario = new Usuario();
    }

    public String logar() {
        try {
            usuario = autenticarUsuario(login, senha);
            
            if (usuario != null && usuario.isStatus()) {
                FacesUtils.setBean("usuario", usuario);
                logService.salvar(TipoAcao.ENTROU, "O usuário entrou no sistema.", "", "");

                if (usuario.getNome() == null || usuario.getMatricula() == null) {
                    MensagensUtils.addWarnMessageFlashScoped("O usuário está com o nome ou matrícula não preenchidos. Complete o seu perfil!");
                    return "/pages/common/perfil.xhtml" + "?faces-redirect=true";
                } else {
                    return "/pages/common/roteiro.xhtml" + "?faces-redirect=true";
                }
            } else {
                MensagensUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
            }
        } catch (DAOException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
        }
        return "/login.xhtml" + "?faces-redirect=true";
    }

    public String sair() {
        try {
            logService.salvar(TipoAcao.SAIU, "O usuário saiu do sistema.", "", "");
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return "/login.xhtml" + "?faces-redirect=true";
    }

    public String trocarSenha() {
        try {
            if (senha1.equals(senha2)) {
                if (verificarForcaDaSenha(senha1)) {
                    usuario.setSenha(Criptografia.criptografarEmBcrypt(senha1));
                    usuarioDAO.atualizar(usuario);
                    MensagensUtils.addInfoMessageFlashScoped("Senha atualizada com sucesso!!!");
                    logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou sua senha", "", usuario.toString());
                } else {
                    MensagensUtils.addWarnMessageFlashScoped("A senha deve atender aos seguintes requisitos: ter no mínimo 8 caracteres, possuir letra minúcula 'a', "
                            + "possuir letra maiúscula 'A' e número '123'!!!");
                }
            } else {
                MensagensUtils.addWarnMessageFlashScoped("As senhas não conferem");
            }
        } catch (DAOException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addInfoMessage("Senha atualizada com sucesso!!!");
        }
        return "/pages/common/redefinir.xhtml" + "?faces-redirect=true";
    }

    public String salvarNomeMatricula() {

        try {
            usuarioDAO.atualizar(usuario);
            MensagensUtils.addInfoMessage("Usuário atualizado com sucesso!!!");
            logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou os dados do seu perfil", "", usuario.toString());
            return "/pages/common/roteiro.xhtml" + "?faces-redirect=true";
        } catch (DAOException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessage("Erro au atualizar senha.");
        }
        return null;
    }

    //__________________________________________________________________________
    public boolean isUsuarioRelatorio() {
        return usuario.getTipo().equals(TipoUsuario.RELATORIO);
    }

    public boolean isOperador() {
        return usuario.getTipo().equals(TipoUsuario.OPERADOR);
    }

    public boolean isOuvinte() {
        return usuario.getTipo().equals(TipoUsuario.OUVINTE);
    }

    public boolean isAdmin() {
        return usuario.getTipo().equals(TipoUsuario.ADMIN);
    }

    public boolean isSuperAdmin() {
        return usuario.getTipo().equals(TipoUsuario.SUPER_ADMIN);
    }

    public String getSistema() {
        try {
            String[] s = FacesUtils.getURL().split("/");
            return s[1];
        } catch (Exception e) {
        }
        return null;
    }

    //__________________________________________________________________________
    private boolean verificarForcaDaSenha(String senha) {
        if (senha.length() < 8) {
            return false;
        }

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;

        for (char c : senha.toCharArray()) {
            if (c >= '0' && c <= '9') {
                achouNumero = true;
            } else if (c >= 'A' && c <= 'Z') {
                achouMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }

        //defini os parâmetros que serão avalidados
        //neste caso não irá levar em consideração o requisito "símbolo"
        return achouNumero && achouMaiuscula && achouMinuscula;
    }
    
    private Usuario autenticarUsuario(String login, String senha){
        var usuarioLogin = usuarioDAO.pesquisarPorLogin(login);
        var result = Criptografia.verificarBcrypt(senha, usuarioLogin.getSenha());
        return result ? usuarioLogin : null;
    }
}
