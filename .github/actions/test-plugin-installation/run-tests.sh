#!/usr/bin/env bash
#
# #%L
# AEM Rules for SonarQube
# %%
# Copyright (C) 2015-2024 VML
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
#


set -euo pipefail

cd "$(dirname "$0")"

port=9000

print_usage() {
    cat << EOF
usage: $0 [IMAGE...]

examples:
       $0 9.9-community
EOF
}

info() {
    echo "[info] $@"
}

warn() {
    echo "[warn] $@" >&2
}

fatal() {
    echo "[error] $@" >&2
    exit 1
}

require() {
    local prog missing=()
    for prog; do
        if ! type "$prog" &>/dev/null; then
            missing+=("$prog")
        fi
    done

    [[ ${#missing[@]} = 0 ]] || fatal "could not find reqired programs on the path: ${missing[@]}"
}

wait_for_sonarqube() {
    local image=$1 i web_up=no sonarqube_up=no aemrules_up=no

    for ((i = 1; i < 11; i++)); do
        info "$image: waiting for web server to start ... $i/10"
        if curl -sI localhost:$port | grep '^HTTP/.* 200'; then
            web_up=yes
            break
        fi
        sleep 5
    done

    [[ $web_up = yes ]] || return 1

    for ((i = 1; i < 21; i++)); do
        info "$image: waiting for sonarqube to be ready ... $i/20"
        if curl -s localhost:$port/api/system/status | grep '"status":"UP"'; then
            sonarqube_up=yes
            break
        fi
        sleep 10
    done

    [[ "$sonarqube_up" = yes ]] || return 1

    for ((i = 1; i < 11; i++)); do
        info "$image: waiting for AEM Rules to be ready ... $i/10"
        curl -s localhost:$port/api/plugins/installed | tee curloutput.log
        if curl -u admin:admin -s localhost:$port/api/plugins/installed | grep -o '"AEM Rules for SonarQube"'; then
            aemrules_up=yes
            break
        fi
        sleep 5
    done

    [[ "$aemrules_up" = yes ]]
}

sanity_check_image() {
    local image=$1 id result

    id=$(docker run -d -p $port:9000 "$image")
    info "$image: container started: $id"

    if wait_for_sonarqube "$image"; then
        info "$image: OK !"
        result=ok
    else
        warn "$image: could not confirm service started"
        result=failure
    fi

    info "$image: stopping container: $id"
    docker container stop "$id"

    [[ $result == ok ]]
    
}

require curl docker

for arg; do
    if [[ $arg == "-h" ]] || [[ $arg == "--help" ]]; then
        print_usage
        exit
    fi
done

if [[ $# = 0 ]]; then
    warn "at least one image as parameter is required"
    exit
fi

image=($1)
results=()

if sanity_check_image "$image" ; then
    results+=("success")
else
    results+=("failure")
fi

failures=0
echo "${image} => ${results}"
if [[ ${results} != success ]]; then
    ((failures++))
fi

[[ $failures = 0 ]]