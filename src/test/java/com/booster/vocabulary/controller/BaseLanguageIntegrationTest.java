package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.TestAuthenticationService;
import com.booster.vocabulary.config.TestBaseLanguageOperations;
import com.booster.vocabulary.dto.BaseLanguageDto;
import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.repository.BaseLanguageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true"
        }
)
@DisplayName("Base Language Integration Test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BaseLanguageIntegrationTest {

    @Autowired
    BaseLanguageRepository baseLanguageRepository;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    TestBaseLanguageOperations testBaseLanguageOperations;
    @Autowired
    TestAuthenticationService testAuthenticationService;

    @Value("${server.host:localhost}")
    String host;
    @Value("${local.server.port}")
    String port;

    @Test
    @DisplayName("/base-language/list")
    void baseLanguageList() {
        // given
        HttpHeaders httpHeaders = testAuthenticationService.getAuthorizationBearerHttpHeaders(host, port);

        var baseLanguageEntity1 = testBaseLanguageOperations.createBaseLanguageEntity("GERMAN");
        var baseLanguageDto1 = toBaseLanguageDto(baseLanguageEntity1);
        var baseLanguageEntity2 = testBaseLanguageOperations.createBaseLanguageEntity("ITALIAN");
        var baseLanguageDto2 = toBaseLanguageDto(baseLanguageEntity2);
        // when
        ResponseEntity<BaseLanguageController.BaseLanguageDtoList> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/base-language/list",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                BaseLanguageController.BaseLanguageDtoList.class,
                host, port
        );
        BaseLanguageController.BaseLanguageDtoList body = responseEntity.getBody();
        List<BaseLanguageDto> baseLanguageDtos = body.getBaseLanguageDtos();
        // then
        assertThat(baseLanguageDtos).containsOnly(baseLanguageDto1, baseLanguageDto2);
    }

    @Test
    @DisplayName("/base-language/{id}")
    void baseLanguageById() {
        // given
        HttpHeaders httpHeaders = testAuthenticationService.getAuthorizationBearerHttpHeaders(host, port);

        BaseLanguageEntity baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity("FRENCH");
        var expectedBaseLanguageDto = toBaseLanguageDto(baseLanguageEntity);
        // when
        ResponseEntity<BaseLanguageDto> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/base-language/{id}",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                BaseLanguageDto.class,
                host, port, baseLanguageEntity.getId()
        );
        BaseLanguageDto actualBaseLanguageDto = responseEntity.getBody();
        // then
        assertThat(actualBaseLanguageDto).isEqualTo(expectedBaseLanguageDto);
    }

    // TODO: AVOID CONVERSION TO DTO
    private BaseLanguageDto toBaseLanguageDto(BaseLanguageEntity baseLanguageEntity) {
        return BaseLanguageDto.builder()
                .id(baseLanguageEntity.getId())
                .name(baseLanguageEntity.getName())
                .build();
    }

}