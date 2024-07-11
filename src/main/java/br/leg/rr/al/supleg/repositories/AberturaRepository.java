package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.Abertura;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class AberturaRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Abertura o){
        em.persist(o);
    }

    public Abertura atualizar(Abertura o){
        return em.merge(o);
    }

    public void excluir(Abertura model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<Abertura> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM Abertura o WHERE o.id = :id", Abertura.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<Abertura> listarPorStatus(Boolean status){
        return em.createQuery("SELECT DISTINCT(o) FROM Abertura o JOIN FETCH o.trechos WHERE o.status = :status ORDER BY o.nome", Abertura.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<Abertura> verificarDuplicidade(Abertura model){
        return em.createQuery("SELECT o FROM Abertura o WHERE o.nome = :nome", Abertura.class)
                .setParameter("nome", model.getNome())
                .setMaxResults(1)
                .getResultList();
    }
}
