package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.TestAuthenticationService;
import com.booster.vocabulary.config.TestBaseLanguageOperations;
import com.booster.vocabulary.config.TestLanguageToLearnOperations;
import com.booster.vocabulary.config.TestVocabularyOperations;
import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.request.LanguageToLearnRequestDto;
import com.booster.vocabulary.dto.request.VocabularyRequestDto;
import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.repository.LanguageToLearnRepository;
import com.booster.vocabulary.repository.UserRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true"
        }
)
@DisplayName("Vocabulary Integration Test")
@Transactional
class VocabularyIntegrationTest {

    static final String VOCABULARY_NAME = "VOCABULARY_NAME";
    static final String BASE_LANGUAGE_NAME = "BASE_LANGUAGE_NAME";

    @Autowired
    UserRepository userRepository;
    @Autowired
    VocabularyRepository vocabularyRepository;
    @Autowired
    LanguageToLearnRepository languageToLearnRepository;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    TestBaseLanguageOperations testBaseLanguageOperations;
    @Autowired
    TestLanguageToLearnOperations testLanguageToLearnOperations;
    @Autowired
    TestVocabularyOperations testVocabularyOperations;
    @Autowired
    TestAuthenticationService testAuthenticationService;

    @Value("${server.host:localhost}")
    String host;
    @Value("${local.server.port}")
    String port;

    @Test
    @DisplayName("/vocabulary/create")
    void createVocabulary() {
        // given
        HttpHeaders httpHeaders = testAuthenticationService.getAuthorizationBearerHttpHeaders(host, port);
        var baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity(BASE_LANGUAGE_NAME);

        var languageToLearnRequestDto = LanguageToLearnRequestDto.builder()
                .baseLanguageId(baseLanguageEntity.getId())
                .build();
        ResponseEntity<LanguageToLearnDto> responseEntity1 = restTemplate.exchange(
                "http://{host}:{port}/api/language-to-learn/create",
                HttpMethod.POST,
                new HttpEntity<>(languageToLearnRequestDto, httpHeaders),
                LanguageToLearnDto.class,
                host, port
        );
        LanguageToLearnDto languageToLearnDto = responseEntity1.getBody();
        // when
        var vocabularyRequestDto = VocabularyRequestDto.builder()
                .vocabularyName(VOCABULARY_NAME)
                .languageToLearnId(languageToLearnDto.getId())
                .build();
        ResponseEntity<VocabularyDto> responseEntity2 = restTemplate.exchange(
                "http://{host}:{port}/api/vocabulary/create",
                HttpMethod.POST,
                new HttpEntity<>(vocabularyRequestDto, httpHeaders),
                VocabularyDto.class,
                host, port
        );
        VocabularyDto vocabularyDto = responseEntity2.getBody();
        // then
        LanguageToLearnEntity languageToLearnEntity = languageToLearnRepository.findById(languageToLearnDto.getId()).get();
        BaseLanguageEntity baseLanguageEntity1 = languageToLearnEntity.getBaseLanguage();

        VocabularyEntity vocabularyEntity = vocabularyRepository.findById(vocabularyDto.getId()).get();

        assertThat(vocabularyEntity.getBaseLanguage()).isEqualTo(baseLanguageEntity1);
        assertThat(vocabularyEntity.getLanguageToLearn()).isEqualTo(languageToLearnEntity);
        assertThat(vocabularyEntity.getName()).isEqualTo(VOCABULARY_NAME);
        assertThat(vocabularyEntity.getCreatedOn()).isNotNull();
        assertThat(vocabularyEntity.getUser()).isNotNull();
        assertThat(vocabularyEntity.getVocabularyEntries()).isEmpty();
    }

    @Test
    @DisplayName("/vocabulary/list/{languageToLearnId}")
    void listAllByLanguageToLearnId() {
        // given
        TestAuthenticationService.AuthenticationResponse authenticationResponse = testAuthenticationService.getAuthenticationResponse(host, port);

        var baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity(BASE_LANGUAGE_NAME);
        UserEntity userEntity = userRepository.findByUsername(authenticationResponse.getUsername()).get();

        var languageToLearnEntity = testLanguageToLearnOperations.createLanguageToLearnEntity(baseLanguageEntity, userEntity);
        VocabularyEntity vocabularyEntity1 = testVocabularyOperations.createVocabularyEntity(userEntity, languageToLearnEntity, "ONE");
        VocabularyEntity vocabularyEntity2 = testVocabularyOperations.createVocabularyEntity(userEntity, languageToLearnEntity, "TWO");
        // when
        ResponseEntity<VocabularyController.VocabularyDtoList> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/vocabulary/list/{languageToLearnId}",
                HttpMethod.GET,
                new HttpEntity<>(authenticationResponse.getHttpHeaders()),
                VocabularyController.VocabularyDtoList.class,
                host, port, languageToLearnEntity.getId()
        );
        List<VocabularyDto> vocabularyDtoList = responseEntity.getBody().getVocabularyDtoList();
        // then
        assertThat(vocabularyDtoList).hasSize(2);
        assertThat(vocabularyDtoList)
                .extracting(VocabularyDto::getId)
                .containsOnly(vocabularyEntity1.getId(), vocabularyEntity2.getId());
    }

}