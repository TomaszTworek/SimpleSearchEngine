## Simple search engine

### Initial information
Application is made as console CLI app that takes all documents from dedicated folder and index documents 
after launching the app. The application runs in an infinite loop. To break that loop you need to pass `:!q`.

### How to use it?
- Download the latest release.
- At the jar level, create a folder `documents` and put text files in it.
- Open CLI and pass the command `java -jar SimpleSearchEngine-1.0-SNAPSHOT.jar`.
- Pass term and get results with format [docName, TF-IDF value]
- To exit program pass `:!q`.

### Mistake in example:

A search for “fox” should return the list: ~~[document 1, document 3]~~ [document 3, document 1], 
because doc3 is shorter than doc1 and in both is only on word fox.

**Technology stack:**
- JDK17
- Maven
- Junit 5
