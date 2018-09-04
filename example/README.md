# How to use this plugin

## Launch

```
$ sudo /usr/bin/marathon \
       --http_port 8080 \
       --plugin_dir /usr/share/marathon/lib/plugins/athnz/ \
       --plugin_conf /path/to/plugin-conf.json
```


## Debug

For Debug with IDE
```
$ sudo JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005" /usr/bin/marathon \
       --logging_level debug \
       --http_port 8080 \
       --plugin_dir /usr/share/marathon/lib/plugins/athnz/ \
       --plugin_conf /path/to/plugin-conf.json
```

marathon log is output to `/var/log/messages`
```
$ sudo tail -F /var/log/messages |grep marathon
```

## Test this plugin

with curl
```
$ curl -X GET -s -i -L \
  -H 'Athenz-Role-Auth: <your athenz token>' \
  -H 'Content-type: application/json' \
  -dmypod.json \
  http://marathon.example.com:8080/v2/pods/
```
