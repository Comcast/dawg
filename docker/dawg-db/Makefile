.PHONY: all build run stop
all: build run

QUIET=@

NAME=dawg-db
FULLNAME=comcast/dawg-db
VERSION=latest
_NAME_VERSION=$(FULLNAME):$(VERSION)

build:
	@echo "Building $(_NAME_VERSION)"
	$(QUIET)docker build -t "$(_NAME_VERSION)" .

run:
	@echo "Running $(_NAME_VERSION)"
	$(QUIET)docker run -d --name=$(NAME) $(_NAME_VERSION)

stop:
	$(QUIET)docker stop $(NAME)
	$(QUIET)docker rm $(NAME)
