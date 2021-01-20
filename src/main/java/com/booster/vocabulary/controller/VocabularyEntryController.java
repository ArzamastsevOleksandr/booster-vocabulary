package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.service.VocabularyEntryService;
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
@RequestMapping("/vocabulary-entry")
public class VocabularyEntryController {

    private final VocabularyEntryService vocabularyEntryService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<VocabularyEntryDto> create(@RequestBody VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        vocabularyEntryRequestDto.setUserId(userId);

        VocabularyEntryDto vocabularyEntryDto = vocabularyEntryService.create(vocabularyEntryRequestDto);
        return ResponseEntity.ok(vocabularyEntryDto);
    }

    @GetMapping("/list/{vocabularyId}")
    ResponseEntity<List<VocabularyEntryDto>> findAllByVocabularyId(@PathVariable Long vocabularyId) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<VocabularyEntryDto> vocabularyEntryDtoList = vocabularyEntryService.findAllByUserIdAndVocabularyId(userId, vocabularyId);
        return ResponseEntity.ok(vocabularyEntryDtoList);
    }

    @GetMapping("/{id}")
    ResponseEntity<VocabularyEntryDto> findById(@PathVariable Long id) {
        VocabularyEntryDto vocabularyEntryDto = vocabularyEntryService.findById(id);
        return ResponseEntity.ok(vocabularyEntryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable Long id) {
        vocabularyEntryService.deleteById(id);
    }

}
