package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.TestAuthenticationService;
import com.booster.vocabulary.config.TestBaseLanguageOperations;
import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.dto.request.LanguageToLearnRequestDto;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.repository.LanguageToLearnRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true"
        }
)
@DisplayName("Language To Learn Integration Test")
@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LanguageToLearnIntegrationTest {

    static final String DEFAULT_VOCABULARY_NAME = "DEFAULT_VOCABULARY";

    @Autowired
    EntityManager entityManager;

    @Autowired
    VocabularyRepository vocabularyRepository;
    @Autowired
    LanguageToLearnRepository languageToLearnRepository;

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
    @DisplayName("/language-to-learn/create")
    void createLanguageToLearn() {
        // TODO: TEST DTO CONTENT
        // given
        HttpHeaders httpHeaders = testAuthenticationService.getAuthorizationBearerHttpHeaders(host, port);
        var baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity("GERMAN");
        String baseLanguageEntityId = baseLanguageEntity.getId();

        // when
        var languageToLearnRequestDto = LanguageToLearnRequestDto.builder()
                .baseLanguageId(baseLanguageEntityId)
                .build();
        ResponseEntity<LanguageToLearnDto> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/create",
                HttpMethod.POST,
                new HttpEntity<>(languageToLearnRequestDto, httpHeaders),
                LanguageToLearnDto.class,
                host, port
        );
        // then
        LanguageToLearnDto languageToLearnDto = responseEntity.getBody();

        LanguageToLearnEntity languageToLearnEntity = languageToLearnRepository.findById(languageToLearnDto.getId()).get();
        assertThat(languageToLearnEntity.getBaseLanguage().getId()).isEqualTo(baseLanguageEntityId);
        assertThat(languageToLearnEntity.getUser()).isNotNull();
        assertThat(languageToLearnEntity.getCreatedOn()).isNotNull();

        List<VocabularyEntity> vocabularyEntities = languageToLearnEntity.getVocabularies();
        assertThat(vocabularyEntities).hasSize(1);

        VocabularyEntity vocabularyEntity = vocabularyEntities.get(0);
        assertThat(vocabularyEntity.getName()).isEqualTo(DEFAULT_VOCABULARY_NAME);
        assertThat(vocabularyEntity.getUser()).isNotNull();
        assertThat(vocabularyEntity.getBaseLanguage().getId()).isEqualTo(baseLanguageEntityId);
        assertThat(vocabularyEntity.getCreatedOn()).isNotNull();
    }

    @Test
    @DisplayName("/language-to-learn/list")
    void list() {
        // TODO: TEST DTO CONTENT
        // given
        HttpHeaders httpHeaders = testAuthenticationService.getAuthorizationBearerHttpHeaders(host, port);
        var baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity("GERMAN");

        var languageToLearnRequestDto = LanguageToLearnRequestDto.builder()
                .baseLanguageId(baseLanguageEntity.getId())
                .build();
        ResponseEntity<LanguageToLearnDto> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/create",
                HttpMethod.POST,
                new HttpEntity<>(languageToLearnRequestDto, httpHeaders),
                LanguageToLearnDto.class,
                host, port
        );
        LanguageToLearnDto expectedLanguageToLearnDto = responseEntity.getBody();
        // when
        ResponseEntity<LanguageToLearnController.LanguageToLearnDtoList> responseEntity1 = restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/list",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                LanguageToLearnController.LanguageToLearnDtoList.class,
                host, port
        );
        List<LanguageToLearnDto> languageToLearnDtoList = responseEntity1.getBody().getLanguageToLearnDtoList();
        // then
        assertThat(languageToLearnDtoList).hasSize(1);
        LanguageToLearnDto actualLanguageToLearnDto = languageToLearnDtoList.get(0);

        assertThat(actualLanguageToLearnDto.getId()).isEqualTo(expectedLanguageToLearnDto.getId());
    }

    @Test
    @DisplayName("find: /language-to-learn/{id}")
    void findById() {
        // TODO: TEST DTO CONTENT
        // given
        HttpHeaders httpHeaders = testAuthenticationService.getAuthorizationBearerHttpHeaders(host, port);
        var baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity("GERMAN");

        var languageToLearnRequestDto = LanguageToLearnRequestDto.builder()
                .baseLanguageId(baseLanguageEntity.getId())
                .build();
        ResponseEntity<LanguageToLearnDto> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/create",
                HttpMethod.POST,
                new HttpEntity<>(languageToLearnRequestDto, httpHeaders),
                LanguageToLearnDto.class,
                host, port
        );
        LanguageToLearnDto expectedLanguageToLearnDto = responseEntity.getBody();
        // when
        ResponseEntity<LanguageToLearnDto> responseEntity1 = restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/{id}",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                LanguageToLearnDto.class,
                host, port, expectedLanguageToLearnDto.getId()
        );
        LanguageToLearnDto actualLanguageToLearnDto = responseEntity1.getBody();
        // then
        assertThat(actualLanguageToLearnDto.getId()).isEqualTo(expectedLanguageToLearnDto.getId());
        assertThat(actualLanguageToLearnDto.getBaseLanguageDto().getId()).isEqualTo(baseLanguageEntity.getId());
        assertThat(actualLanguageToLearnDto.getCreatedOn()).isNotNull();
        // TODO: TEST VOCABULARY DTOS
    }

    @Test
    @DisplayName("delete: /language-to-learn/{id}")
    void deleteById() {
        // given
        HttpHeaders httpHeaders = testAuthenticationService.getAuthorizationBearerHttpHeaders(host, port);
        var baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity("GERMAN");

        var languageToLearnRequestDto = LanguageToLearnRequestDto.builder()
                .baseLanguageId(baseLanguageEntity.getId())
                .build();
        ResponseEntity<LanguageToLearnDto> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/create",
                HttpMethod.POST,
                new HttpEntity<>(languageToLearnRequestDto, httpHeaders),
                LanguageToLearnDto.class,
                host, port
        );
        LanguageToLearnDto expectedLanguageToLearnDto = responseEntity.getBody();

        assertThat(languageToLearnRepository.findById(expectedLanguageToLearnDto.getId())).isNotEmpty();
        // when
        restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(httpHeaders),
                Void.class,
                host, port, expectedLanguageToLearnDto.getId()
        );
        entityManager.clear();
        // then
        assertThat(languageToLearnRepository.findById(expectedLanguageToLearnDto.getId())).isEmpty();
        // TODO: TEST THAT VOCABULARIES & ENTRIES ARE DELETED
    }

}
