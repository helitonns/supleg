package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.enums.TipoUsuario;
import br.leg.rr.al.supleg.models.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Usuario.
 *
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Usuario
 */
@Stateless
public class UsuarioRepository {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Usuario model) {
        em.persist(model);
    }
    
//    public void salvar(UsuarioPesquisa model) {
//        em.persist(model);
//    }

    public void atualizar(Usuario model) {
        em.merge(model);
    }

    public List listarTodos() {
        return em.createQuery("select o from Usuario o order by o.login asc").getResultList();
    }

    public List listarTodosSemSuperAdmin() {
        return em.createQuery("select o from Usuario o where o.tipo <> :tipo order by o.login asc")
                .setParameter("tipo", TipoUsuario.SUPER_ADMIN)
                .getResultList();
    }

    public List listarTodosAtivos() {
        return em.createQuery("select o from Usuario o where o.status = true order by o.login asc").getResultList();
    }

    public void remover(Usuario model) {
        model = em.merge(model);
        em.remove(model);
    }

    public Usuario pesquisarPorLogin(String login) {
        return em.createQuery("select u from Usuario u where u.login =:login", Usuario.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    public boolean haOLogin(String login) {
        return em.createQuery("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Usuario u where u.login =:login", Boolean.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    public Usuario pesquisarPorLoginESenha(String login, String senha) {
        return (Usuario) em.createQuery("select u from Usuario u where u.login =:login and u.senha =:senha", Usuario.class)
                .setParameter("login", login)
                .setParameter("senha", senha)
                .getSingleResult();
    }

    public boolean haUsuarioComEsteLogin(String login) {
        return em.createQuery("select u from Usuario u where u.login =:login", Usuario.class)
                .setParameter("login", login)
                .getResultList().size() >= 1;
    }

    public int removerUusarioPorLogin(String login) {
        return em.createQuery("DELETE FROM Usuario where login =:login")
                .setParameter("login", login)
                .executeUpdate();
    }
    
//    public List<UsuarioPesquisa> buscarUsuarioEventoPorUsuario(Usuario usuario) {
//        return em.createQuery("SELECT o FROM UsuarioPesquisa o WHERE o.usuario = :usuario", UsuarioPesquisa.class)
//                .setParameter("usuario", usuario)
//                .getResultList();
//    }
    
//    public List<Usuario> listarTodosSemSuperAdminPorPesquisa(Pesquisa pesquisa) {
//        return em.createQuery("SELECT o.usuario FROM UsuarioPesquisa o WHERE o.pesquisa = :pesquisa AND o.usuario.tipo <> :tipo ORDER BY o.usuario.login asc", Usuario.class)
//                .setParameter("pesquisa", pesquisa)
//                .setParameter("tipo", TipoUsuario.SUPER_ADMIN)
//                .getResultList();
//    }
}
