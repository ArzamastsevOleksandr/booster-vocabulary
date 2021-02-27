package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.TestAuthenticationService;
import com.booster.vocabulary.config.TestBaseLanguageOperations;
import com.booster.vocabulary.config.TestLanguageToLearnOperations;
import com.booster.vocabulary.config.TestVocabularyOperations;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.repository.LanguageToLearnRepository;
import com.booster.vocabulary.repository.UserRepository;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true"
        }
)
@DisplayName("Vocabulary Entry Integration Test")
@Transactional
class VocabularyEntryIntegrationTest {

    static final String VOCABULARY_NAME1 = "VOCABULARY_NAME1";
    static final String BASE_LANGUAGE_NAME = "BASE_LANGUAGE_NAME";

    @Autowired
    UserRepository userRepository;
    @Autowired
    VocabularyRepository vocabularyRepository;
    @Autowired
    LanguageToLearnRepository languageToLearnRepository;
    @Autowired
    VocabularyEntryRepository vocabularyEntryRepository;

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
    @DisplayName("/vocabulary-entry/create")
    void create() {
        // given
        TestAuthenticationService.AuthenticationResponse authenticationResponse = testAuthenticationService.getAuthenticationResponse(host, port);

        var baseLanguageEntity = testBaseLanguageOperations.createBaseLanguageEntity(BASE_LANGUAGE_NAME);
        UserEntity userEntity = userRepository.findByUsername(authenticationResponse.getUsername()).get();

        var languageToLearnEntity = testLanguageToLearnOperations.createLanguageToLearnEntity(baseLanguageEntity, userEntity);
        VocabularyEntity vocabularyEntity = testVocabularyOperations.createVocabularyEntity(userEntity, languageToLearnEntity, VOCABULARY_NAME1);
        // when
        var vocabularyEntryRequestDto = VocabularyEntryRequestDto.builder()
                .word("strong")
                .synonyms(Set.of("powerful", "hefty"))
                .antonyms(Set.of("weak", "fragile"))
                .vocabularyId(vocabularyEntity.getId())
                .build();
        ResponseEntity<VocabularyEntryDto> responseEntity = restTemplate.exchange(
                "http://{host}:{port}/api/vocabulary-entry/create",
                HttpMethod.POST,
                new HttpEntity<>(vocabularyEntryRequestDto, authenticationResponse.getHttpHeaders()),
                VocabularyEntryDto.class,
                host, port
        );
        VocabularyEntryDto vocabularyEntryDto = responseEntity.getBody();
        // then
        VocabularyEntryEntity vocabularyEntryEntity = vocabularyEntryRepository.findById(vocabularyEntryDto.getId()).get();

        assertThat(vocabularyEntryEntity.getTargetWord().getName()).isEqualTo("strong");
        assertThat(vocabularyEntryEntity.getSynonyms())
                .extracting(WordEntity::getName)
                .containsOnly("powerful", "hefty");
        assertThat(vocabularyEntryEntity.getAntonyms())
                .extracting(WordEntity::getName)
                .containsOnly("weak", "fragile");
        assertThat(vocabularyEntryEntity.getCreatedOn()).isNotNull();
        assertThat(vocabularyEntryEntity.getCorrectAnswersCount()).isEqualTo(0);
        assertThat(vocabularyEntryEntity.getUser()).isEqualTo(userEntity);

        VocabularyEntity actualVocabularyEntity = vocabularyEntryEntity.getVocabulary();
        assertThat(actualVocabularyEntity).isEqualTo(vocabularyEntity);
        assertThat(actualVocabularyEntity.getVocabularyEntries()).containsOnly(vocabularyEntryEntity);
    }

}