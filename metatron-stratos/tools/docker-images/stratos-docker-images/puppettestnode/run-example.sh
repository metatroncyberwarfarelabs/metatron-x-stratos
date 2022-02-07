#!/bin/bash
# ----------------------------------------------------------------------------
#
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing,
#  software distributed under the License is distributed on an
#  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#  KIND, either express or implied.  See the License for the
#  specific language governing permissions and limitations
#  under the License.
#
# ----------------------------------------------------------------------------

set -e 

cat > addpuppettestnode.txt <<EOF
server 127.0.0.1
zone example.com
prereq nxdomain testnode.$DOMAIN.
update add testnodde.$DOMAIN. 10  A $IP_ADDR
send
EOF
nsupdate addpuppettestnode.txt
rm -f addpuppettestnode.txt

docker run -d=false -i -t --dns=$BIND_IP_ADDR -h testnode.$DOMAIN -e "PUPPET_HOST=puppet.$DOMAIN" -e "PUPPET_PORT=$PUPPET_PORT" apachestratos/puppettestnode

