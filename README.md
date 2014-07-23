# om-async-error

A Clojure library designed to ... well, that part is up to you.

## Usage

    lein cljsbuild once

Then open index.html in a browser and open the browsers console.

The problem can be seen as follows:

1. Toggle the form open. 
2. Press the dump button. It works.
3. Type into the form.
4. Press the dump button, the channel is broken.

Even though it can be seen that render is not called on base between
working and not working states. Initializing dump-chan inside the
state of base fixes this, but why does this happen?

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
