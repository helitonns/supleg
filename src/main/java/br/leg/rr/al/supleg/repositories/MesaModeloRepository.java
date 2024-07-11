package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.MesaModelo;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class MesaModeloRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(MesaModelo o){
        em.persist(o);
    }

    public MesaModelo atualizar(MesaModelo o){
        return em.merge(o);
    }

    public void excluir(MesaModelo model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<MesaModelo> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM MesaModelo o WHERE o.id = :id", MesaModelo.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<MesaModelo> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM MesaModelo o WHERE o.status = :status", MesaModelo.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public MesaModelo buscarUltimaPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM MesaModelo o WHERE o.status = :status ORDER BY o.id DESC", MesaModelo.class)
                .setParameter("status", status)
        	.setMaxResults(1)
        	.getSingleResult();
    }
}
