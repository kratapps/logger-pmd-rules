#!/usr/bin/env bash

result="$(cat out/test_results.json | jq .result)"

exactly_one_violation() {
  local class_name="$1"
  local rule_name="$2"
  success=$(echo "${result}" | jq --arg class_name "$class_name" --arg rule_name "$rule_name" \
    'map(select(.fileName | endswith("classes/" + $class_name + ".cls")) 
    | select(.violations | length == 1 and .[0].ruleName == $rule_name)) | length == 1')
  if ! ${success} ; then
    echo "Error: There must be exactly one violation with ruleName '$rule_name' for classes/$class_name.cls." >&2
    exit 1
  fi
}

no_violation() {
  local class_name="$1"
  success=$(echo "${result}" | jq --arg class_name "$class_name" \
    'map(select(.fileName | endswith("classes/" + $class_name + ".cls")) | select(.violations)) | length == 0')
  if ! ${success} ; then
    echo "Error: There should be no violation for classes/$class_name.cls." >&2
    violations=$(echo "${result}" | jq --arg class_name "$class_name" \
        'map(select(.fileName | endswith("classes/" + $class_name + ".cls")) | .violations)' )
    echo "Violations: ${violations}" >&2
    exit 1
  fi
}

no_violation "ClassNoViolations"
exactly_one_violation "AuraEnabledMissingFinally" "AuraEnabledMethodShouldPublishLogsInFinallyBlock"
exactly_one_violation "AuraEnabledMissingPublishInFinally" "AuraEnabledMethodShouldPublishLogsInFinallyBlock"
exactly_one_violation "AuraEnabledMissingTryCatch" "AuraEnabledMethodShouldPublishLogsInFinallyBlock"
exactly_one_violation "LoggerInitClassDoesntMatchClassName" "LoggerInitClassMatchesClassName"
