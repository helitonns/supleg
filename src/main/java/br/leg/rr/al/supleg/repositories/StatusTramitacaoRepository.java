package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.StatusTramitacao;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class StatusTramitacaoRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(StatusTramitacao o){
        em.persist(o);
    }

    public StatusTramitacao atualizar(StatusTramitacao o){
        return em.merge(o);
    }

    public void excluir(StatusTramitacao model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<StatusTramitacao> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM StatusTramitacao o WHERE o.id = :id", StatusTramitacao.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public Optional<StatusTramitacao> buscarNome(String nome){
        return Optional.ofNullable(em.createQuery("SELECT o FROM StatusTramitacao o WHERE UPPER(o.nome) LIKE :nome", StatusTramitacao.class)
                .setParameter("nome", "%" + nome + "%")
                .getSingleResult());
    }
    
    public List<StatusTramitacao> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM StatusTramitacao o WHERE o.status = :status ORDER BY o.nome", StatusTramitacao.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<StatusTramitacao> verificarDuplicidade(StatusTramitacao model){
        return em.createQuery("SELECT o FROM StatusTramitacao o WHERE o.nome = :nome", StatusTramitacao.class)
                .setParameter("nome", model.getNome())
        	.setMaxResults(1)
        	.getResultList();
    }
}
