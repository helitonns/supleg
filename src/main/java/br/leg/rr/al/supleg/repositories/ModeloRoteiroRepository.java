package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.ModeloRoteiro;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class ModeloRoteiroRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(ModeloRoteiro o){
        em.persist(o);
    }

    public ModeloRoteiro atualizar(ModeloRoteiro o){
        return em.merge(o);
    }

    public void excluir(ModeloRoteiro model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<ModeloRoteiro> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM ModeloRoteiro o WHERE o.id = :id", ModeloRoteiro.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<ModeloRoteiro> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM ModeloRoteiro o WHERE o.status = :status ORDER BY o.nome", ModeloRoteiro.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<ModeloRoteiro> verificarDuplicidade(ModeloRoteiro model){
        return em.createQuery("SELECT o FROM ModeloRoteiro o WHERE o.nome = :nome", ModeloRoteiro.class)
                .setParameter("nome", model.getNome())
                .setMaxResults(1)
                .getResultList();
    }
}
