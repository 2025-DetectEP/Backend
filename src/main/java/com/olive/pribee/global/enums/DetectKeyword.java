package com.olive.pribee.global.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum DetectKeyword {
	PERSON_NAME("PERSON_NAME", "이름", 10),
	GENDER("GENDER", "성별", 10),
	AGE("AGE", "나이", 10),
	LOCATION("LOCATION", "주소", 10),
	EMAIL_ADDRESS("EMAIL_ADDRESS", "이메일", 10),
	PHONE_NUMBER("PHONE_NUMBER", "전화번호", 10),
	IP_ADDRESS("IP_ADDRESS", "IP 주소", 5),
	MAC_ADDRESS("MAC_ADDRESS", "MAC 주소", 5),
	FINANCIAL_ACCOUNT_NUMBER("FINANCIAL_ACCOUNT_NUMBER", "계좌번호", 20),
	CREDIT_CARD_NUMBER("CREDIT_CARD_NUMBER", "카드번호", 20),
	IBAN_CODE("IBAN_CODE", "국제 계좌번호", 20),
	MEDICAL_RECORD_NUMBER("MEDICAL_RECORD_NUMBER", "의료 기록 번호", 20),
	KOREA_RRN("KOREA_RRN", "주민등록번호", 30),
	KOREA_DRIVERS_LICENSE_NUMBER("KOREA_DRIVERS_LICENSE_NUMBER", "운전면허번호", 30),
	KOREA_PASSPORT("KOREA_PASSPORT", "여권번호", 30);

	private final String code;
	private final String name;
	private final int score;

	private DetectKeyword(String code, String name, int score) {
		this.code = code;
		this.name = name;
		this.score = score;
	}

	public static DetectKeyword of(String code) {
		return Arrays.stream(DetectKeyword.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElse(null);
	}

	public static List<String> getInfoTypes() {
		return Arrays.stream(values())
			.map(keyword -> keyword.code)
			.toList();
	}

}
