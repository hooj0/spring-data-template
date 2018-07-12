package io.github.hooj0.springdata.template.core.convert;

import java.util.Date;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.convert.converter.Converter;

/**
 * 时间格式化转换器，在 ConversionService 中可以设置使用
 * @author hoojo
 * @createDate 2018年7月11日 上午9:55:03
 * @file DateTimeConverter.java
 * @package io.github.hooj0.springdata.template.core.convert
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class DateTimeConverter {

	private static DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

	public enum JodaDateTimeConverter implements Converter<ReadableInstant, String> {
		INSTANCE;

		@Override
		public String convert(ReadableInstant source) {
			if (source == null) {
				return null;
			}
			return formatter.print(source);
		}
	}

	public enum JodaLocalDateTimeConverter implements Converter<LocalDateTime, String> {
		INSTANCE;

		@Override
		public String convert(LocalDateTime source) {
			if (source == null) {
				return null;
			}
			return formatter.print(source.toDateTime(DateTimeZone.UTC));
		}
	}

	public enum JavaDateConverter implements Converter<Date, String> {
		INSTANCE;

		@Override
		public String convert(Date source) {
			if (source == null) {
				return null;
			}

			return formatter.print(source.getTime());
		}
	}
}
