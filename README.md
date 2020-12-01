# ncl_icn-sfcsim
- icn-sfcsim is a simulator for service function chaining(SFC) using workflow scheduling on ICN. 
- This simulator supports several SF(Service Function) scheduling algorithms, and you can add a new algorithm in it. 
- Resultant execution information is written to a log file, and you can analyze the behaviors of the process. 

# How to build/run
- If you have already installed `ant` type `ant build` at the $PROJECT_HOME. 
## Master-Worker ICN Mode
- A CCNNode generates SFC and send the request to the master node. Then the master node schedules according to `master.properties`. 
- Then every SF (Service Function) is assigned to a vCPU ID and the interest packet for the END SF is sent to the node in which the vCPU is allocated to the END SF. 
- As for executing the simulator, type `./mastermain.sh` for Linux, or double click `mastermain.bat` for windows. 
## Autonomous ICN Mode
- As for executing the simulator, type `./mastermain.sh` for Linux, or double click `mastermain.bat` for windows. 
# About log format. 
- There are two types of log file, i.e., `is/islog.csv` and `is/YYYY-MM-DD-HH-mm-ss.csv`.
- is/islog.csv format: 
~~~
2020/12/02 01:46:52.592  ,Int., 0:Interest/1:CacheHit, AplID, SFCID, prefix, fromSFID@R_ID, toSFID, <-R/N+ID, Hop, Duration, @Host, @VM, @vCPU, TimeStamp
2020/12/02 01:46:52.592  ,Data., 0: ProcReturn/1: CacheReturn, AplID, SFCID, prefix, Proc.Time, fromSFID, R_ID->, toSFID@R/N+ID, Hop, ComTime, DataSize, @Host, @VM, @vCPU, BW, #ofSharedConnection, TimeStamp
2020/12/02 01:47:00.537  ,Int.,0,0,1,/1/2/5/,2@R68,5,<-R68,1,3699,1^5,1^5^0,1^5^0^2^0,1606841220537
2020/12/02 01:47:00.537  ,Data.,0,0,1,/1/1/4/,2912,1,R68->,4@R68,1,84,7330,1^5,1^5^0,1^5^0^2^1,-,-,1606841220537
~~~
 - That is, there are two types, i.e., Int (Interest sending) and Data (Data sending). 
- is/YYYY-MM-DD-HH-mm-ss.csv format: 
~~~
0:Each/1:Total,Site#, Host#, VM#, vCPU#, MaxMips, MinMips, MaxBW, MinBW,Apl#, SFC#, CCR, SF#, SFIns#, MakeSpan,Host#, VM#, vCPU#, CacheHit#
0,4,38,243,980,4000,2000,1000,600,0,1,2.8014,10,7,13878,3,1,3,2
0,4,38,243,980,4000,2000,1000,600,1,2,3.0076,10,9,20756,4,1,4,2
0,4,38,243,980,4000,2000,1000,600,3,4,3.317,10,9,17061,5,1,7,0
0,4,38,243,980,4000,2000,1000,600,2,3,2.6946,10,7,20530,2,1,2,1
~~~
