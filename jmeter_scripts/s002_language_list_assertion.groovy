import groovy.json.JsonSlurper

final EXPECTED_STATUS = "200"
final EXPECTED_LANGUAGE_LIST_SIZE = 2
final EXPECTED_LANGUAGE_NAMES = Arrays.asList(
        "English",
        "German"
)

def failureMessage = ""
List<String> responseList = null

JsonSlurper JSON = new JsonSlurper()

try {
    responseList = JSON.parseText(prev.getResponseDataAsString())
} catch (Exception ignored) {
    failureMessage += "Invalid JSON.\n"
}

if (!EXPECTED_STATUS.equals(prev.getResponseCode())){
    failureMessage += "Expected " + EXPECTED_STATUS + " but got [" + prev.getResponseCode() + "]\n\n"
}

def actualListSize = responseList.size()
if (actualListSize != EXPECTED_LANGUAGE_LIST_SIZE) {
    failureMessage += String.format("Expected list of size %s, got %s\n\n", EXPECTED_LANGUAGE_LIST_SIZE, actualListSize)
}

// todo: optimize (create a map and reduce complexity to O(1)
def listOfMaps = (responseList as List<org.apache.groovy.json.internal.LazyMap>)
EXPECTED_LANGUAGE_NAMES.each {languageName -> {
    def anyMatch = listOfMaps.any {it.get("name") == languageName}
    if (!anyMatch) {
        failureMessage += "Expected to find [" + languageName + "] in the response collection\n\n"
    }
}}
if (failureMessage?.trim()) {
    failureMessage += "URL: " + SampleResult.getURL() + "\n\n"
    failureMessage += "JSON RESPONSE: " + responseList + "\n\n"
    failureMessage += "REQUEST HEADERS: " + SampleResult.getRequestHeaders() + "\n\n"

    AssertionResult.setFailureMessage(failureMessage)
    AssertionResult.setFailure(true)
}
