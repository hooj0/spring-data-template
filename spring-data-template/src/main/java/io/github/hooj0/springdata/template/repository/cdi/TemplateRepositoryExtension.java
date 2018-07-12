package io.github.hooj0.springdata.template.repository.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessBean;

import org.springframework.data.repository.cdi.CdiRepositoryBean;
import org.springframework.data.repository.cdi.CdiRepositoryExtensionSupport;

import io.github.hooj0.springdata.template.core.TemplateOperations;

/**
 * 一个便携式CDI扩展，它为Spring Data Template存储库注册bean。
 * @author hoojo
 * @createDate 2018年7月4日 下午3:05:15
 * @file TemplateRepositoryExtension.java
 * @package io.github.hooj0.springdata.template.repository.cdi
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateRepositoryExtension extends CdiRepositoryExtensionSupport {
	
	private final Map<Set<Annotation>, Bean<TemplateOperations>> templateOperationsMap = new HashMap<>();

	/**
	 * 实现一个观察器，它检查 TemplateOperations bean并将它们存储在{@link #templateOperationsMap}中，
	 * 以便以后与相应的存储库bean相关联。
	 * @param <T> The type.
	 * @param processBean The annotated type as defined by CDI.
	 */
	@SuppressWarnings("unchecked")
	<T> void processBean(@Observes ProcessBean<T> processBean) {
		Bean<T> bean = processBean.getBean();
		for (Type type : bean.getTypes()) {
			if (type instanceof Class<?> && TemplateOperations.class.isAssignableFrom((Class<?>) type)) {
				templateOperationsMap.put(bean.getQualifiers(), ((Bean<TemplateOperations>) bean));
			}
		}
	}
	
	/**
	 * 实现一个观察者，它将bean注册到CDI容器中，用于检测到的Spring Data存储库。
	 * 存储库bean使用其限定符与EntityManagers相关联。 
	 * @author hoojo
	 * @createDate 2018年7月9日 上午10:22:35
	 * @param afterBeanDiscovery
	 * @param beanManager
	 */
	void afterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) {
		for (Entry<Class<?>, Set<Annotation>> entry : getRepositoryTypes()) {

			Class<?> repositoryType = entry.getKey();
			Set<Annotation> qualifiers = entry.getValue();

			CdiRepositoryBean<?> repositoryBean = createRepositoryBean(repositoryType, qualifiers, beanManager);
			afterBeanDiscovery.addBean(repositoryBean);
			registerBean(repositoryBean);
		}
	}
	
	/**
	 * 创建 TemplateRepositoryBean
	 * @author hoojo
	 * @createDate 2018年7月9日 上午10:24:10
	 */
	private <T> CdiRepositoryBean<T> createRepositoryBean(Class<T> repositoryType, Set<Annotation> qualifiers, BeanManager beanManager) {
		if (!this.templateOperationsMap.containsKey(qualifiers)) {
			throw new UnsatisfiedResolutionException(String.format("Unable to resolve a bean for '%s' with qualifiers %s.", TemplateOperations.class.getName(), qualifiers));
		}

		Bean<TemplateOperations> templateOperationsBean = this.templateOperationsMap.get(qualifiers);

		return new TemplateRepositoryBean<>(templateOperationsBean, qualifiers, repositoryType, beanManager, getCustomImplementationDetector());
	}
}
