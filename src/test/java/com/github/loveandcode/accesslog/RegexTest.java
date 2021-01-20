package com.github.loveandcode.accesslog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegexTest {

	@Test
	@DisplayName("정규 표현식 테스트")
	void regex_test() {
		String login = "{\n"
			+ "  \"email\": \"practice1356@gmail.com\",\n"
			+ "  \"password\": \"1234\"\n"
			+ "}";

		login = login.replaceAll("(?<=\"password\":\")[^\"]+","");

		System.out.println(login);

		Pattern pattern = Pattern.compile("(?<=\"password\":\")[^\"]+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(login);

		if(matcher.find()){
			String find = matcher.group();
			System.out.println(find);
			Assertions.assertThat(find).isNotBlank();
		}
	}
}
