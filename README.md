# om-async-error

A Clojure library designed to ... well, that part is up to you.

## Usage

    lein cljsbuild once

Then open index.html in a browser and open the browsers console.

The problem can be seen as follows:

1. Toggle the form open. 
2. Type something into the text box.
3. Press the dump state button several times.
4. The console should show the correct state.
5. Toggle the form closed, then toggle it open again.
6. Type something different into the text box.
7. Hit the dump state button several times.
8. The console randomly flips between showing the correct state and the previous state.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
