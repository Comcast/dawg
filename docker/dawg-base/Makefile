.PHONY: all build start
all: build start

QUIET ?= @

NAME=comcast/dawg-base
VERSION=latest
_NAME_VERSION=$(NAME):$(VERSION)

build: copy-dependencies
	@echo "Building $(_NAME_VERSION)"
	$(QUIET)docker build -t "$(_NAME_VERSION)" -p 8080

run:
	@echo "Running $(_NAME_VERSION)"
	$(QUIET)docker run -d $(_NAME_VERSION)

# $1 - directory/artifact name...eg: dawg-house
rpm-location-builder =  $(addsuffix *.rpm,../../libraries/$(1)/target/rpm/$(1)/RPMS/noarch/)

copy-dependencies:
	$(QUIET)mkdir -p target/rpms
	$(QUIET)cp $(call rpm-location-builder,dawg-config) target/rpms

clean:
	$(QUIET)rm -rf target

