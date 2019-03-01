# DropBox

This project is a DropBox prototype. It watches for changes (CREATE, MODIFY, DELETE) in a designated folder and reflect all the changes in Google Drive

## Build

1. git clone https://github.com/wilsenkosasih/DropBox.git
2. cd DropBox
3. mvn package


## Running the Tests

* Unit Test: mvn verify
* Integration-Test: mvn integration-test
* Cobertura: mvn cobertura:cobertura
* CheckStyle: mvn checkstyle:checkstyle
* FindBugs: mvn site

Reports will be found in  target/site
