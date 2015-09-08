CRS Fish
========

Simple demo of AWS SWF, including human-interaction activities.

## Usage

(Assumes AWS credentials are in place)

1. `mvn clean install`
2. `mvn cargo:run`
3. Open [`http://localhost:8080`][2] in a web browser. You should see a fish swimming back and forth.
4. Submit a color change request: `curl -X POST -d "color=red" http://localhost:8080/crs/submit`.
5. Grab the task token from `/var/log/tomcat7/catalina.out`.
6. Approve or reject the request: `curl -X POST --data-urlencode "id=[TASK TOKEN]" http://localhost:8080/crs/[approve|reject]`.
7. If you approved it, watch the fish change colors!

## Fish Images

The fish images were generated with the following [Racket][1] program:

```racket
#lang slideshow

(map (λ (color)
       (send (pict->bitmap (standard-fish 140 70 #:direction 'right #:color color))
             save-file (string-append "/Users/drautb/GitHub/drautb/crs-fish/src/main/webapp/" color "-fish.png") 'png))
     (list "red" "orange" "yellow" "green" "blue" "indigo" "violet" "white" "black" "gray"))
```


[1]: http://racket-lang.org
[2]: http://localhost:8080

