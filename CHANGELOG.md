# Changelog

### 1.2.0
- try to ping server first when creating connection; avoid hanging until jmx timeout on unresponsive
  servers

### 1.1.0
- avoid wrapping attribute / operation names, class names
- support executing overloaded operations
- actually useful error message if consul discovery does not find any nodes
- avoid Freemarker errors if operation / attribute description is null

### 1.0.0
- Initial Release!
