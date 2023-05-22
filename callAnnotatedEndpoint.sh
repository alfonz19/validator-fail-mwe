#!/bin/bash

curl -X POST localhost:8080/vfa/test/annotated -H "Content-Type:application/json" --data-binary @<(echo "{\"uuid\": \"$(uuidgen)\"}")
