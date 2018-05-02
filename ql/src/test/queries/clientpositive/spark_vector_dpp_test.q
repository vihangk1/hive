--! qt:dataset:srcpart
set hive.optimize.ppd=true;
set hive.ppd.remove.duplicatefilters=true;
set hive.spark.dynamic.partition.pruning=true;
set hive.optimize.metadataonly=false;
set hive.optimize.index.filter=true;
set hive.vectorized.execution.enabled=true;
set hive.strict.checks.cartesian.product=false;

-- SORT_QUERY_RESULTS
set hive.auto.convert.join=true;
set hive.auto.convert.join.noconditionaltask = true;
set hive.auto.convert.join.noconditionaltask.size = 10000000;

select distinct ds from srcpart;
select distinct hr from srcpart;

create table srcpart_date stored as orc as select ds as ds, ds as `date` from srcpart group by ds;
create table srcpart_hour stored as orc as select hr as hr, hr as hour from srcpart group by hr;

-- with static pruning
EXPLAIN vectorization operator select count(*) from srcpart join srcpart_date on (srcpart.ds = srcpart_date.ds) join srcpart_hour on (srcpart.hr = srcpart_hour.hr) 
where srcpart_date.`date` = '2008-04-08' and srcpart.hr = 13;
select count(*) from srcpart join srcpart_date on (srcpart.ds = srcpart_date.ds) join srcpart_hour on (srcpart.hr = srcpart_hour.hr) 
where srcpart_date.`date` = '2008-04-08' and srcpart.hr = 13;

drop table if exists srcpart_hour;
drop table if exists srcpart_date;
