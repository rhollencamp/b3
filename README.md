# Percolator

Percolator is a web application that exposes JMX beans over a web frontend.


### Configuring Percolator

A configuration file is required to list JMX endpoints for Percolator to
connect to. The configuration file needs to list `Applications` inside of
`clusters`:
 * an `Application` is a Java process to connect to via JMX
   * `name` unique name within a cluster
   * `host` hostname / IP address to connect to
   * `port` port on `host` to connect to
   * `username` (optional) username for JMX authentication
   * `password` (optional) password for JMX authentication
 * a `Cluster` is a collection of `Application` that are expected to have a
   the same beans exposed over JMX; operations against a bean can optionally
   be applied against every `Application` inside of a given `Cluster`

A sample config file is as follows:

```
percolator:
  clusters:
    - name: test
      apps:
        - name: test
          host: 127.0.0.1
          port: 8081
```

Config files should be named `application-foo.yml`; on application startup you
can pass in the config file to use with `-Dspring.profiles.active=foo`.
