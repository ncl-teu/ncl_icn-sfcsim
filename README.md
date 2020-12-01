# ncl_icn-sfcsim
- icn-sfcsim is a simulator for service function chaining(SFC) using workflow scheduling on ICN. 
- This simulator supports several SF(Service Function) scheduling algorithms, and you can add a new algorithm in it. 
- Resultant execution information is written to a log file, and you can analyze the behaviors of the process. 

## Master-Worker ICN Mode
# How to build/Run
- If you have already installed `ant` type `ant build` at the $PROJECT_HOME. 
- As for executing the simulator, type `./mastermain.sh` for Linux, or double click `mastermain.bat` for windows. 
# About log format. 
- There are two types of log file, i.e., `is/islog.csv` and `is/YYYY-MM-DD-HH-mm-ss.csv`.
- is/islog.csv format: 
~~~
2020/12/02 01:46:52.592  ,Int., 0:Interest/1:CacheHit, AplID, SFCID, prefix, fromSFID@R_ID, toSFID, <-R/N+ID, Hop, Duration, @Host, @VM, @vCPU, TimeStamp
2020/12/02 01:46:52.592  ,Data., 0: ProcReturn/1: CacheReturn, AplID, SFCID, prefix, Proc.Time, fromSFID, R_ID->, toSFID@R/N+ID, Hop, ComTime, DataSize, @Host, @VM, @vCPU, BW, #ofSharedConnection, TimeStamp
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
