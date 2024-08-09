docker_name=logger-pmd-rules-build

start-build-env:
	docker run --name "${docker_name}" -d -v "$(shell pwd)":/app -w /app maven:3.8.1-jdk-8 tail -f /dev/null

build:
	docker exec -it "${docker_name}" mvn package
	# scanner rule add in package.json postinstall script? 
	sf scanner rule add --language apex --path target/logger-pmd-rules-1.0.0.jar

preview-build:
	unzip -l target/logger-pmd-rules-1.0.0.jar
        
test:
	mkdir -p out
	sf scanner run --target src/test/force-app/ --engine pmd --pmdconfig src/main/resources/rulesets/apex/logger.xml --json > out/test_results.json
	bash scripts/test.sh
	
assert:
	bash scripts/test.sh

run-pure:
	sf scanner run --target ~/projects/pure/sfdc-legacy/src/classes/ --engine pmd  --pmdconfig src/main/resources/rulesets/apex/logger.xml

run-jar:
	"${JAVA_HOME}"/bin/java -jar target/logger-pmd-rules-1.0.0-jar-with-dependencies.jar \
        -R src/main/resources/category/apex/logger.xml \
        -d src/test/force-app/classes/ \
        -f json