package com.art.usermanagement.repository.project;

import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import com.art.usermanagement.model.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ProjectCriteriaRepoImpl implements ProjectCriteriaRepo {
    private EntityManager em;

    public ProjectCriteriaRepoImpl(EntityManager em)
    {
        this.em = em;
    }

    @Override
    public List<Project> search(UUID accountId, boolean isSuperUser, ProjectSearchQueryParam param)
    {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = criteriaQuery.from(Project.class);

//        where conditions (account id)
        List<Predicate> predicates = new ArrayList<>();
        Predicate predicate = criteriaBuilder.equal(root.get("account").get("id"), accountId);
        if (!isSuperUser) predicates.add(predicate);

//        sort by and order
        Order order = "asc".equalsIgnoreCase(param.getOrder()) ? criteriaBuilder.asc(root.get(param.getSortBy())) :
                criteriaBuilder.desc(root.get(param.getSortBy()));

        criteriaQuery.orderBy(order).where(predicates);
        TypedQuery<Project> query = em.createQuery(criteriaQuery);

//        page and size
        int pageNumber = (param.getPage() - 1) * param.getSize();
        query.setFirstResult(pageNumber);
        query.setMaxResults(param.getSize());

        return query.getResultList();
    }

    @Override
    public long getCount(ProjectSearchQueryParam param, boolean isSuperUser, UUID accountId)
    {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Project> root = criteriaQuery.from(Project.class);

//        where conditions (account id)
        List<Predicate> predicates = new ArrayList<>();
        Predicate predicate = criteriaBuilder.equal(root.get("account").get("id"), accountId);
        if (!isSuperUser) predicates.add(predicate);

        criteriaQuery.select(criteriaBuilder.count(root)).where(predicates);

        return em.createQuery(criteriaQuery).getSingleResult();
    }
}
