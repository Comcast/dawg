.PHONY: all build run stop copy-dependencies
all: build run copy-dependencies

QUIET ?= @

NAME=dawg-combo
PORT=8080
FULLNAME=comcast/$(NAME)
VERSION=latest
_NAME_VERSION=$(FULLNAME):$(VERSION)

build: copy-dependencies
	@echo "Building $(_NAME_VERSION)"
	$(QUIET)docker build -t "$(_NAME_VERSION)" .

run:
	@echo "Running $(_NAME_VERSION)"
	$(QUIET)docker run -d -p $(PORT):8080 --name=$(NAME) --link=dawg-db:dawg-db $(_NAME_VERSION)

# $1 - directory/artifact name...eg: dawg-house
rpm-location-builder =  $(addsuffix *.rpm,../../libraries/$(1)/target/rpm/$(1)/RPMS/noarch/)

copy-dependencies:
	$(QUIET)mkdir -p target/rpms
	$(QUIET)cp $(call rpm-location-builder,dawg-house) target/rpms
	$(QUIET)cp $(call rpm-location-builder,dawg-show) target/rpms
	$(QUIET)cp $(call rpm-location-builder,dawg-pound) target/rpms

clean:
	$(QUIET)rm -rf target

stop:
	$(QUIET)docker stop $(NAME)
	$(QUIET)docker rm $(NAME)
