import groovy.json.JsonSlurper

final EXPECTED_STATUS = "200"
final EXPECTED_DEFAULT_VOCABULARY_ID = Integer.valueOf((String) vars.get("DEFAULT_VOCABULARY_ID_ONE"))
final EXPECTED_ENTRY_COUNT = 0

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
if (EXPECTED_DEFAULT_VOCABULARY_ID != actualId) {
    failureMessage += String.format("Expected id %s, got %s\n\n", EXPECTED_DEFAULT_VOCABULARY_ID, actualId)
}

if (responseMap.get("createdOn") == null) {
    failureMessage += "createdOn is null\n\n"
}

def actualEntryCount = responseMap.get("entryCount")
if (actualEntryCount != EXPECTED_ENTRY_COUNT) {
    failureMessage += String.format("Expected entryCount %s, got %s\n\n", EXPECTED_ENTRY_COUNT, actualEntryCount)
}

List<org.apache.groovy.json.internal.LazyMap> actualVocabularyEntries = responseMap.get("vocabularyEntries")
if (!actualVocabularyEntries.isEmpty()) {
    failureMessage += String.format("Expected vocabularyEntries to have size %s, got %s\n\n", 0, actualVocabularyEntries.size())
}

if (failureMessage?.trim()) {
    failureMessage += "URL: " + SampleResult.getURL() + "\n\n"
    failureMessage += "JSON RESPONSE: " + responseMap + "\n\n"
    failureMessage += "REQUEST HEADERS: " + SampleResult.getRequestHeaders() + "\n\n"

    AssertionResult.setFailureMessage(failureMessage)
    AssertionResult.setFailure(true)
}
