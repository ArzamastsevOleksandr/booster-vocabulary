package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.request.VocabularyRequestDto;
import com.booster.vocabulary.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/vocabulary")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<VocabularyDto> create(@RequestBody VocabularyRequestDto vocabularyRequestDto) {
        String userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        vocabularyRequestDto.setUserId(userId);

        VocabularyDto vocabularyDto = vocabularyService.create(vocabularyRequestDto);
        return ResponseEntity.ok(vocabularyDto);
    }

    @GetMapping("/list/{languageToLearnId}")
    ResponseEntity<List<VocabularyDto>> findAllByLanguageToLearnId(@PathVariable String languageToLearnId) {
        String userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(vocabularyService.findAllByUserIdAndLanguageToLearnId(userId, languageToLearnId));
    }

    @GetMapping("/{id}")
    ResponseEntity<VocabularyDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(vocabularyService.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable String id) {
        vocabularyService.deleteById(id);
    }

}
