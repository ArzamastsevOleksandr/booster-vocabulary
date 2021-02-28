package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.request.VocabularyEntryRequestDto;
import com.booster.vocabulary.service.VocabularyEntryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    // todo: 201 status?
    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<VocabularyEntryDto> create(@RequestBody VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        String userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        vocabularyEntryRequestDto.setUserId(userId);

        VocabularyEntryDto vocabularyEntryDto = vocabularyEntryService.create(vocabularyEntryRequestDto);
        return ResponseEntity.ok(vocabularyEntryDto);
    }

    @GetMapping("/list/{vocabularyId}")
    ResponseEntity<VocabularyEntryDtoList> findAllByVocabularyId(@PathVariable String vocabularyId) {
        String userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<VocabularyEntryDto> vocabularyEntryDtoList = vocabularyEntryService.findAllByUserIdAndVocabularyId(userId, vocabularyId);
        return ResponseEntity.ok(new VocabularyEntryDtoList(vocabularyEntryDtoList));
    }

    // todo: security: an authenticated user can obtain an id and get someone's vocabularyEntry
    @GetMapping("/{id}")
    ResponseEntity<VocabularyEntryDto> findById(@PathVariable String id) {
        VocabularyEntryDto vocabularyEntryDto = vocabularyEntryService.findById(id);
        return ResponseEntity.ok(vocabularyEntryDto);
    }

    // todo: security: an authenticated user can obtain an id and delete someone's vocabularyEntry
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable String id) {
        vocabularyEntryService.deleteById(id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VocabularyEntryDtoList {
        List<VocabularyEntryDto> vocabularyEntryDtoList;
    }

}
