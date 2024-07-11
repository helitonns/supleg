package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.TipoMateria;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class TipoMateriaRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(TipoMateria o){
        em.persist(o);
    }

    public TipoMateria atualizar(TipoMateria o){
        return em.merge(o);
    }

    public void excluir(TipoMateria model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<TipoMateria> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM TipoMateria o WHERE o.id = :id", TipoMateria.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<TipoMateria> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM TipoMateria o WHERE o.status = :status ORDER BY o.nome", TipoMateria.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<TipoMateria> verificarDuplicidade(TipoMateria tipoMateria){
        return em.createQuery("SELECT o FROM TipoMateria o WHERE o.nome = :nome", TipoMateria.class)
                .setParameter("nome", tipoMateria.getNome())
        	.setMaxResults(1)
        	.getResultList();
    }
}
