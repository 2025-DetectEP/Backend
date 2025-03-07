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

	@Operation(summary = "게시물 상세 조회", description = "게시물 ID 기반 상세 게시물을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Ok",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{\n"
					+ "  \"code\": 200,\n"
					+ "  \"message\": \"OK\",\n"
					+ "  \"data\": {\n"
					+ "    \"createdTime\": \"2025-03-06T14:35:45\",\n"
					+ "    \"safeMessage\": null,\n"
					+ "    \"message\": \"2025년 2월 27일 ~ 3월 2일 대만 여행✈\\n\\n오랜만에 해외여행! 겨울이 끝나갈 즈음, 따뜻한 나라로 떠나고 싶어서 친구..."
					+ "    \"permalinkUrl\": \"https://www.facebook.com/122100024608769823/posts/122110815812769823\",\n"
					+ "    \"messageDetectRes\": [\n"
					+ "      {\n"
					+ "        \"detectWord\": \"오랜만\",\n"
					+ "        \"keyword\": \"LOCATION\",\n"
					+ "        \"likelihood\": \"LIKELY\",\n"
					+ "        \"startAt\": 29,\n"
					+ "        \"endAt\": 32\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"detectWord\": \"M12345678\",\n"
					+ "        \"keyword\": \"KOREA_PASSPORT\",\n"
					+ "        \"likelihood\": \"POSSIBLE\",\n"
					+ "        \"startAt\": 160,\n"
					+ "        \"endAt\": 169\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"detectWord\": \"타이베이\",\n"
					+ "        \"keyword\": \"LOCATION\",\n"
					+ "        \"likelihood\": \"LIKELY\",\n"
					+ "        \"startAt\": 256,\n"
					+ "        \"endAt\": 260\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"detectWord\": \"금광 마을\",\n"
					+ "        \"keyword\": \"LOCATION\",\n"
					+ "        \"likelihood\": \"LIKELY\",\n"
					+ "        \"startAt\": 626,\n"
					+ "        \"endAt\": 631\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"detectWord\": \"센\",\n"
					+ "        \"keyword\": \"PERSON_NAME\",\n"
					+ "        \"likelihood\": \"POSSIBLE\",\n"
					+ "        \"startAt\": 674,\n"
					+ "        \"endAt\": 675\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"detectWord\": \"치히로\",\n"
					+ "        \"keyword\": \"PERSON_NAME\",\n"
					+ "        \"likelihood\": \"POSSIBLE\",\n"
					+ "        \"startAt\": 677,\n"
					+ "        \"endAt\": 680\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"detectWord\": \"한국\",\n"
					+ "        \"keyword\": \"LOCATION\",\n"
					+ "        \"likelihood\": \"LIKELY\",\n"
					+ "        \"startAt\": 1110,\n"
					+ "        \"endAt\": 1112\n"
					+ "      }\n"
					+ "    ],\n"
					+ "    \"photoUrlRes\": [\n"
					+ "      {\n"
					+ "        \"pictureUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/481898235_122110815350769823_7830570790572853712_n.jpg?stp=dst-jpg_s960x960_tt6&_nc_cat=103&ccb=1-7&_nc_sid=127cfc&_nc_ohc=pqTxmMiaRmAQ7kNvgF_M2fo&_nc_oc=AdinZRm68WKJbnn4k8rmen-R1dYh-fVdY5Ln9wy9FUZzoYlqG6p3dTwjVynw9mzo0uM&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEzEWPriAvCi_kxV_i4eY7DSTzniaPtlf7qcPcVrlettQ&oe=67D07B53\",\n"
					+ "        \"photoDetectRes\": [\n"
					+ "          {\n"
					+ "            \"detectWord\": \"TAIPEI\",\n"
					+ "            \"keyword\": \"LOCATION\",\n"
					+ "            \"likelihood\": \"LIKELY\",\n"
					+ "            \"boundingBoxRes\": [\n"
					+ "              {\n"
					+ "                \"x\": 0,\n"
					+ "                \"y\": 0,\n"
					+ "                \"width\": 69,\n"
					+ "                \"height\": 130\n"
					+ "              }\n"
					+ "            ]\n"
					+ "          },\n"
					+ "          {\n"
					+ "            \"detectWord\": \"INCHEON\",\n"
					+ "            \"keyword\": \"LOCATION\",\n"
					+ "            \"likelihood\": \"LIKELY\",\n"
					+ "            \"boundingBoxRes\": [\n"
					+ "              {\n"
					+ "                \"x\": 0,\n"
					+ "                \"y\": 0,\n"
					+ "                \"width\": 74,\n"
					+ "                \"height\": 142\n"
					+ "              }\n"
					+ "            ]\n"
					+ "          },\n"
					+ "          {\n"
					+ "            \"detectWord\": \"SEOUL\",\n"
					+ "            \"keyword\": \"LOCATION\",\n"
					+ "            \"likelihood\": \"LIKELY\",\n"
					+ "            \"boundingBoxRes\": [\n"
					+ "              {\n"
					+ "                \"x\": 0,\n"
					+ "                \"y\": 0,\n"
					+ "                \"width\": 61,\n"
					+ "                \"height\": 112\n"
					+ "              }\n"
					+ "            ]\n"
					+ "          }\n"
					+ "        ]\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"pictureUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/481660333_122110815518769823_8695251980723261352_n.jpg?stp=dst-jpg_s960x960_tt6&_nc_cat=107&ccb=1-7&_nc_sid=127cfc&_nc_ohc=tNw5uyzpP4gQ7kNvgFaUSzy&_nc_oc=AdgNS_NCXQvdYi0SgeISTbhNFApOfOkD1Sl_JdC6fvn7fpMSPJBJkz6ouL23GQCQfuI&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYFp1fMpIXr7D6TfKutspe9qE-b1q_3xwJ0RWlOOerkOvA&oe=67D06CCD\",\n"
					+ "        \"photoDetectRes\": []\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"pictureUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/482017104_122110815512769823_160186683350267742_n.jpg?stp=dst-jpg_s960x960_tt6&_nc_cat=101&ccb=1-7&_nc_sid=127cfc&_nc_ohc=tm6myDkjx3MQ7kNvgF5sYwJ&_nc_oc=AdhOyAWpZwVmm2tD2EEkbkhPotc9zV-WEOKIHDIL0TdDQZynqgcQAd0Rg_9wnqXUa-Y&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYHz8uyuq3j_H8Bpk_L4b2eMtQQF03OajuYazQouyt-SzQ&oe=67D0555D\",\n"
					+ "        \"photoDetectRes\": []\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"pictureUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/481771934_122110815506769823_5550061559215650391_n.jpg?stp=dst-jpg_p720x720_tt6&_nc_cat=107&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OL0Fw7PcimsQ7kNvgHOKkl5&_nc_oc=AdhoVSqUqxSa0f67GtTVnVZAyLWXH4ZZQ0RS5n9rZaSbj1bDkuuA79MDFkGcEQfVW2E&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYFHmdkt3hxwMgV-JVz6U6HFM8g5VICr0lHYYPSipDHZgQ&oe=67D04EA9\",\n"
					+ "        \"photoDetectRes\": []\n"
					+ "      },\n"
					+ "      {\n"
					+ "        \"pictureUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/482010082_122110815596769823_3206669003289189272_n.jpg?stp=dst-jpg_s960x960_tt6&_nc_cat=101&ccb=1-7&_nc_sid=127cfc&_nc_ohc=0NL_Dl9eJPIQ7kNvgH_LG7T&_nc_oc=AdiJ1S12kB4r37eXclp5Sn7TsGKWQvkaMhb7Zci2YfSDRtUqFz3qe0A9TmkVCn8OE5g&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYFDtbA0NTz8R74OOg1eBFY5YrdvuMZ_OiT5KI4-xFI39Q&oe=67D048A2\",\n"
					+ "        \"photoDetectRes\": []\n"
					+ "      }\n"
					+ "    ]\n"
					+ "  }\n"
					+ "}")
			)
		),
		@ApiResponse(responseCode = "404", description = "해당 자원을 찾을 수 없습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "{ \"code\": 404, \"message\": \"권한이 없거나 존재하지 않는 게시물입니다.\" }")
			)
		)
	})
	ResponseEntity<ResponseDto> getDetailPost(Member member, Long postId);

	@Operation(summary = "게시물 전체 조회", description = "전체 게시물을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Ok",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{\n"
					+ "  \"code\": 200,\n"
					+ "  \"message\": \"OK\",\n"
					+ "  \"data\": {\n"
					+ "    \"totalPostCount\": 10,\n"
					+ "    \"detectPostCount\": 8,\n"
					+ "    \"averageDangerScore\": 46.25,\n"
					+ "    \"fbPostResPage\": {\n"
					+ "      \"data\": [\n"
					+ "        {\n"
					+ "          \"id\": 151,\n"
					+ "          \"createdTime\": \"2025-03-06T20:41:12\",\n"
					+ "          \"firstPhotoUrl\": null,\n"
					+ "          \"detectedKeywords\": [\n"
					+ "            \"PERSON_NAME\",\n"
					+ "            \"PHONE_NUMBER\",\n"
					+ "            \"EMAIL_ADDRESS\"\n"
					+ "          ]\n"
					+ "        },\n"
					+ "        {\n"
					+ "          \"id\": 152,\n"
					+ "          \"createdTime\": \"2025-03-06T15:01:36\",\n"
					+ "          \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/482005688_122110821902769823_5883437262206830682_n.jpg?stp=cp1_dst-jpegr_s960x960_tt6&_nc_cat=109&ccb=1-7&_nc_sid=127cfc&_nc_ohc=_89orQPXAU4Q7kNvgHHDDfF&_nc_oc=Adis2DKgtqlPMSInKT1rq0EdMJk6HmxfVGCXD0dV3Tjoo34wIBMFr4wRhjgTpLtCcaA&_nc_zt=23&se=-1&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEhMlsoqOMBiXKi-Zaa3UBaSD3oSBvmqZspg37f3cG94A&oe=67D04B41\",\n"
					+ "          \"detectedKeywords\": [\n"
					+ "            \"LOCATION\"\n"
					+ "          ]\n"
					+ "        },\n"
					+ "        {\n"
					+ "          \"id\": 153,\n"
					+ "          \"createdTime\": \"2025-03-06T14:57:00\",\n"
					+ "          \"firstPhotoUrl\": \"https://scontent-gmp1-1.xx.fbcdn.net/v/t39.30808-6/481820902_122110819862769823_6805450053096602031_n.jpg?stp=dst-jpg_p720x720_tt6&_nc_cat=106&ccb=1-7&_nc_sid=127cfc&_nc_ohc=4QVBrscw0c8Q7kNvgELjzdr&_nc_oc=AdhCgQD2zlsa4Hg825zj-DKr4ap131fSquivJ46rCh7kRs5ggPSEZ52ooD_TfcsR7zU&_nc_zt=23&_nc_ht=scontent-gmp1-1.xx&edm=AP4hL3IEAAAA&_nc_gid=APgMhPiomivhYaHlQ0N7ogZ&oh=00_AYEjnM7gBNxhuTBrvgLmhy3CfgTTnCLhHN1H3EAZDqqBiw&oe=67D0502C\",\n"
					+ "          \"detectedKeywords\": [\n"
					+ "            \"PERSON_NAME\",\n"
					+ "            \"PHONE_NUMBER\",\n"
					+ "            \"LOCATION\"\n"
					+ "          ]\n"
					+ "        }\n"
					+ "      ],\n"
					+ "      \"totalElements\": 10,\n"
					+ "      \"totalPages\": 4,\n"
					+ "      \"size\": 3,\n"
					+ "      \"numberOfElements\": 3\n"
					+ "    }\n"
					+ "  }\n"
					+ "}")
			)
		),
	})
	ResponseEntity<ResponseDto> getTotalPost(Member member, DetectKeyword detectType, String keyword, int page,
		int size);
}

