/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://oss.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.persistence.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class BaseRepository<E extends Serializable> {

    static final Logger LOG = LoggerFactory.getLogger(BaseRepository.class);
    protected Class<E> entityType;
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public BaseRepository() {
        Type type = getClass().getGenericSuperclass();
        while (!(type instanceof ParameterizedType) || (((ParameterizedType) type).getRawType() != BaseRepository.class)) {
            if (type instanceof ParameterizedType) {
                type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
            } else {
                type = ((Class<?>) type).getGenericSuperclass();
            }
        }
        this.entityType = (Class<E>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    /**
     * Returns the current hibernate-session.
     */
    final Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Get {@link Class} of the entity of this Dao.
     */
    Class<E> getEntityType() {
        return entityType;
    }

    /**
     * Flush the whole session.
     */
    public void flush() {
        getCurrentSession().flush();
    }

    /**
     * Create entity.
     */
    public void create(E object) {
        getCurrentSession().save(object);
    }

    /**
     * Create or update ...
     */
    public void createOrUpdate(E object) {
        getCurrentSession().saveOrUpdate(object);
    }

    /**
     * Create {@link Criteria} for entity-type.
     */
    private Criteria distinctCriteria() {
        return getCurrentSession().createCriteria(getEntityType()).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    }

    /**
     * Create {@link Criteria} with the given {@link Criterion}s.
     */
    private Criteria criteriaWithCriterions(Criterion... criterions) {
        Criteria criteria = distinctCriteria();
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }
        return criteria;
    }

    /**
     * Find entity by id.
     */
    public E findById(long id) {
        return (E) getCurrentSession().get(getEntityType(), id);
    }

    /**
     * Query all entities with the given ids.
     */
    protected List<E> queryByIds(List<Long> list) {
        return query(Restrictions.in("id", list));
    }

    /**
     * Query all - really all!!
     */
    public List<E> queryAll() {
        return queryAll(new Order[0]);
    }

    /**
     * Query all - really all!! - using the given order.
     */
    public List<E> queryAll(Order... orders) {
        Criteria criteria = distinctCriteria();
        for (Order order : orders) {
            criteria.addOrder(order);
        }
        return criteria.list();
    }

    /**
     * Find by using {@link Criterion}s.
     */
    protected E find(Criterion... criterions) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = criteriaWithCriterions(criterions);
        E result = (E) criteria.uniqueResult();
        LOG.trace("Find for {} by {}. Runtime: {}", getEntityType(), criterions, System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * Query by using {@link Criterion}s.
     */
    protected List<E> query(Criterion... criterions) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = criteriaWithCriterions(criterions);
        List<E> list = criteria.list();
        LOG.trace("Query for {} by {}. Runtime: {}", getEntityType(), criterions, System.currentTimeMillis() - startTime);
        return list;
    }

    /**
     * Query by using {@link Criterion}s and an {@link Order}.
     */
    protected List<E> query(Order order, Criterion... criterions) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = criteriaWithCriterions(criterions);
        criteria.addOrder(order);
        List<E> list = criteria.list();
        LOG.trace("Query for {} by {} order {}. Runtime: {}", getEntityType(), criterions, order, System.currentTimeMillis() - startTime);
        return list;
    }

    /**
     * Find by user-defined criteria.
     */
    protected E find(CriteriaModifier cm) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = distinctCriteria();
        cm.modify(criteria);
        E result = (E) criteria.uniqueResult();
        LOG.trace("Find for {} by {}. Runtime: {}", getEntityType(), criteria, System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * Query by user-defined criteria.
     */
    protected List<E> query(CriteriaModifier cm) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = distinctCriteria();
        cm.modify(criteria);
        List<E> list = criteria.list();
        LOG.trace("Query for {} by {}. Runtime: {}", getEntityType(), criteria, System.currentTimeMillis() - startTime);
        return list;
    }

    /**
     * Find by example.
     */
    protected E find(E example) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = distinctCriteria();
        E result = (E) criteria.add(Example.create(example)).uniqueResult();
        LOG.trace("Find for {} by {} (example). Runtime: {}", getEntityType(), criteria, System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * Query by example.
     */
    protected List<E> query(E example) {
        long startTime = System.currentTimeMillis();
        Criteria criteria = distinctCriteria();
        List<E> list = criteria.add(Example.create(example)).list();
        LOG.trace("Query for {} by {} (example). Runtime: {}", getEntityType(), criteria, System.currentTimeMillis() - startTime);
        return list;
    }

    /**
     * Finds the entity by the given sql-query.
     *
     * @param sql    The SQL must return just the id-field aliased as 'id'.
     * @param params can be added by position '?' and a list of objects, or a single Map<String:Object> must be given.
     */
    protected E findBySqlUsingId(String sql, Object... params) {
        long startTime = System.currentTimeMillis();
        SQLQuery sqlQuery = createSqlUsingId(sql.toUpperCase(), params);
        long id = (long) sqlQuery.uniqueResult();
        E result = findById(id);
        LOG.trace("Find for {} by '{}'. Runtime: {}", getEntityType(), sql, System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * Query entities by the given sql-query.
     *
     * @param sql    The SQL must return just the id-field aliased as 'id'.
     * @param params can be added by position '?' and a list of objects, or a single Map<String:Object> must be given.
     */
    protected List<E> queryBySqlUsingId(String sql, Object... params) {
        long startTime = System.currentTimeMillis();
        SQLQuery sqlQuery = createSqlUsingId(sql.toUpperCase(), params);
        List<Long> list = sqlQuery.list();
        List<E> resultList = queryByIds(list);
        LOG.trace("Query for {} by '{}'. Runtime: {}", getEntityType(), sql, System.currentTimeMillis() - startTime);
        return resultList;
    }

    private SQLQuery createSqlUsingId(String sql, Object... params) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql).addScalar("id", LongType.INSTANCE);
        setQueryParameter(sqlQuery, params);
        return sqlQuery;
    }

    private void setQueryParameter(Query query, Object... params) {
        if (params != null && params.length == 1 && params[0] instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) params[0];
            for (String key : paramMap.keySet()) {
                query.setParameter(key, paramMap.get(key));
            }
        } else {
            for (int i = 0; params != null && i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
    }

    /**
     * Finds the entity by the given hql-query.
     *
     * @param hql    HQL query string.
     * @param params can be added by position '?' and a list of objects, or a single Map<String:Object> must be given.
     */
    protected E findByHql(String hql, Object... params) {
        long startTime = System.currentTimeMillis();
        Query hqlQuery = createHql(hql, params);
        E result = (E) hqlQuery.uniqueResult();
        LOG.trace("Find for {} by '{}'. Runtime: {}", getEntityType(), hql, System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * Query entities by the given hql-query.
     *
     * @param hql    HQL query string.
     * @param params can be added by position '?' and a list of objects, or a single Map<String:Object> must be given.
     */
    protected List<E> queryByHql(String hql, Object... params) {
        long startTime = System.currentTimeMillis();
        Query hqlQuery = createHql(hql, params);
        List<E> list = hqlQuery.list();
        LOG.trace("Query for {} by '{}'. Runtime: {}", getEntityType(), hql, System.currentTimeMillis() - startTime);
        return list;
    }

    /**
     * Execute an update by the given hql.
     *
     * @param hql    HQL string.
     * @param params can be added by position '?' and a list of objects, or a single Map<String:Object> must be given.
     */
    protected int executeUpdatebyHql(String hql, Object... params) {
        long startTime = System.currentTimeMillis();
        Query hqlQuery = createHql(hql, params);
        int count = hqlQuery.executeUpdate();
        LOG.trace("Execute update of {} by '{}'. Runtime: {}", getEntityType(), hql, System.currentTimeMillis() - startTime);
        return count;
    }

    protected Query createHql(String hql, Object... params) {
        Query hqlQuery = getCurrentSession().createQuery(hql);
        setQueryParameter(hqlQuery, params);
        return hqlQuery;
    }

    protected interface CriteriaModifier {
        void modify(Criteria criteria);
    }
}
