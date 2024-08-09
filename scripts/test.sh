#!/usr/bin/env bash

result="$(cat out/test_results.json | jq .result)"

#echo "${result}"

success=$(echo "${result}" | jq 'map(select(.fileName | endswith("classes/LoggerClassDoesntMatchClassName.cls")) | select(.violations | length == 1 and .[0].ruleName == "LoggerClassMatchesClassNames")) | length == 1')
if ! ${success} ; then
  echo "Error: There must be exactly one violation with ruleName 'LoggerClassMatchesClassNames' for classes/LoggerClassDoesntMatchClassName.cls." >&2
  exit 1
fi

success=$(echo "${result}" | jq 'map(select(.fileName | endswith("classes/ClassNoViolations.cls"))) | length == 0')
if ! ${success} ; then
  echo "Error: There should be no violation for classes/ClassNoViolations.cls." >&2
  exit 1
fi

