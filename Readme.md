# Reverse Template Engine

## Quick start

Java library that permits to extract data to JSON from a plain text file which can be templated.
There are two configuration files, one specific to the file we want to extract the data which is the template and one specifying the types of the data we want to extract.
In addition, there is a parameter to ignore the empty lines in both template parsing and file parsing.
### Configuration file #1: Types

Sample of the file:
```
STRING=[A-Za-z0-9]+
DATE=[0-9]{8}[T| ][0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{4}
INTEGER=[0-9]+
DOUBLE=[0-9]+.[0-9]+
```
Each type is defined by a regular expression.

### Configuration file #2 : Template

#### Usecase #1: No iterations

Sample of the template:
```
*** ID: {{STRING:id}} ***
- type: {{STRING:type}}
- time-stamp: {{DATE:timestamp}}

* Device: XKK-{{INTEGER:deviceid}}

```
Corresponding file to fetch data from:
```
*** ID: X821 ***
- type: B5
- time-stamp: 20160202T01:11:01.2991


* Device: XKK-255141

```

The programm will capture the data at the placeholder position with the corresponding regex given by the Type and name it respectively.

JSON output:
```
{
 "id": "X821",
 "type": "B5",
 "timestamp": "20160202T01:11:01.2991",
 "deviceId": 255141
}
```

#### Usecase #2: Iterations
Sample of a Template with iterations

```
##########################

** Base Information **
ID: {{STRING:id}} ***
Type: {{STRING:type}}

** Measurements **
{{*:measurements}}{{STRING:measurement}}-{{INTEGER:value}}
``` 

Corresponding file: 
```
##########################

** Base Information **
ID: K76261X ***
Type: MX7

** Measurements **
pressure1-17
pressure2-18
pressure3-19

```

JSON output:

```
{
 "id": "K76261X",
 "type": "MX7",
 "measurements": [
   {
     "pressure1": 17
   },
   {
     "pressure2": 18
   },
   {
     "pressure3": 19
   }
 ]
}
```


