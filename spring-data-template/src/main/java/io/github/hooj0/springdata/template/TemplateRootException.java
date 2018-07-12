package io.github.hooj0.springdata.template;

/**
 * <b>function:</b> 定义异常类，处理通用异常信息
 * @author hoojo
 * @createDate 2018年7月10日 上午10:23:19
 * @file TemplateRootException.java
 * @package io.github.hooj0.springdata.template
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TemplateRootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TemplateRootException(String string, Exception e) {
		super(string, e);
	}

	public TemplateRootException(String string) {
		super(string);
	}
}
