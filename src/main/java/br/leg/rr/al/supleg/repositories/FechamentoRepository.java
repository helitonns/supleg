package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.Fechamento;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class FechamentoRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Fechamento o){
        em.persist(o);
    }

    public Fechamento atualizar(Fechamento o){
        return em.merge(o);
    }

    public void excluir(Fechamento model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<Fechamento> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM Fechamento o WHERE o.id = :id", Fechamento.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<Fechamento> listarPorStatus(Boolean status){
        return em.createQuery("SELECT DISTINCT o FROM Fechamento o JOIN FETCH o.trechos WHERE o.status = :status ORDER BY o.nome", Fechamento.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<Fechamento> verificarDuplicidade(Fechamento model){
        return em.createQuery("SELECT o FROM Fechamento o WHERE o.nome = :nome", Fechamento.class)
                .setParameter("nome", model.getNome())
                .setMaxResults(1)
                .getResultList();
    }
}
