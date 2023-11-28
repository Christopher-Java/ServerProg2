package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ActionsDaoJPAImpl implements ActionDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Action newAction) {
        em.persist(newAction);

    }
@Override
    public List<Action> getIncompleteActions(String userId) {

        String jpaQuery = "SELECT a FROM Action a WHERE a.owningUser = :userId AND a.complete = false";
        TypedQuery<Action> query = em.createQuery(jpaQuery, Action.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        Action existingAction = em.find(Action.class, actionToUpdate.getActionId());

        if (existingAction == null) {
            throw new RecordNotFoundException();
        }
        existingAction.setDetails(actionToUpdate.getDetails());
        existingAction.setComplete(actionToUpdate.isComplete());
        existingAction.setOwningUser(actionToUpdate.getOwningUser());
        existingAction.setRequiredBy(actionToUpdate.getRequiredBy());
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        Action action = em.find(Action.class, oldAction.getActionId());

        if (action == null) {
            throw new RecordNotFoundException();
        }
        em.remove(action);
    }
}
