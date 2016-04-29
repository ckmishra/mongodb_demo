#!/usr/bin/env bash

mkdir -p /data/demo1 /data/demo2 /data/demo3
mongod --replSet rs1 --logpath "1.log" --dbpath /data/demo1 --port 27017 --oplogSize 64 --fork --smallfiles
mongod --replSet rs1 --logpath "2.log" --dbpath /data/demo2 --port 27018 --oplogSize 64 --smallfiles --fork
mongod --replSet rs1 --logpath "3.log" --dbpath /data/demo3 --port 27019 --oplogSize 64 --smallfiles --fork



