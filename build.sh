#!/bin/bash

cd `dirname $0`
mvn clean install dependency:copy-dependencies
rm -rf deploy
mkdir -p deploy
cp script/*.sh deploy/
cp -r target/lib/ deploy/lib
cp target/*.jar deploy/

cd deploy/
tar zcvf ste.tar.gz *

echo deploy/ste.tar.gz 

