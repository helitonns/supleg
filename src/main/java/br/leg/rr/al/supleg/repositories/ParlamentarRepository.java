package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.Parlamentar;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class ParlamentarRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Parlamentar o){
        em.persist(o);
    }

    public Parlamentar atualizar(Parlamentar o){
        return em.merge(o);
    }

    public void excluir(Parlamentar model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<Parlamentar> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM Parlamentar o WHERE o.id = :id", Parlamentar.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
    public List<Parlamentar> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM Parlamentar o WHERE o.status = :status ORDER BY o.nome", Parlamentar.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<Parlamentar> verificarDuplicidade(Parlamentar parlamentar){
        return em.createQuery("SELECT o FROM Parlamentar o WHERE o.nome = :nome", Parlamentar.class)
                .setParameter("nome", parlamentar.getNome())
        	.setMaxResults(1)
        	.getResultList();
    }
}
