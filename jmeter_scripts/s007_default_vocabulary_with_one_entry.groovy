import groovy.json.JsonSlurper

final EXPECTED_STATUS = "200"
final EXPECTED_ENTRY_COUNT = 1
final EXPECTED_TARGET_WORD = vars.get("VOCABULARY_ENTRY_ONE")

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

def actualEntryCount = responseMap.get("entryCount")
if (actualEntryCount != EXPECTED_ENTRY_COUNT) {
    failureMessage += String.format("Expected entryCount %s, got %s\n\n", EXPECTED_ENTRY_COUNT, actualEntryCount)
}

List<org.apache.groovy.json.internal.LazyMap> actualVocabularyEntries = responseMap.get("vocabularyEntries")
if (actualVocabularyEntries.size() != EXPECTED_ENTRY_COUNT) {
    failureMessage += String.format("Expected vocabularyEntries to have size %s, got %s\n\n", EXPECTED_ENTRY_COUNT, actualVocabularyEntries.size())
}
def containsTargetWord = actualVocabularyEntries.any { it.containsValue(EXPECTED_TARGET_WORD) }
if (!containsTargetWord) {
    failureMessage += String.format("Expected vocabularyEntries to contain targetWord %s\n\n", EXPECTED_TARGET_WORD)
}

if (failureMessage?.trim()) {
    failureMessage += "URL: " + SampleResult.getURL() + "\n\n"
    failureMessage += "JSON RESPONSE: " + responseMap + "\n\n"
    failureMessage += "REQUEST HEADERS: " + SampleResult.getRequestHeaders() + "\n\n"

    AssertionResult.setFailureMessage(failureMessage)
    AssertionResult.setFailure(true)
}
