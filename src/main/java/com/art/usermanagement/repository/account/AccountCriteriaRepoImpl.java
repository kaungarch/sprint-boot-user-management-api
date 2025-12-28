package com.art.usermanagement.repository.account;

import com.art.usermanagement.dto.request.AccountSearchQueryParam;
import com.art.usermanagement.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountCriteriaRepoImpl implements AccountCriteriaRepo {
    private EntityManager em;

    public AccountCriteriaRepoImpl(EntityManager em)
    {
        this.em = em;
    }

    @Override
    public List<Account> search(AccountSearchQueryParam param)
    {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
        Root<Account> root = criteriaQuery.from(Account.class);

//        where conditions (status, role)
        List<Predicate> predicates = new ArrayList<>();
        Optional.ofNullable(param.getStatus()).ifPresent(status -> {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        });

        Optional.ofNullable(param.getRole()).ifPresent(status -> {
            predicates.add(criteriaBuilder.equal(root.get("role"), status));
        });

//        set sort by and order
        Order orderBy = "asc".equalsIgnoreCase(param.getOrder()) ? criteriaBuilder.asc(root.get(param.getSortBy())) :
                criteriaBuilder.desc(root.get(param.getSortBy()));


        criteriaQuery.orderBy(orderBy).where(predicates);
        TypedQuery<Account> query = em.createQuery(criteriaQuery);
//        set page number and limit
        int pageNumber = (param.getPage() - 1) * param.getSize();
        query.setFirstResult(pageNumber);
        query.setMaxResults(param.getSize());

        return query.getResultList();
    }

    @Override
    public long getCount(AccountSearchQueryParam param)
    {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Account> root = criteriaQuery.from(Account.class);

//        where conditions
        List<Predicate> predicates = new ArrayList<>();
        Optional.ofNullable(param.getStatus()).ifPresent(status -> {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        });

        Optional.ofNullable(param.getRole()).ifPresent(status -> {
            predicates.add(criteriaBuilder.equal(root.get("role"), status));
        });

        criteriaQuery.select(criteriaBuilder.count(root)).where(predicates);

        return em.createQuery(criteriaQuery).getSingleResult();
    }
}
