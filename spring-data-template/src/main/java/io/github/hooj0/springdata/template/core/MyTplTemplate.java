package io.github.hooj0.springdata.template.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mapping.Person;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.hooj0.springdata.template.TemplateRootException;
import io.github.hooj0.springdata.template.annotations.Model;
import io.github.hooj0.springdata.template.core.convert.MappingTemplateConverter;
import io.github.hooj0.springdata.template.core.convert.TemplateConverter;
import io.github.hooj0.springdata.template.core.mapping.SimpleTemplateMappingContext;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentEntity;
import io.github.hooj0.springdata.template.core.mapping.TemplatePersistentProperty;
import io.github.hooj0.springdata.template.core.query.Criteria;
import io.github.hooj0.springdata.template.core.query.CriteriaQuery;
import io.github.hooj0.springdata.template.core.query.StringQuery;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>function:</b> 模板类，完成CRUD 的操作
 * @author hoojo
 * @createDate 2018年7月10日 上午11:02:13
 * @file MyTplTemplate.java
 * @package io.github.hooj0.springdata.template.core
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings({ "all", "unused" })
@Slf4j
public class MyTplTemplate implements TemplateOperations, ApplicationContextAware {

	private Object client = new Object();
	private TemplateConverter converter;
	
	public MyTplTemplate(Object client) {
		this(client, new MappingTemplateConverter(new SimpleTemplateMappingContext()));
	}
	
	public MyTplTemplate(Object client, TemplateConverter converter) {
		Assert.notNull(client, "Client must not be null!");
		Assert.notNull(converter, "TemplateConverter must not be null!");

		this.client = client;
		this.converter = converter;
	}
	
	@Override
	public TemplateConverter getTemplateConverter() {
		return converter;
	}

	@Override
	public Object getClient() {
		return client;
	}

	@Override
	public String getTemplateName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String getEntityName() {
		return client.getClass().getSimpleName();
	}
	
	public TemplatePersistentEntity getPersistentEntityFor(Class clazz) {
		Assert.isTrue(clazz.isAnnotationPresent(Model.class), "Unable to identify index name. " + clazz.getSimpleName()
				+ " is not a Document. Make sure the document class is annotated with @Model(indexName=\"foo\")");
		return converter.getMappingContext().getRequiredPersistentEntity(clazz);
	}
	
	private String getPersistentEntityId(Object entity) {

		TemplatePersistentEntity<?> persistentEntity = getPersistentEntityFor(entity.getClass());
		Object identifier = persistentEntity.getIdentifierAccessor(entity).getIdentifier();

		if (identifier != null) {
			return identifier.toString();
		}
		return null;
	}
	
	private void setPersistentEntityId(Object entity, String id) {

		TemplatePersistentEntity<?> persistentEntity = getPersistentEntityFor(entity.getClass());
		TemplatePersistentProperty idProperty = persistentEntity.getIdProperty();

		// Only deal with text because ES generated Ids are strings !
		if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
			persistentEntity.getPropertyAccessor(entity).setProperty(idProperty, id);
		}
	}
	
	private String[] retrieveIndexNameFromPersistentEntity(Class clazz) {
		if (clazz != null) {
			return new String[] { getPersistentEntityFor(clazz).getIndexName() };
		}
		return null;
	}

	private String[] retrieveTypeFromPersistentEntity(Class clazz) {
		if (clazz != null) {
			return new String[] { getPersistentEntityFor(clazz).getIndexType() };
		}
		return null;
	}
	
	@Override
	public boolean add(Object entity) {
		log.debug("template run method: add({})", entity);
		//client.add(entity);
		
		return true;
	}

	@Override
	public boolean edit(Object entity) {
		log.debug("template run method: edit({})", entity);
		
		return true;
	}

	@Override
	public boolean remove(Serializable id) {
		log.debug("template run method: remove({})", id);
		
		return true;
	}

	@Override
	public Object get(Serializable id) {
		log.debug("template run method: get({})", id);
		
		return true;
	}

	@Override
	public <T> List<T> list(Object entity) {
		log.debug("template run method: list({})", entity);
		
		return Lists.newArrayList();
	}

	@Override
	public <T> Page<T> queryForPage(Object entity, Class<T> classes) {
		log.debug("template run method: queryForPage({}, {})", entity, classes);
		
		return new PageImpl<>(Lists.newArrayList());
	}

	@Override
	public <S> S index(S entity) {
		log.debug("template run method: index({})", entity);
		
		return entity;
	}

	@Override
	public boolean invoke(String... args) {
		log.debug("template run method: invoke({})", new Object[] { args });
		
		return true;
	}

	@Override
	public String query(String params) {
		log.debug("template run method: query({})", params);
		
		return params;
	}

	@Override
	public Object queryForList(StringQuery stringQuery, Class<?> javaType) {
		log.debug("template run method: queryForList({}, {})", stringQuery, javaType);
		
		return "list";
	}

	@Override
	public Object queryForObject(StringQuery stringQuery, Class<?> javaType) {
		log.debug("template run method: queryForObject({}, {})", stringQuery, javaType);
		
		return null;
	}

	@Override
	public void delete(CriteriaQuery query, Class<?> javaType) {
		log.debug("template run method: delete({}, {})", query, javaType);
		
		build(query);
	}

	@Override
	public CloseableIterator<Object> stream(CriteriaQuery query, Class<?> entityType) {
		log.debug("template run method: delete({}, {})", query, entityType);
		
		build(query);
		return null;
	}

	@Override
	public int count(CriteriaQuery query, Class<?> javaType) {
		log.debug("template run method: count({}, {})", query, javaType);
		
		build(query);
		return 0;
	}

	@Override
	public Object queryForList(CriteriaQuery query, Class<?> javaType) {
		log.debug("template run method: queryForList({}, {})", query, javaType);
		
		build(query);
		return "list";
	}

	@Override
	public Object queryForObject(CriteriaQuery query, Class<?> javaType) {
		log.debug("template run method: queryForObject({}, {})", query, javaType);
		
		build(query);
		
		if (ClassUtils.isPrimitiveOrWrapper(javaType)) {
			return 1;
		} else {
			return null;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		if (converter instanceof ApplicationContextAware) {
			((ApplicationContextAware) converter).setApplicationContext(context);
		}
	}
	
	private void build(CriteriaQuery query) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("chain: " + Statement.buildChain(query));
		System.out.println("query: " + Statement.buildQuery(query));
		System.out.println("filter: " + Statement.buildFilter(query));
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
