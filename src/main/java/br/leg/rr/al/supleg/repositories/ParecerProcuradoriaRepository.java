package br.leg.rr.al.supleg.repositories;

import br.leg.rr.al.supleg.models.ParecerProcuradoria;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Heliton Nascimento
 */
@Stateless
public class ParecerProcuradoriaRepository{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(ParecerProcuradoria o){
        em.persist(o);
    }

    public ParecerProcuradoria atualizar(ParecerProcuradoria o){
        return em.merge(o);
    }

    public void excluir(ParecerProcuradoria model){
        model = em.merge(model);
        em.remove(model);
    }
    
    public Optional<ParecerProcuradoria> buscarPorId(Long id){
        return Optional.ofNullable(em.createQuery("SELECT o FROM ParecerProcuradoria o WHERE o.id = :id", ParecerProcuradoria.class)
                .setParameter("id", id)
                .getSingleResult());
    }
    
        public Optional<ParecerProcuradoria> buscarNome(String nome){
        return Optional.ofNullable(em.createQuery("SELECT o FROM ParecerProcuradoria o WHERE UPPER(o.nome) = :nome", ParecerProcuradoria.class)
                .setParameter("nome", nome)
                .getSingleResult());
    }
    
    public List<ParecerProcuradoria> listarPorStatus(Boolean status){
        return em.createQuery("SELECT o FROM ParecerProcuradoria o WHERE o.status = :status ORDER BY o.nome", ParecerProcuradoria.class)
                .setParameter("status", status)
                .getResultList();
    }
    
    public List<ParecerProcuradoria> verificarDuplicidade(ParecerProcuradoria model){
        return em.createQuery("SELECT o FROM ParecerProcuradoria o WHERE o.nome = :nome", ParecerProcuradoria.class)
                .setParameter("nome", model.getNome())
        	.setMaxResults(1)
        	.getResultList();
    }
}
