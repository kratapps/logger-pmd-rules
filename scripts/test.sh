#!/usr/bin/env bash

result="$(cat out/test_results.json | jq .result)"

success=$(echo "${result}" | jq 'map(select(.fileName | endswith("classes/AuraEnabledMissingFinally.cls")) | select(.violations | length == 1 and .[0].ruleName == "AuraEnabledMethodShouldPublishLogsInFinallyBlock")) | length == 1')
if ! ${success} ; then
  echo "Error: There must be exactly one violation with ruleName 'AuraEnabledMethodShouldPublishLogsInFinallyBlock' for classes/AuraEnabledMissingFinally.cls." >&2
  exit 1
fi

success=$(echo "${result}" | jq 'map(select(.fileName | endswith("classes/AuraEnabledMissingPublishInFinally.cls")) | select(.violations | length == 1 and .[0].ruleName == "AuraEnabledMethodShouldPublishLogsInFinallyBlock")) | length == 1')
if ! ${success} ; then
  echo "Error: There must be exactly one violation with ruleName 'AuraEnabledMethodShouldPublishLogsInFinallyBlock' for classes/AuraEnabledMissingPublishInFinally.cls." >&2
  exit 1
fi

success=$(echo "${result}" | jq 'map(select(.fileName | endswith("classes/AuraEnabledMissingTryCatch.cls")) | select(.violations | length == 1 and .[0].ruleName == "AuraEnabledMethodShouldPublishLogsInFinallyBlock")) | length == 1')
if ! ${success} ; then
  echo "Error: There must be exactly one violation with ruleName 'AuraEnabledMethodShouldPublishLogsInFinallyBlock' for classes/AuraEnabledMissingTryCatch.cls." >&2
  exit 1
fi

success=$(echo "${result}" | jq 'map(select(.fileName | endswith("classes/LoggerInitClassDoesntMatchClassName.cls")) | select(.violations | length == 1 and .[0].ruleName == "LoggerInitClassMatchesClassName")) | length == 1')
if ! ${success} ; then
  echo "Error: There must be exactly one violation with ruleName 'LoggerInitClassMatchesClassName' for classes/LoggerInitClassDoesntMatchClassName.cls." >&2
  exit 1
fi

success=$(echo "${result}" | jq 'map(select(.fileName | endswith("classes/ClassNoViolations.cls"))) | length == 0')
if ! ${success} ; then
  echo "Error: There should be no violation for classes/ClassNoViolations.cls." >&2
  exit 1
fi