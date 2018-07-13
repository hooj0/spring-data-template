package io.github.hooj0.springdata.template.repository.complex;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import io.github.hooj0.springdata.template.core.MyTplTemplate;

/**
 * <b>function:</b> 实现自定义接口方法 
 * @author hoojo
 * @createDate 2018年7月13日 上午11:15:18
 * @file CustomMethodRepositoryImpl.java
 * @package io.github.hooj0.springdata.template.repository.complex
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CustomMethodRepositoryImpl implements CustomMethodRepository {

	@Autowired
	@Qualifier("myTplTemplate")
	private MyTplTemplate template;
	
	@Override
	public String doSomethingSpecial() {
		checkNotNull(template.getTemplateConverter(), "converter not null");
		
		return "implements special method!";
	}

}
