#!/bin/bash
# --------------------------------------------------------------
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
# --------------------------------------------------------------

# This script will be called from /etc/rc.local when the cartridge
# instance is spawned. It will initiate all the tasks that needs to 
# be run to bring the cartridge instance to operational state.

set -e # Terminate on any error
export LOG=/var/log/apache-stratos/cartridge-agent-sh.log
instance_path=/opt/apache-stratos-cartridge-agent # Cartridge agent home
ca_exec_path=${instance_path}/cartridge-agent # Cartridge agent executable home
cert_truststore=security/client-truststore.jks
truststore_password=wso2carbon

# ---------------------------------------------
# Download payload
# ---------------------------------------------
if [ ! -d ${instance_path}/payload ]; then
    echo "creating payload directory... " | tee -a $LOG
    mkdir ${instance_path}/payload
    echo "payload directory created" | tee -a $LOG
    wget http://169.254.169.254/latest/user-data -O ${instance_path}/payload/payload.txt
    echo "payload copied"  | tee -a $LOG

    for i in `/usr/bin/ruby ${instance_path}/get-launch-params.rb`
    do
        # Add double quotes on both sides of the value
        value=`echo "${i}" | sed -e s@=@=\"@g`
        value=${value}"\""
        if [[ ${value} == PORTS* ]]; then
            # Replace port separator | with ,
            value=`echo ${value} | sed -e s@'|'@,@g`
        fi
        echo "writing to launch.params ${value}" | tee -a $LOG
        echo "export" ${value} >> ${instance_path}/launch.params
    done    
fi

source ${instance_path}/launch.params

#---------------------------
# Starting load balancer
#---------------------------
pushd $instance_path/load-balancer/
sh "start-load-balancer.sh" $MB_IP $MB_PORT $CEP_IP $CEP_PORT $CLUSTER_ID $NETWORK_PARTITION_ID &
popd

#------------------------------------
# Starting cartridge agent executable
#------------------------------------
pushd $ca_exec_path
echo "Configuring cartridge agent executable..." | tee -a $LOG
cp -f templates/stratos.sh.template bin/stratos.sh.tmp
cat bin/stratos.sh.tmp | sed -e "s@MB-IP@$MB_IP@g" > bin/stratos.sh
cp -f bin/stratos.sh bin/stratos.sh.tmp
cat bin/stratos.sh.tmp | sed -e "s@MB-PORT@$MB_PORT@g" > bin/stratos.sh
cp -f bin/stratos.sh bin/stratos.sh.tmp
cat bin/stratos.sh.tmp | sed -e "s@CEP-IP@$CEP_IP@g" > bin/stratos.sh
cp -f bin/stratos.sh bin/stratos.sh.tmp
cat bin/stratos.sh.tmp | sed -e "s@CEP-PORT@$CEP_PORT@g" > bin/stratos.sh
cp -f bin/stratos.sh bin/stratos.sh.tmp
cat bin/stratos.sh.tmp | sed -e "s@CERT-TRUSTSTORE@$cert_truststore@g" > bin/stratos.sh
cp -f bin/stratos.sh bin/stratos.sh.tmp
cat bin/stratos.sh.tmp | sed -e "s@\bTRUSTSTORE-PASSWORD\b@$truststore_password@g" > bin/stratos.sh
rm -f bin/stratos.sh.tmp
echo "Starting cartridge agent..." | tee -a $LOG
sh bin/stratos.sh &
popd
