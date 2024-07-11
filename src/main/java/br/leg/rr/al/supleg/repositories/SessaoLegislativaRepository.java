package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.SessaoLegislativa;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class SessaoLegislativaRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(SessaoLegislativa o){
        em.persist(o);
    }

    public SessaoLegislativa atualizar(SessaoLegislativa o){
        return em.merge(o);
    }

    public void excluir(SessaoLegislativa model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<SessaoLegislativa> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM SessaoLegislativa o WHERE o.id = :id", SessaoLegislativa.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<SessaoLegislativa> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM SessaoLegislativa o WHERE o.status = :status ORDER BY o.legislatura DESC, o.sessaoLegislativa DESC", SessaoLegislativa.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public SessaoLegislativa buscarUltimaPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM SessaoLegislativa o WHERE o.status = :status ORDER BY o.legislatura DESC, o.sessaoLegislativa DESC", SessaoLegislativa.class)
                .setParameter("status", status)
        	.setMaxResults(1)
        	.getSingleResult();
    }
    
    public List<SessaoLegislativa> verificarDuplicidade(SessaoLegislativa sessaoLegislativa){
        return em.createQuery("SELECT o FROM SessaoLegislativa o WHERE o.legislatura = :legislatura AND o.sessaoLegislativa = :sessaoLegislativa", SessaoLegislativa.class)
                .setParameter("legislatura", sessaoLegislativa.getLegislatura())
                .setParameter("sessaoLegislativa", sessaoLegislativa.getSessaoLegislativa())
        	.setMaxResults(1)
        	.getResultList();
    }
}
