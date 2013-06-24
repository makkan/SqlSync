#!/bin/bash

ENV_HOME=${HOME}
export HOME="/home/sqlsync"
sudo -u sqlsync "/home/sqlsync/sqlsync.sh" $1


export HOME=${ENV_HOME}
