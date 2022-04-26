## Server

```bash
java -classpath target/classes -Djava.rmi.server.hostname=202.117.15.41 -Djava.rmi.server.codebase=file:target/classes/ main.server.TranslatorImpl &
```

## Client

```bash
java -classpath target/classes main.client.Client
```

