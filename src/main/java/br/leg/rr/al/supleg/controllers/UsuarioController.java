package br.leg.rr.al.supleg.controllers;

import br.leg.rr.al.supleg.enums.TipoAcao;
import br.leg.rr.al.supleg.models.Usuario;
import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import br.leg.rr.al.supleg.enums.TipoUsuario;
import br.leg.rr.al.supleg.services.LogService;
import br.leg.rr.al.supleg.services.UsuarioService;
import br.leg.rr.al.supleg.utils.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class UsuarioController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;
    
    @Inject
    private LogService logService;

    @Getter
    private ArrayList<Usuario> usuarios;

    @Getter
    @Setter
    private Usuario usuario;

    @Getter
    @Setter
    private String senha;

    @Getter
    private TipoUsuario tipoUsuario;

    @Getter
    @Setter
    private Boolean statusPesquisa;

    //__________________________________________________________________________
    @PostConstruct
    public void init() {
        limparForm();
        
        logService.salvar(TipoAcao.ACESSOU, "O usuário acessou a página: " + FacesUtils.getURL(), "","");
    }
    
    public void listarTodosUsuarios() {
        try {
            usuarios = (ArrayList<Usuario>) usuarioService.listarTodos();
        } catch (DAOException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessage(e.getLocalizedMessage());
        }
    }

    public void listarTodosUsuariosSemSuperAdmin() {
        try {
            usuarios = (ArrayList<Usuario>) usuarioService.listarTodosSemSuperAdmin();
        } catch (DAOException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarUsuario() {
        try {
            return usuarioService.salvarUsuario(usuario, senha);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessage(e.getLocalizedMessage());
        }
        return "usuario.xhtml" + "?faces-redirect=true";
    }

    public void removerUsuario() {
        try {
            usuarioService.remover(usuario);
            MensagensUtils.addInfoMessage("Usuário removido com sucesso!");
        } catch (DAOException e) {
            System.out.println(e.getLocalizedMessage());
            MensagensUtils.addErrorMessage(e.getLocalizedMessage());
        }
        limparForm();
    }
    
    //__________________________________________________________________________
    private void limparForm() {
        usuarios = new ArrayList<>();
        usuario = new Usuario();

        tipoUsuario = TipoUsuario.RELATORIO;

        Usuario u = (Usuario) FacesUtils.getBean("usuario");
        if (u.getTipo().equals(TipoUsuario.SUPER_ADMIN)) {
            listarTodosUsuarios();
        } 
        else {
            listarTodosUsuariosSemSuperAdmin();
        }
    }

    public String cancelarUsuario() {
        return "usuario.xhtml" + "?faces-redirect=true";
    }

    public String cancelarSuperUsuario() {
        return "superusuario.xhtml" + "?faces-redirect=true";
    }
    
}
