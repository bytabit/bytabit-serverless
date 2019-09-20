#!/bin/bash

## mount EBS

while ! ls ${device_name} > /dev/null
do
    sleep 5
done

if [ `sudo file -s ${device_name} | cut -d ' ' -f 2` = 'data' ]
then
    sudo mkfs -t ext4 ${device_name}
fi

sudo mkdir -p ${mount_dir}
sudo mount -t ext4 ${device_name} ${mount_dir}
sudo chmod o-r ${mount_dir}

sudo sh -c 'echo "${device_name} ${mount_dir} ext4 defaults 0 2" >> /etc/fstab'

## install docker

sudo apt-get update

sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common \
    unzip

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"

sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose

## install dojo

cd /home/ubuntu
wget -q https://github.com/Samourai-Wallet/samourai-dojo/archive/master.zip
unzip master.zip
