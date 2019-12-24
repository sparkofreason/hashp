# hashtag

hashtag is derived from a fork of [weavejester/hashp][], and allows
definition of "tagged literal" functions to aid in debugging. If
"hashp (ab)uses data readers to make it easier to get debugging
data", hashtag beats them with a stick to achieve the same,
but with more flexibility, inspired by this [hashp issue][].

[weavejester/hashp]: https://github.com/weavejester/hashp
[hashp issue]: https://github.com/weavejester/hashp/issues/2

## Usage

**NOTE**: pre-alpha, not yet published to clojars. Please give it a try
by either cloning locally or using `deps.edn` and `:git/url`. Expect
breakage for now.

Use the `defhashtag` macro to define your own debugging hashtag. `
defhashtag` requires an (optionally namespaced) name for your hashtag,
and a single-argument handler function. The handler will be passed a
map with spec `:hashtag.core/debug-data`.

```clojure
(ns hashpp
  (:require [hashtag.core :as ht :refer [defhashtag]]
            [clojure.pprint :refer [pprint]]))

(defhashtag pp pprint)
```

This will define and register the reader tag `#pp`, which can then be used as

```clojure
(defn f
  [x]
  (let [a #pp (inc x)
        b #pp (* 2 a)]
    (dec b)))

(defn g
  [x]
  (* 3 #pp (f x)))

user=> (g 5)
{:result 6, :form (inc x)}
{:result 12, :form (* 2 a)}
{:result 11, :form (f x)}
33
```

Faster to type than `(pprint ...)`, and trivially removed with find/replace.
Note that you can use any single-argument function as a handler, so `pprint`
could be replaced by `tap>`, your own custom logic, etc.

`defhashtag` also accepts keyword options.

```clojure
(defhashtag pp/locals pprint :locals? true)
```

If we replace `#pp` with `#pp/locals` in the defintions of `f` and `g`, we
get the following output:

```clojure
user=> (g 5)
{:result 6, :form (inc x), :locals {:x 5}}
{:result 12, :form (* 2 a), :locals {:x 5, :a 6}}
{:result 11, :form (f x), :locals {:x 5}}
33
```

Setting the `:locals?` option to `true` adds a `:locals` attribute to the debug
map, containing a map of keywordized local binding names to their current
values (an idea borrowed from [athos/postmortem][]).

**NOTE**: Initial commits include support for manipulating the stacktrace.
This has been removed in favor of allowing implementers of hashtag handlers
to do whatever stacktrace manipulations they require, rather than being stuck
with the defaults that were defined here. See examples/hashp.clj for an
example.

[athos/postmortem]:https://github.com/athos/postmortem

## Examples

Run `clj -A:examples` to work with the code in the `examples` folder.
Examples include an implementation of the functionality of hashp using
hashtag.

## License

Copyright © 2019 Dave Dixon, James Reeves

Released under the MIT license.
