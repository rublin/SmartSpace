#! /bin/bash
# validate parameters
if [ -z "$2" ]
        then
                echo "usage: install.sh <telegram bot name> <telegram bot token>"
        exit 1
fi
# create variables
# postgresPass=$1
telegramBot=$1
telegramToken=$2

# update server
#apt-get --assume-yes update
# install dependencies
#apt-get --assume-yes install openjdk-8-jre postgresql-9.5 vlc-nox
mkdir -p /opt/smartspace/config
