package io.github.hoojo.example.entity;

import lombok.Data;
import lombok.ToString;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月16日 上午9:24:33
 * @file User.java
 * @package io.github.hoojo.example.entity
 * @project spring-data-template-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Data
@ToString
public class User {

	private Long id;
	private String name;
	private String nickName;
	private boolean sex;
	private int age;
}
