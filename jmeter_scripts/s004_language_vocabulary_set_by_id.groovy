import groovy.json.JsonSlurper

final EXPECTED_STATUS = "200"
final EXPECTED_LANGUAGE_VOCABULARY_ID = Integer.valueOf((String) vars.get("LANG_VOCAB_SET_ID_ONE"))
final EXPECTED_LANGUAGE_ID = Integer.valueOf((String) vars.get("LANG_ID"))
final EXPECTED_LANGUAGE_NAME = vars.get("LANG_NAME")
final EXPECTED_VOCABULARY_DTO_LIST_SIZE = 1
final DEFAULT_VOCABULARY_NAME = vars.get("DEFAULT_VOCABULARY_NAME")

def failureMessage = ""
org.apache.groovy.json.internal.LazyMap responseMap = null

JsonSlurper JSON = new JsonSlurper()

try {
    responseMap = JSON.parseText(prev.getResponseDataAsString())
} catch (Exception ignored) {
    failureMessage += "Invalid JSON.\n"
}

if (!EXPECTED_STATUS.equals(prev.getResponseCode())) {
    failureMessage += "Expected " + EXPECTED_STATUS + " but got [" + prev.getResponseCode() + "]\n\n"
}

def actualId = responseMap.get("id")
if (EXPECTED_LANGUAGE_VOCABULARY_ID != actualId) {
    failureMessage += String.format("Expected id %s, got %s\n\n", EXPECTED_LANGUAGE_VOCABULARY_ID, actualId)
}
if (responseMap.get("createdOn") == null) {
    failureMessage += "createdOn is null\n\n"
}

org.apache.groovy.json.internal.LazyMap languageDto = responseMap.get("languageDto")
def actualLanguageId = languageDto.get("id")
if (actualLanguageId != EXPECTED_LANGUAGE_ID) {
    failureMessage += String.format("Expected languageDto.id %s, got %s\n\n", EXPECTED_LANGUAGE_ID, actualLanguageId)
}
def actualLanguageName = languageDto.get("name")
if (actualLanguageName != EXPECTED_LANGUAGE_NAME) {
    failureMessage += String.format("Expected languageDto.name %s, got %s\n\n", EXPECTED_LANGUAGE_NAME, actualLanguageName)
}

List<org.apache.groovy.json.internal.LazyMap> actualVocabularyDtoList = responseMap.get("vocabularyDtoList")
def actualVocabularyDtoListSize = actualVocabularyDtoList.size()
if (actualVocabularyDtoListSize != EXPECTED_VOCABULARY_DTO_LIST_SIZE) {
    failureMessage += String.format("Expected vocabularyDtoList.size() %s, got %s\n\n", EXPECTED_VOCABULARY_DTO_LIST_SIZE, actualVocabularyDtoListSize)
}
org.apache.groovy.json.internal.LazyMap firstVocabularyDto = actualVocabularyDtoList.get(0)
def firstVocabularyDtoId = firstVocabularyDto.get("id")
if (firstVocabularyDtoId == null) {
    failureMessage += "firstVocabularyDto.id is null " + firstVocabularyDto + "\n\n"
} else {
    vars.put("DEFAULT_VOCABULARY_ID_ONE", "" + firstVocabularyDtoId)
}
def firstVocabularyDtoName = firstVocabularyDto.get("name")
if (!DEFAULT_VOCABULARY_NAME.equals(firstVocabularyDtoName)) {
    failureMessage += String.format("Expected firstVocabularyDto.name to be equal to %s, got %s\n\n", DEFAULT_VOCABULARY_NAME, firstVocabularyDtoName)
}

if (failureMessage?.trim()) {
    failureMessage += "URL: " + SampleResult.getURL() + "\n\n"
    failureMessage += "JSON RESPONSE: " + responseMap + "\n\n"
    failureMessage += "REQUEST HEADERS: " + SampleResult.getRequestHeaders() + "\n\n"

    AssertionResult.setFailureMessage(failureMessage)
    AssertionResult.setFailure(true)
}
