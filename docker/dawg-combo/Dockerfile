FROM comcast/dawg-base
MAINTAINER trent_schmidt@cable.comcast.com

RUN mkdir /dawg-combo
WORKDIR /dawg-combo

ADD target/rpms rpms
RUN rpm -Uvh rpms/*
ENV DAWG_DB=dawg-db \
    DAWG_HOUSE=http://localhost:8080 \
    DAWG_POUND=http://localhost:8080 \
    DAWG_SHOW=http://localhost:8080
RUN sed -i "s|dawg-db.test.domain|${DAWG_DB}|g" /etc/dawg/dawg.ini && \
    sed -i "s|http://localhost:8080/dawg-house|${DAWG_HOUSE}/dawg-house|g" /etc/dawg/dawg.ini && \
    sed -i "s|http://localhost:8080/dawg-show|${DAWG_SHOW}/dawg-show|g" /etc/dawg/dawg.ini && \
    sed -i "s|http://localhost:8080/dawg-pound|${DAWG_POUND}/dawg-pound|g" /etc/dawg/dawg.ini
