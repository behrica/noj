![ci workflow](https://github.com/scicloj/noj/actions/workflows/ci.yml/badge.svg)


# Scinojure
![Noj logo](notebooks/Noj.svg)

Scinojure ("Noj") wraps a few of the relevant Clojure libraries for data & science and documents common ways of using them together.

deps:
```clj
org.scicloj/noj {:git/url "https://github.com/scicloj/noj.git"
                 :git/tag "2-alpha6"
                 :git/sha "c7a7240"}

```

## Status
Most of the [underlying libraries](https://scicloj.github.io/noj/noj_book.underlying_libraries.html) are stable. The experimental parts are marked as such. For some of the libraries, we use a branch for an upcoming release.
The main current goal is to provide a clear picture of the direction the stack is going towards, expecting most of it to stabilize around October 2024.

## Usage
[Docs](https://scicloj.github.io/noj/) (WIP)

## Where to discuss
- library development:
  - the [#noj-dev](https://clojurians.zulipchat.com/#narrow/stream/321125-noj-dev) stream at the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/)
  - [Issues](https://github.com/scicloj/noj) at this repo
- user questions:
  - the [#data-science stream](https://clojurians.zulipchat.com/#narrow/stream/151924-data-science) at the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/)

## License

Copyright © 2022 Scicloj

_EPLv1.0 is just the default for projects generated by `clj-new`: you are not_
_required to open source this project, nor are you required to use EPLv1.0!_
_Feel free to remove or change the `LICENSE` file and remove or update this_
_section of the `README.md` file!_

Distributed under the Eclipse Public License version 1.0.

