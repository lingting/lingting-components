package live.lingting.component.validation.url.validator;

import live.lingting.component.validation.url.annotation.IsFileURL;
import live.lingting.component.validation.url.annotation.IsFtpURL;
import live.lingting.component.validation.url.annotation.IsHttpURL;
import live.lingting.component.validation.url.annotation.IsURL;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2023-12-20 20:36
 */
class UrlValidatorTest {

	Validator validator;

	@BeforeEach
	void before() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			validator = factory.getValidator();
		}

	}

	@Test
	void success() {
		UrlValidatorObject object = new UrlValidatorObject();
		object.http = "http://www.baidu.com/robot.txt";
		object.https = "https://www.baidu.com/robot.txt";
		object.ftp = "ftp://www.baidu.com/robot.txt";
		object.file = "file://www.baidu.com/robot.txt";
		object.notHttp = "file://www.baidu.com/robot.txt";
		object.notFtp = "file://www.baidu.com/robot.txt";
		object.notFile = "https://www.baidu.com/robot.txt";

		Set<ConstraintViolation<UrlValidatorObject>> violations = validator.validate(object);
		assertTrue(violations.isEmpty());
	}

	@Test
	void failed() {
		UrlValidatorObject object = new UrlValidatorObject();
		object.http = "ftp://www.baidu.com/robot.txt";
		object.https = "http://www.baidu.com/robot.txt";
		object.ftp = "file://www.baidu.com/robot.txt";
		object.file = "http://www.baidu.com/robot.txt";
		object.notHttp = "https://www.baidu.com/robot.txt";
		object.notFtp = "ftp://www.baidu.com/robot.txt";
		object.notFile = "file://www.baidu.com/robot.txt";
		object.empty = "胡乱写的";

		Set<ConstraintViolation<UrlValidatorObject>> violations = validator.validate(object);
		assertFalse(violations.isEmpty());
		assertEquals(8, violations.size());
	}

	@Getter
	static class UrlValidatorObject {

		@IsHttpURL
		private String http;

		@IsHttpURL(notHttp = true)
		private String https;

		@IsFtpURL
		private String ftp;

		@IsFileURL
		private String file;

		@IsURL(notHttp = true)
		private String notHttp;

		@IsURL(notFtp = true)
		private String notFtp;

		@IsURL(notFile = true)
		private String notFile;

		@IsURL(allowEmpty = true)
		private String empty;

	}

}
