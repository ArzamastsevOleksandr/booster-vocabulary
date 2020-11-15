import groovy.json.JsonSlurper

final EXPECTED_STATUS = "200"
final EXPECTED_RESPONSE_MAP_SIZE = 1
final EXPECTED_MESSAGE = "User registered successfully!"

def failureMessage = ""
org.apache.groovy.json.internal.LazyMap response = null

JsonSlurper JSON = new JsonSlurper()

try {
    response = JSON.parseText(prev.getResponseDataAsString())
} catch (Exception ignored) {
    failureMessage += "Invalid JSON.\n"
}

if (!EXPECTED_STATUS.equals(prev.getResponseCode())) {
    failureMessage += "Expected " + EXPECTED_STATUS + " but got [" + prev.getResponseCode() + "]\n\n"
}

def actualListSize = response.size()
if (actualListSize != EXPECTED_RESPONSE_MAP_SIZE) {
    failureMessage += String.format("Expected list of size %s, got %s\n\n", EXPECTED_RESPONSE_MAP_SIZE, actualListSize)
}

if (!response.get("message").equals(EXPECTED_MESSAGE)) {
    failureMessage += String.format("Expected response to contain %s", EXPECTED_MESSAGE)
}

if (failureMessage?.trim()) {
    failureMessage += "URL: " + SampleResult.getURL() + "\n\n"
    failureMessage += "JSON RESPONSE: " + response + "\n\n"
    failureMessage += "REQUEST HEADERS: " + SampleResult.getRequestHeaders() + "\n\n"

    AssertionResult.setFailureMessage(failureMessage)
    AssertionResult.setFailure(true)
}
