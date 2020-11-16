import groovy.json.JsonSlurper

final EXPECTED_STATUS = "200"
final EXPECTED_TARGET_WORD = vars.get("VOCABULARY_ENTRY_ONE")
final EXPECTED_VOCABULARY_ENTRY_ID = Integer.valueOf((String) vars.get("VOCABULARY_ENTRY_ID_ONE"))
final EXPECTED_CORRECT_ANSWERS_COUNT = 0
final EXPECTED_ANTONYMS_SIZE = 1
final EXPECTED_SYNONYMS_SIZE = 2
final EXPECTED_ANTONYMS = Arrays.asList(
        vars.get("VOCABULARY_ENTRY_ONE_ANTONYM_ONE")
)
final EXPECTED_SYNONYMS = Arrays.asList(
        vars.get("VOCABULARY_ENTRY_ONE_SYNONYM_ONE"),
        vars.get("VOCABULARY_ENTRY_ONE_SYNONYM_TWO")
)

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
if (EXPECTED_VOCABULARY_ENTRY_ID != actualId) {
    failureMessage += String.format("Expected id %s, got %s\n\n", EXPECTED_VOCABULARY_ENTRY_ID, actualId)
}

def actualTargetWord = responseMap.get("targetWord")
if (EXPECTED_TARGET_WORD != actualTargetWord) {
    failureMessage += String.format("Expected targetWord %s, got %s\n\n", EXPECTED_TARGET_WORD, actualTargetWord)
}

if (responseMap.get("createdOn") == null) {
    failureMessage += "createdOn is null\n\n"
}

def actualCorrectAnswersCount = responseMap.get("correctAnswersCount")
if (actualCorrectAnswersCount != EXPECTED_CORRECT_ANSWERS_COUNT) {
    failureMessage += String.format("Expected entryCount %s, got %s\n\n", EXPECTED_CORRECT_ANSWERS_COUNT, actualCorrectAnswersCount)
}

List<String> actualAntonyms = responseMap.get("antonyms")
if (actualAntonyms.size() != EXPECTED_ANTONYMS_SIZE) {
    failureMessage += String.format("Expected [antonyms] to have size %s, got %s\n\n", EXPECTED_ANTONYMS_SIZE, actualAntonyms.size())
}
EXPECTED_ANTONYMS.each {
    antonym ->
        {
            if (!actualAntonyms.contains(antonym)) {
                failureMessage += String.format("Expected [antonyms] to have antonym %s\n\n", antonym)
            }
        }
}

List<org.apache.groovy.json.internal.LazyMap> actualSynonyms = responseMap.get("synonyms")
if (actualSynonyms.size() != EXPECTED_SYNONYMS_SIZE) {
    failureMessage += String.format("Expected [synonyms] to have size %s, got %s\n\n", EXPECTED_ANTONYMS_SIZE, actualSynonyms.size())
}

EXPECTED_SYNONYMS.each {
    synonym ->
        {
            if (!actualSynonyms.contains(synonym)) {
                failureMessage += String.format("Expected [synonyms] to have synonym %s\n\n", synonym)
            }
        }
}

if (failureMessage?.trim()) {
    failureMessage += "URL: " + SampleResult.getURL() + "\n\n"
    failureMessage += "JSON RESPONSE: " + responseMap + "\n\n"
    failureMessage += "REQUEST HEADERS: " + SampleResult.getRequestHeaders() + "\n\n"

    AssertionResult.setFailureMessage(failureMessage)
    AssertionResult.setFailure(true)
}
