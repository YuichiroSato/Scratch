#!/bin/sh

npm install
mkdir node_modules/hello-world
cp pkg/* node_modules/hello-world
npm run start
