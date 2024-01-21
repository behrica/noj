#!/usr/bin/env bash
export CLJ_CONFIG=/tmp
if [ -f "pyproject.toml" ]; then
    echo "Run clojure in python environment specified by pyproject.toml"
    poetry install --sync --no-root && poetry lock && poetry run clj -Srepro -Sdeps '{:deps {org.scicloj/noj {:mvn/version "1-alpha25"}}}' "$@"
else
    echo "pyproject.toml not found. Start Clojure without python venv."
    clj -Srepro -Sdeps '{:deps {org.scicloj/noj {:mvn/version "1-alpha25"}}}' "$@"
fi
