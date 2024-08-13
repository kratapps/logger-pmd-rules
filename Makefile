docker_name=logger-pmd-rules-build

start-build-env:
	docker run --name "${docker_name}" -d -v "$(shell pwd)":/app -w /app maven:3.8.1-jdk-8 tail -f /dev/null

build:
	docker exec -it "${docker_name}" mvn package
	mkdir -p lib
	cp target/logger-pmd-rules-1.0.0.jar lib/ruleset.jar
	cp -r src/main/resources/* lib

preview-build:
	unzip -l target/logger-pmd-rules-1.0.0.jar
        
test:
	mkdir -p out
	sf scanner rule add --language apex --path lib/ruleset.jar
	sf scanner run --target src/test/force-app/ --engine pmd --pmdconfig lib/rulesets/apex/logger.xml --json > out/test_results.json
	sf scanner rule remove --force --path lib/ruleset.jar
	make assert
	
assert:
	bash scripts/test.sh
