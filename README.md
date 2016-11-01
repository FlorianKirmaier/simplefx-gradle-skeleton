# visual-neural-net-javafx

Client for the [visual-neural-net-server](https://github.com/JavaFXpert/visual-neural-net-server) 
from [James Weaver](https://github.com/JavaFXpert).

It uses:
 * javafx as a java-gui-framework.
 * simplefx to write efficently javafx-programs
 * [jpro](http://jpro.io) to run in the browser.

### run desktop
```shell
gradle run
```

### run browser

To run in the Web, it requires a jpro-license, which must be put into `src/main/resources/jpro.conf`.

You can register [here](http://jpro.io/?page=signup) for the closed beta!
```shell
gradle runBrowser
```
