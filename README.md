# Filencrypt

A simple file encryption program written in Java.

## Usage

First You need to package the program into a jar file:

```
mvn clean package
```

Then You can execute the resulting jar file to encrypt files:

```
java -jar target/filencrypt-1.0.0.jar -e -i ./file.pdf -o file.pdf.enc -p 1234
```

Or to decrypt files:

```
java -jar target/filencrypt-1.0.0.jar -d -i ./file.pdf.enc -o file.pdf -p 1234
```
