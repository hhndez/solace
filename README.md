# solace

Ejemplos para probar el appliance 

*TOPIC
PRODUCER
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=100 -ptl=topic
CONSUMER
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -stl=topic -md

*QUEUE
PRODUCER
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=100 -pql=testqueue
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=10000 -pql=testqueue
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=100000000 -mr=10000 -pql=testqueue

CONSUMER 1
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=testqueue -md
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=testqueue
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=testqueue -md
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=testqueue

CONSUMER 2
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=testqueue -md
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=testqueue




*fanout
PRODUCER
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=10000000 -mr=5000 -ptl=fanout/topic -mt=persistent
--------------------------------------------------------------------------------------------------------------
CONSUMER 1
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=fanout2
CONSUMER 2
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=fanout1


*LOAD BALANCE
PRODUCER
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=10000000 -mr=1 -pql=nonexclusive -mt=persistent
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=10000000 -mr=10 -pql=nonexclusive -mt=persistent
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=10000000 -mr=10 -pql=nonexclusive -mt=persistent
./sdkperf_c -cip=10.100.236.146 -cu=test@poc_vpn -mn=10000000 -mr=100 -pql=nonexclusive -mt=persistent

CONSUMER 1
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=nonexclusive -md
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=nonexclusive

CONSUMER 2
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=nonexclusive -md -sd=1000 -asw=1
./sdkperf_c -cip=10.100.236.146 -cu=sub@poc_vpn -sql=nonexclusive -md -sd=100 -asw=1

