package br.leg.rr.al.supleg.services;

import br.leg.rr.al.supleg.enums.TipoAcao;
import java.io.Serializable;
import java.util.List;
import br.leg.rr.al.supleg.enums.TipoUsuario;
import br.leg.rr.al.supleg.exceptions.DAOException;
import br.leg.rr.al.supleg.repositories.UsuarioRepository;
import br.leg.rr.al.supleg.models.Usuario;
import br.leg.rr.al.supleg.utils.Criptografia;
import br.leg.rr.al.supleg.utils.MensagensUtils;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@ViewScoped
public class UsuarioService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsuarioRepository repository;
    
    @Inject
    private LogService logService;
	
    //__________________________________________________________________________
    
    public String salvarUsuario(Usuario usuario, String senha) {
        try {
            //verifca se a senha de usuário é forte, se sim permite o cadastro
            if (verificarForcaDaSenha(senha)) {
                if (usuario.getId() != null) {
                    usuario.setSenha(Criptografia.criptografarEmBcrypt(senha));
                    repository.atualizar(usuario);
                    MensagensUtils.addInfoMessageFlashScoped("Usuário atualizado com sucesso!");
                    logService.salvar(TipoAcao.ATUALIZOU, "O usuário alterou um usuário.", "", usuario.toString());
                } else {
                    //verifica se já há usuario cadastrado com o mesmo login
                    if (!repository.haUsuarioComEsteLogin(usuario.getLogin())) {
                        usuario.setSenha(Criptografia.criptografarEmBcrypt(senha));
                        repository.salvar(usuario);
                        
                        MensagensUtils.addInfoMessageFlashScoped("Usuário salvo com sucesso!");
                        logService.salvar(TipoAcao.SALVOU, "O usuário salvou um novo usuário.", "", usuario.toString());
                    } else {
                        MensagensUtils.addWarnMessageFlashScoped("O usuário não pode ser cadastrado, pois já há um usuário com este mesmo login!!!");
                    }
                }
            } else {
                MensagensUtils.addWarnMessageFlashScoped("A senha deve atender aos seguintes requisitos: ter no mínimo 8 caracteres, possuir letra minúcula 'a', possuir letra maiúscula 'A' e número '123'!!!");
            }
        } catch (DAOException e) {
            MensagensUtils.addErrorMessageFlashScoped("Erro ao salvar usuário!");
        }
        if(usuario.getTipo().equals(TipoUsuario.SUPER_ADMIN)){
            return "superusuario.xhtml" + "?faces-redirect=true";
        }
         return "usuario.xhtml" + "?faces-redirect=true";
    }
    
    public List<Usuario> listarTodos(){
        try {
            return repository.listarTodos();
        } catch (DAOException e) {
           throw new DAOException("Erro ao listar Usuario: "+e.getLocalizedMessage());
        }
    }
    
    public List<Usuario> listarTodosSemSuperAdmin(){
        try {
            return repository.listarTodosSemSuperAdmin();
        } catch (DAOException e) {
           throw new DAOException("Erro ao listar Usuario: "+e.getLocalizedMessage());
        }
    }
    
    public void remover(Usuario model) {
        try {
            repository.remover(model);
            logService.salvar(TipoAcao.APAGOU, "O usuário excluiu um usuário.", "", model.toString());
        } catch (DAOException e) {
           throw new DAOException("Erro ao excluir Usuario: "+e.getLocalizedMessage());
        }
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
    
}

