package com.olive.pribee.module.feed.controller;

import org.springframework.http.ResponseEntity;

import com.olive.pribee.global.common.ResponseDto;
import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.module.auth.domain.entity.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Feed", description = "게시물 관련 API")
public interface FbPostControllerDocs {
	@Operation(summary = "게시물 조회", description = "전체 게시물을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Ok",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{\n"
					+ "  \"code\": 200,\n"
					+ "  \"message\": \"OK\",\n"
					+ "  \"data\": [\n"
					+ "    {\n"
					+ "      \"id\": 151,\n"
					+ "      \"createdTime\": \"2025-03-06T20:41:12\",\n"
					+ "      \"firstPhotoUrl\": null,\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"PERSON_NAME\",\n"
					+ "        \"PHONE_NUMBER\",\n"
					+ "        \"EMAIL_ADDRESS\"\n"
					+ "      ]\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 152,\n"
					+ "      \"createdTime\": \"2025-03-06T15:01:36\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/482005688_122110821902769823_5883437262206830682_n.jpg?stp=cp1_dst-jpegr_s960x960_tt6&_nc_cat=109&ccb=1-7&_nc_sid=127cfc&_nc_ohc=_89orQPXAU4Q7kNvgHHDDfF&_nc_oc=Adis2DKgtqlPMSInKT1rq0EdMJk6HmxfVGCXD0dV3Tjoo34wIBMFr4wRhjgTpLtCcaA&_nc_zt=23&se=-1&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEhMlsoqOMBiXKi-Zaa3UBaSD3oSBvmqZspg37f3cG94A&oe=67D04B41\",\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"LOCATION\"\n"
					+ "      ]\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 153,\n"
					+ "      \"createdTime\": \"2025-03-06T14:57:00\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/481820902_122110819862769823_6805450053096602031_n.jpg?stp=dst-jpg_p720x720_tt6&_nc_cat=106&ccb=1-7&_nc_sid=127cfc&_nc_ohc=4QVBrscw0c8Q7kNvgELjzdr&_nc_oc=AdhCgQD2zlsa4Hg825zj-DKr4ap131fSquivJ46rCh7kRs5ggPSEZ52ooD_TfcsR7zU&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEjnM7gBNxhuTBrvgLmhy3CfgTTnCLhHN1H3EAZDqqBiw&oe=67D0502C\",\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"PERSON_NAME\",\n"
					+ "        \"PHONE_NUMBER\",\n"
					+ "        \"LOCATION\"\n"
					+ "      ]\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 154,\n"
					+ "      \"createdTime\": \"2025-03-06T14:40:15\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/482006157_122110816010769823_4822934692025284850_n.jpg?stp=dst-jpg_p720x720_tt6&_nc_cat=103&ccb=1-7&_nc_sid=127cfc&_nc_ohc=KlCInOtXz28Q7kNvgEvqHN8&_nc_oc=AdjDKcRdysEHQpSEO879poNXbnmxaC9b6Q76zyGCFaZjI7ZSPOFWRjPTBMK68iTIM7U&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEX_qhEoL8SnhCEj5rLYYEwsELQyz5jYm3FC_Frf9D9Hw&oe=67D07164\",\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"PERSON_NAME\",\n"
					+ "        \"LOCATION\"\n"
					+ "      ]\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 155,\n"
					+ "      \"createdTime\": \"2025-03-06T14:35:45\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/481898235_122110815350769823_7830570790572853712_n.jpg?stp=dst-jpg_s960x960_tt6&_nc_cat=103&ccb=1-7&_nc_sid=127cfc&_nc_ohc=pqTxmMiaRmAQ7kNvgF_M2fo&_nc_oc=AdinZRm68WKJbnn4k8rmen-R1dYh-fVdY5Ln9wy9FUZzoYlqG6p3dTwjVynw9mzo0uM&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEzEWPriAvCi_kxV_i4eY7DSTzniaPtlf7qcPcVrlettQ&oe=67D07B53\",\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"LOCATION\",\n"
					+ "        \"KOREA_PASSPORT\",\n"
					+ "        \"PERSON_NAME\"\n"
					+ "      ]\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 156,\n"
					+ "      \"createdTime\": \"2025-03-06T14:30:10\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/481784709_122110814570769823_1075445356216340862_n.jpg?stp=dst-jpg_s720x720_tt6&_nc_cat=105&ccb=1-7&_nc_sid=127cfc&_nc_ohc=akejy1pFTa0Q7kNvgE9XOoT&_nc_oc=Adg34RMotqKCf1aJJAMqtuREdnJ9ylGTvIzVdGfHbXZftniJzyMuimRrC_Wfo7HdGl4&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEZ135NtAgXdENzv8jaUqSTc9b9K4SIOGgrvQQqQdwZyA&oe=67D06263\",\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"PHONE_NUMBER\"\n"
					+ "      ]\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 157,\n"
					+ "      \"createdTime\": \"2025-03-06T12:04:58\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/482000767_122110765268769823_7669289345773660085_n.jpg?stp=dst-jpg_p720x720_tt6&_nc_cat=102&ccb=1-7&_nc_sid=127cfc&_nc_ohc=uJUeAzILfEAQ7kNvgE7TfMb&_nc_oc=AdgepuB4YVwf55XUeu9-8y-cr_rmliSInY3FAYhpT63pKsjb9eL_N2p-jigE9ZCJpRo&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEcpbr8H07mfjd6pB9WGwWx66r6ij4Flr19Wdv9dNWR4w&oe=67D06C87\",\n"
					+ "      \"detectedKeywords\": []\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 158,\n"
					+ "      \"createdTime\": \"2025-03-06T11:57:26\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/482023063_122110760102769823_4351467489794574342_n.jpg?stp=dst-jpg_p720x720_tt6&_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OGF2WaeTx20Q7kNvgEAI-yv&_nc_oc=AdjU9rcdQLeDH_HZcwdgFlhNo5JGFKTFmQi5MKgTzBIDSw5_99ll42yHGYQiwQU3SM4&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYFYX1e3_iuFWGzn-GeWuYgWdDWa4IBU7fMCGRNvhSq4xA&oe=67D05993\",\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"PERSON_NAME\",\n"
					+ "        \"LOCATION\"\n"
					+ "      ]\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 159,\n"
					+ "      \"createdTime\": \"2025-02-26T06:44:10\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t51.75761-15/481183925_17845332873425342_9111973536618245990_n.jpg?stp=dst-jpg_p720x720_tt6&_nc_cat=106&ccb=1-7&_nc_sid=127cfc&_nc_ohc=tGJeHDNelXUQ7kNvgFkn75g&_nc_oc=AdgigxklAKeh_KvLXY-OITdWuG5Skm4ZgxrNyllvej-XbBcqrhZ894tG8M-yrrIGcJM&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEoZ1MUAsbf-JCOLxGRTH5qKKIGf3ANu4dq1qaP7giNMQ&oe=67D0597B\",\n"
					+ "      \"detectedKeywords\": []\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 160,\n"
					+ "      \"createdTime\": \"2025-02-26T05:53:39\",\n"
					+ "      \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t51.75761-15/480981728_17845326564425342_6913190954196226453_n.jpg?stp=dst-jpg_s720x720_tt6&_nc_cat=103&ccb=1-7&_nc_sid=127cfc&_nc_ohc=bOuIQ3JTMHcQ7kNvgEYt7zo&_nc_oc=Adg5hRwzuNW2Xjx6lJBkl_7JXv3pfkTAMTydiGSX7Zvc8e4TWYfBXcx_ugva3lscHZA&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYG0asG6FVLmE_eW2OA6dYFXakMs5tJQx5mMCMbYzo8JxA&oe=67D0711C\",\n"
					+ "      \"detectedKeywords\": [\n"
					+ "        \"LOCATION\"\n"
					+ "      ]\n"
					+ "    }\n"
					+ "  ],\n"
					+ "  \"totalElements\": 10,\n"
					+ "  \"totalPages\": 1,\n"
					+ "  \"size\": 100,\n"
					+ "  \"numberOfElements\": 10\n"
					+ "}")
			)
		),
	})
	ResponseEntity<ResponseDto> getExhibitions(Member member, DetectKeyword detectType, String keyword, int page,
		int size);
}

