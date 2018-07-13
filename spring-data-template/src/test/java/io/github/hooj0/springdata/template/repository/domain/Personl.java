package io.github.hooj0.springdata.template.repository.domain;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import io.github.hooj0.springdata.template.annotations.Field;
import io.github.hooj0.springdata.template.annotations.Model;
import io.github.hooj0.springdata.template.annotations.Score;
import io.github.hooj0.springdata.template.annotations.Setting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <b>function:</b>
 * 
 * @author hoojo
 * @createDate 2018年7月13日 下午5:21:03
 * @file Personl.java
 * @package io.github.hooj0.springdata.template.repository.domain
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Model(indexName = "Persion")
@Setting(settingPath = "/aaa/bbb/xxx/person")
public class Personl {

	@Id
	private final BigInteger number;
	@Score
	float socre;
	@Field
	String name;
	@Version
	Long version;
	Date date;
}
