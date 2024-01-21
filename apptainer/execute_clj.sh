#!/usr/bin/env sh
CLJ_CONFIG=/tmp
if [ -f "pyproject.toml" ]; then
    echo "Run clojure in python environment specified by pyproject.toml"
    poetry lock && poetry install --sync --no-root && poetry lock && poetry run clj -M:nrepl "$@"poetry lock && poetry install --sync --no-root && poetry lock && poetry run clj -Srepro  "$@"
else
    echo "pyproject.toml not found. Start Clojure without python venv."
    clj -Srepro "$@"
fi
