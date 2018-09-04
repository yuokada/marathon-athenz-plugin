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

## Tips

### Marathon command is simple shellscript

So bash `-x` is very useful.
```
$ rpm -qa marathon
marathon-1.4.8-1.0.660.el7.x86_64

$ sudo head -3 /usr/bin/marathon
#!/bin/bash
set -o errexit -o nounset -o pipefail
function -h {

$ sudo bash -xe /usr/bin/marathon --logging_level debug --http_port 8080 --plugin_dir /usr/share/marathon/lib/plugins/athenz/ --plugin_conf /etc/marathon/plugin-conf.json
```

### Chackpoint

- [ ] Do META-INF Files match your class file?
  
  ```
  src/main/resources/META-INF/services/mesosphere.marathon.plugin.auth.Authorizer
  src/main/resources/META-INF/services/mesosphere.marathon.plugin.auth.Authenticator
  
   $ cat src/main/resources/META-INF/services/mesosphere.marathon.plugin.auth.Authenticator
   jp.co.yahoo.qe.marathon.plugin.athenz.AthenzAuthenticator
  ```

