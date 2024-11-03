## Nasa app telegram bot

Install commands listings:  
```sh
qwuen@vm-c32d04:~$ ls
NasaApp-1.0.jar
qwuen@vm-c32d04:~$ sudo groupadd -r appmgr
qwuen@vm-c32d04:~$ sudo useradd -r -s /bin/false -g appmgr jvmapps
qwuen@vm-c32d04:~$ id jvmapps
uid=998(jvmapps) gid=999(appmgr) groups=999(appmgr)
qwuen@vm-c32d04:~$ sudo nano /etc/systemd/system/nasaapp.service
qwuen@vm-c32d04:~$ sudo chown -R jvmapps:appmgr /opt/prod
qwuen@vm-c32d04:~$ sudo mv NasaApp.jar /opt/prod/
qwuen@vm-c32d04:~$ ls /opt/prod/
NasaApp-1.0.jar
qwuen@vm-c32d04:~$ sudo systemctl daemon-reload
qwuen@vm-c32d04:~$ sudo systemctl start nasaapp.service


```